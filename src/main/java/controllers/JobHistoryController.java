package main.java.controllers;

import main.java.model.JobReportHistory;
import main.java.mongo.IJobReportHistoryMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dell on 24-01-2016.
 */
@RestController
public class JobHistoryController {

    @Autowired
    IJobReportHistoryMongoRepository jobHisMongoRepo;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @RequestMapping(value = {"/getJobHistoryData"}, method = RequestMethod.GET, headers = {"Accept=application/json"})
    public List<?> getJobHistoryData() {
        List<JobReportHistory> jobHistoryList = jobHisMongoRepo.findAll();
        return jobHistoryList;
    }

    @RequestMapping(value = {"/deleteJobHistory/{id}"}, method = RequestMethod.GET, headers = {"Accept=application/json"})
    public void deleteHistoryById(@PathVariable("id") String id) {
        jobHisMongoRepo.delete(id);
        Criteria crit = Criteria.where("filename").is(id + ".pdf");
        Query query = new Query();
        query.addCriteria(crit);
        gridFsTemplate.delete(query);
    }
}
