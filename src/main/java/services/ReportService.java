package main.java.services;

import main.java.component.IJobService;
import main.java.model.Expenses;
import main.java.model.JobData;
import main.java.model.JobReportHistory;
import main.java.model.User;
import main.java.mongo.IJobReportHistoryMongoRepository;
import main.java.mongo.IUserMongoRepository;
import main.java.report.UserExpenseReportTO;
import main.java.util.Utility;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dell on 23-01-2016.
 */
@Component
public class ReportService implements IJobService<List<JobData>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    @Autowired
    IUserMongoRepository userMongoRepo;
    @Autowired
    IJobReportHistoryMongoRepository jobHisMongoRepo;
    @Autowired
    GridFsTemplate gridFsTemplate;


    @Override
    public void execute(List<JobData> jobDataList) {
        if (Utility.isNotEmptyAndNotNullCollection(jobDataList)) {
            Map<String, List<JobData>> groupByEmailIdMap =
                    jobDataList.stream().collect(Collectors.groupingBy(JobData::getEmail));

            LOGGER.info("Report Job Execution Started ...");
            try {
                Iterator<Map.Entry<String, List<JobData>>> groupByemailIter
                        = groupByEmailIdMap.entrySet().iterator();
                while (groupByemailIter.hasNext()) {
                    Map.Entry<String, List<JobData>> jobdataEntry = groupByemailIter.next();

                    InputStream input = new FileInputStream(new File("src/reports/splitwise_expences_report.jrxml"));
                    List<UserExpenseReportTO> reportTOList = fillReportTo();
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    // Generate the report
                    JasperReport jasperReport = JasperCompileManager.compileReport(input);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(reportTOList));

                    OutputStream output = new FileOutputStream(new File("JasperReport.pdf"));
                    JasperExportManager.exportReportToPdfStream(jasperPrint, output);
                    output.close();

                    JobReportHistory jobHisData = new JobReportHistory();
                    JobData jobData = jobdataEntry.getValue().get(0);
                    jobHisData.setEmail(jobData.getEmail());
                    jobHisData.setCompletedTime(new Date());
                    jobHisData.setUserName(jobData.getName());
                    JobReportHistory savedData = jobHisMongoRepo.insert(jobHisData);
                    gridFsTemplate.store(new FileInputStream(new File("JasperReport.pdf")), savedData.getId() + ".pdf");
                }

                LOGGER.info("Job Execution End :: ");
            } catch (Exception e) {
                LOGGER.info("Exception :: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    private List<UserExpenseReportTO> fillReportTo() {
        List<UserExpenseReportTO> reportToList = new ArrayList<>();
        List<User> fetchAllUser = userMongoRepo.findAll();
        List<User> currentMonthExpenseList = Utility.filterCurrentMonthExpence(fetchAllUser);
        for (User u : currentMonthExpenseList) {

            List<Expenses> expenseList = u.getExpenseComment();
            for (Expenses exp : expenseList) {
                UserExpenseReportTO reportTO = new UserExpenseReportTO();
                reportTO.setName(u.getFirstName() + " " + u.getLastName());
                reportTO.setEmail(u.getEmail());

                reportTO.setDate(exp.getDate());
                reportTO.setComment(exp.getComment());
                reportTO.setAmount(exp.getAmount());
                reportToList.add(reportTO);
            }
        }
        return reportToList;
    }
}
