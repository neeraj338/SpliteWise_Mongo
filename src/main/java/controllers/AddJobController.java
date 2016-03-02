package main.java.controllers;

import com.mongodb.gridfs.GridFSDBFile;
import main.java.model.JobData;
import main.java.mongo.IJobMongoRepository;
import main.java.mongo.IUserMongoRepository;
import net.sf.jasperreports.engine.JRException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dell on 20-01-2016.
 */
@RestController
public class AddJobController {
    @Autowired
    public MongoOperations mongoOperation;
    @Autowired
    IJobMongoRepository jobMongoRepo;
    @Autowired
    IUserMongoRepository userMongoRepo;
    @Autowired
    GridFsTemplate gridFsTemplate;

    @RequestMapping(value = {"/addJob"}, method = RequestMethod.POST, headers = {"Accept=application/json"})
    public void registerNewUser(@RequestBody JobData jobData, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        jobMongoRepo.insert(jobData);
    }

    @RequestMapping(value = {"/genratePDF/{id}"}, method = RequestMethod.GET, headers = {"Accept=application/json"})
    public void downloadPFD(@PathVariable("id") String id, HttpServletResponse response) throws IOException, JRException {

        Query query = new Query();
        Criteria filterCriteria = Criteria.where("filename").is(id + ".pdf");
        query.addCriteria(filterCriteria);
        GridFSDBFile file = gridFsTemplate.findOne(query);
        response.setContentType(file.getContentType());

        response.setHeader("Content-disposition", "inline; filename=Expense.pdf");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "No-cache");
        // Export the report as a PDF
        IOUtils.copyLarge(file.getInputStream(), response.getOutputStream());
    }
}
