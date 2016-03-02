package main.java.controllers;

import main.java.model.User;
import main.java.mongo.IUserMongoRepository;
import main.java.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by dell on 06-11-2015.
 */
@RestController
public class NewUserRegController {

    @Autowired
    IUserMongoRepository repository;

    @RequestMapping(value = {"/newUser"}, method = RequestMethod.POST, headers = {"Accept=application/json"})
    public ModelAndView registerNewUser(@RequestBody User newUser, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {

        List<User> users = repository.findByEmail(newUser.getEmail());

        if (!users.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(newUser.getPassword().getBytes());
            byte[] passwordHashed = md.digest();
            newUser.setPassword(Utility.bytesToHex(passwordHashed));
            repository.save(newUser);

        }

        ModelAndView mv = new ModelAndView();
        mv.setViewName("logIn");

        return mv;
    }
}
