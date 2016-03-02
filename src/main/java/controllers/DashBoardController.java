package main.java.controllers;

import main.java.model.Expenses;
import main.java.model.PIChart;
import main.java.model.User;
import main.java.mongo.IUserMongoRepository;
import main.java.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 01-03-2016.
 */
@RestController
public class DashBoardController {

    @Autowired
    IUserMongoRepository repository;

    @RequestMapping(value = {"/getPiChartData"}, method = RequestMethod.GET, produces = {"application/json"})
    public List<PIChart> getUserExpences(HttpServletResponse res) {
        List<User> allUsers = this.repository.findAll();
        List<User> filteredData = new ArrayList<>();
        List<PIChart> piChartData = new ArrayList<>();
        filteredData.addAll(Utility.filterCurrentMonthExpence(allUsers));
        filteredData.stream().forEach((usr) -> {
            PIChart pi = new PIChart();
            pi.setLabel(usr.getFirstName());
            BigDecimal totalExp = usr.getExpenseComment().stream().map(Expenses::getAmount).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
            pi.setCount(totalExp.intValue());
            piChartData.add(pi);
        });
        res.setStatus(200);

        return piChartData;
    }
}
