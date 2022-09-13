
package com.innovaturelabs.training.employee.management.view;

import java.util.Date;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.json.Json;

public class JobView {

    private final int jobId;
    private final String title;
    private final String description;
    private final Byte qualifivation;

    private final Integer openings;

    private final Integer userId;

    private final Byte status;

    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public JobView(Job job) {
        this.jobId = job.getJobId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.qualifivation = job.getQualification();
        this.openings = job.getOpenings();
        this.userId = job.getUser().getUserId();
        this.status = job.getStatus();
        this.createDate = job.getCreateDate();
        this.updateDate = job.getUpdateDate();
    }

    public JobView(int jobId, String title, String description, Byte qualifivation, Integer openings, Integer userId,
            Byte status, Date createDate, Date updateDate) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.qualifivation = qualifivation;
        this.openings = openings;
        this.userId = userId;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
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

    public Byte getQualifivation() {
        return qualifivation;
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

}
