package main.java.util;

import main.java.model.Expenses;
import main.java.model.User;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dell on 15-11-2015.
 */
public class Utility {

    public static String bytesToHex(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static <T> boolean isNotEmptyAndNotNullCollection(Collection<T> coll) {
        return (coll != null && !coll.isEmpty()) ? true : false;

    }

    public static List<User> filterCurrentMonthExpence(List<User> userList) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setTimeZero(cal);
        Date startDate = cal.getTime();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal
        setTimeZero(cal);
        Date endDate = cal.getTime();
        return filterExpenseBetweenDate(startDate, endDate, userList);
    }

    private static void setTimeZero(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
    }

    public static List<User> filterExpenseBetweenDate(Date startDate, Date endDate, List<User> userList) {
        final List<User> userFilterList = new ArrayList<>();
        userList.stream().forEach(user -> {
            userFilterList.add(user);
            List<Expenses> expense = user.getExpenseComment()
                    .stream()
                    .filter((ex) -> {
                        System.out.print("" + ex.getDate());
                        return ((ex.getDate().after(startDate) && ex.getDate().before(endDate))
                                ||
                                (ex.getDate().equals(startDate) || ex.getDate().equals(endDate))
                        );
                    })
                    .collect(Collectors.toList());
            BigDecimal totalOfTheMonth = expense.stream()
                    .map(Expenses::getAmount)
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
            user.setAmountSpent(totalOfTheMonth);
            user.setExpenseComment(expense);
        });
        return userFilterList;
    }
}
