package main.java.mongo;


import main.java.model.JobData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by dell on 18-01-2016.
 */
public interface IJobMongoRepository extends MongoRepository<JobData, String> {

    List<JobData> findAll();
}
