package main.java.services;

import com.mongodb.gridfs.GridFSDBFile;
import main.java.component.IJobService;
import main.java.model.JobData;
import main.java.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by dell on 19-01-2016.
 */
@Component
public class EmailService implements IJobService<List<JobData>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    GridFsTemplate mongoFileTemplate;

    @Override
    public void execute(List<JobData> emailJobList) {

        if (Utility.isNotEmptyAndNotNullCollection(emailJobList)) {
            Map<String, List<JobData>> groupByEmailMap =
                    emailJobList.stream().collect(Collectors.groupingBy(JobData::getEmail));
            Iterator<Map.Entry<String, List<JobData>>> groupBymapIter = groupByEmailMap.entrySet().iterator();

            while (groupBymapIter.hasNext()) {
                Map.Entry<String, List<JobData>> entry = groupBymapIter.next();
                // Recipient's email ID needs to be mentioned.
                String to = entry.getKey();
                LOGGER.info("Mail to :: {}", to);
                // Sender's email ID needs to be mentioned
                sendEmail(to);
            }
        }
    }

    private void sendEmail(String to) {
        String from = "splitwise.com";

        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("neeraj338@gmail.com", "ahhpd5064d");//userid and password for "from" email address
            }
        };

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties, authenticator);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Send the actual HTML message, as big as you like
            //message.setContent("<h1>hello :) </h1>", "text/html");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new FileDataSource("JasperReport.pdf")));
            messageBodyPart.setFileName("JasperReport.pdf");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);


            // Send message
            Transport.send(message);
            LOGGER.info("Sent message successfully....{}", to);
        } catch (MessagingException mex) {
            LOGGER.info(" MessagingException :: {}", mex);
            throw new RuntimeException("Exception in sending EMail :: {}", mex);
        }
    }

    private GridFSDBFile fetchLetstReport() {
        InputStream is = null;
        Query q = new Query();

        GridFSDBFile file = mongoFileTemplate.findOne(new Query()
                .with(new Sort(Sort.Direction.DESC, "uploadDate")));
        return file;
    }
}
