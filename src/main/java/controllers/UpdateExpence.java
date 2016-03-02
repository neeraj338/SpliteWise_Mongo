package main.java.controllers;

import main.java.model.User;
import main.java.mongo.IUserMongoRepository;
import main.java.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 09-01-2016.
 */
@RestController
public class UpdateExpence {

    @Autowired
    IUserMongoRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;

    private static void setTimeZero(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
    }

    @RequestMapping(value = {"/expeseAdd"}, method = RequestMethod.POST, headers = {"Accept=application/json"})
    public User addUpdateExpense(@RequestBody User user, HttpServletResponse response) throws IOException {
        List<User> users = repository.findByEmail(user.getEmail());

        if (users.contains(user)) {
            User dbUser = users.get(0);

            dbUser.setAmountSpent(user.getAmountSpent().add(dbUser.getAmountSpent() == null ? BigDecimal.ZERO : dbUser.getAmountSpent()));
            dbUser.getExpenseComment().add(user.getExpenseComment().get(0));
            repository.save(dbUser);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return user;
    }

    @RequestMapping(value = {"/userExpence/{isPreviousMonth}"}, method = RequestMethod.GET, produces = {"application/json"})
    public List<User> getUserExpences(@PathVariable("isPreviousMonth") boolean isPreviousMonth, HttpServletResponse res) {
        List<User> allUsers = this.repository.findAll();
        List<User> filteredData = new ArrayList<>();
        if (isPreviousMonth) {
            filteredData.addAll(getPreviousMonthData(allUsers));
        } else {
            filteredData.addAll(Utility.filterCurrentMonthExpence(allUsers));
        }
        res.setStatus(200);
        return filteredData;
    }

    private List<User> getPreviousMonthData(List<User> allUserList) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 1);
        setTimeZero(cal);
        Date firstDate = cal.getTime();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal
        setTimeZero(cal);
        Date lastDate = cal.getTime();
        List<User> filterData = Utility.filterExpenseBetweenDate(firstDate, lastDate, allUserList);
        return filterData;

    }
}
