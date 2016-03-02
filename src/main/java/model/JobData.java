package main.java.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by dell on 19-01-2016.
 */
public class JobData implements Serializable {

    @Id
    private String id;

    private String name;

    private String status;

    @NotNull
    private String email;
    @NotNull
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public enum JOB_STATUS {
        COMPLETED,
        STARTED,
        LOGGED;
    }
}
