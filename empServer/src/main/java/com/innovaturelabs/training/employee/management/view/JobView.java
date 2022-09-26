
package com.innovaturelabs.training.employee.management.view;

import java.util.Date;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.json.Json;

public class JobView {

    private final int jobId;
    private final String title;
    private final String description;
    private final Byte qualification;

    private final Integer openings;

    private final String userId;

    private final Byte status;

    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public JobView(Job job) {
        this.jobId = job.getJobId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.qualification = job.getQualification();
        this.openings = job.getOpenings();
        this.userId = job.getUser().getUserName();
        this.status = job.getStatus();
        this.createDate = job.getCreateDate();
        this.updateDate = job.getUpdateDate();
    }

    public int getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Byte getQualification() {
        return qualification;
    }

    public Integer getOpenings() {
        return openings;
    }

    public String getUserId() {
        return userId;
    }

    public Byte getStatus() {
        return status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

}
