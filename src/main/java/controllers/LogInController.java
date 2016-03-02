package main.java.controllers;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import main.java.model.User;
import main.java.mongo.IUserMongoRepository;
import main.java.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class LogInController {

    @Autowired
    IUserMongoRepository repository;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET, headers = {"Accept=application/json"})
    public ModelAndView index(ModelMap model) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("name", "neeraj");
        mv.setViewName("logIn");
        return mv;
    }

    @ApiOperation(value = "login", nickname = "ValidateLogIn")
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST, headers = {"Accept=application/json"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public User ValidateLogIn(@RequestBody User usrParam, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {

        List<User> users = repository.findByEmail(usrParam.getEmail());

        if (users.contains(usrParam)) {
            User dbUser = users.get(0);

            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(usrParam.getPassword().getBytes());
            byte[] passwordHashed = md.digest();
            if (!dbUser.getPassword().equals(Utility.bytesToHex(passwordHashed))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                usrParam = dbUser;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return usrParam;
    }

    @RequestMapping(value = "goto/{page}", method = RequestMethod.GET)
    public ModelAndView loginSuccess(ModelMap model, @PathVariable(value = "page") String page) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("name", "neeraj");
        mv.setViewName(page);
        return mv;
    }

}