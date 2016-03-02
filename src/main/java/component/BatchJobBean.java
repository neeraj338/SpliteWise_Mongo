package main.java.component;

import main.java.model.JobData;
import main.java.mongo.IJobMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dell on 19-01-2016.
 */
@Component
public class BatchJobBean {
    private static final Logger LOG = LoggerFactory.getLogger(BatchJobBean.class);
    @Autowired
    IJobMongoRepository jobMongoRepo = null;
    @Autowired
    @Qualifier(value = "JobServiceImpl")
    private IJobService service = null;

    @Scheduled(cron = "0 0/1 * * * *")
    public void emailJob() {
        LOG.info(" started .. ");
        List<JobData> dataList = jobMongoRepo.findAll();
        if (!dataList.isEmpty()) {
            service.execute(dataList);
        }
        jobMongoRepo.deleteAll();
        LOG.info("finished .. ");
    }
}
