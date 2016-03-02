package main.java.model;

import main.java.services.EmailService;
import main.java.services.ReportService;

/**
 * Created by dell on 19-01-2016.
 */
public enum JOB_CATEGORY {
    EMAIL {
        public Class<?> getService() {
            return EmailService.class;
        }
    },
    GEN_REPORT {
        public Class<?> getService() {
            return ReportService.class;
        }
    };

    public abstract Class<?> getService();
}
