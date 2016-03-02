package main.java.component;

import main.java.model.JOB_CATEGORY;
import main.java.model.JobData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dell on 19-01-2016.
 */
@Component(value = "JobServiceImpl")
public class JobServiceImpl implements IJobService<List<JobData>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    @Autowired
    ApplicationContext appContext;

    @Override
    public void execute(List<JobData> jobDataList) {
        Map<String, List<JobData>> jobTypeMap = jobDataList.stream()
                .collect(Collectors.groupingBy(JobData::getCategory));
        Set<Map.Entry<String, List<JobData>>> jobMapEntrySet = jobTypeMap.entrySet();
        for (Iterator<Map.Entry<String, List<JobData>>> entryDataItr = jobMapEntrySet.iterator(); entryDataItr.hasNext(); ) {
            Map.Entry<String, List<JobData>> entryData = entryDataItr.next();

            JOB_CATEGORY category = JOB_CATEGORY.valueOf(entryData.getKey());
            Class<?> jobSerClass = category.getService();
            IJobService jobServiceBean = (IJobService) appContext.getBean(jobSerClass);
            jobServiceBean.execute(entryData.getValue());
        }
    }
}
