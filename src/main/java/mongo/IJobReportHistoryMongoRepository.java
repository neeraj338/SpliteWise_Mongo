package main.java.mongo;

import main.java.model.JobReportHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by dell on 23-01-2016.
 */
public interface IJobReportHistoryMongoRepository extends MongoRepository<JobReportHistory, String> {

    //public void deleteById(@Param("id") String id);
}
