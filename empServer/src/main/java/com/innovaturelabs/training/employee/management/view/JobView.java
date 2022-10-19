
package com.innovaturelabs.training.employee.management.view;

import java.util.Date;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.json.Json;

public class JobView {

    private final int jobId;
    private final String title;
    private final String description;
    private final Byte qualification;

    private final Integer openings;

    private final Integer userId;

    private final String userName;

    private final Byte status;

    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    private final boolean eligible;

    public JobView(Job job) {
        this.jobId = job.getJobId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.qualification = job.getQualification();
        this.openings = job.getOpenings();
        this.userName = job.getUser().getUserName();
        this.userId = job.getUser().getUserId();
        this.status = job.getStatus();
        this.createDate = job.getCreateDate();
        this.updateDate = job.getUpdateDate();

        this.eligible = false;
    }

    public JobView(Job job, User user) {
        this.jobId = job.getJobId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.qualification = job.getQualification();
        this.openings = job.getOpenings();
        this.userId = job.getUser().getUserId();
        this.userName = job.getUser().getUserName();
        this.status = job.getStatus();
        this.createDate = job.getCreateDate();
        this.updateDate = job.getUpdateDate();

        this.eligible = user.getQualification() >= job.getQualification();
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

    public Integer getUserId() {
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

    public boolean isEligible() {
        return eligible;
    }

    public String getUserName() {
        return userName;
    }

}
