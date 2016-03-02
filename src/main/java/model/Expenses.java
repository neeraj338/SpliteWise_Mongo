package main.java.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dell on 09-01-2016.
 */
public class Expenses {

    private String comment;
    private Date date;

    private BigDecimal amount = BigDecimal.ZERO;

    public Expenses() {

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date paramDate) {
        this.date = paramDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
