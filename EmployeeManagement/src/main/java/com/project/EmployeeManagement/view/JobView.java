package com.project.EmployeeManagement.view;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.json.Json;

import java.util.Date;

public class JobView {

    private final int jobId;
    private final String jobTitle;
    private final String jobDescription;
    private final Integer openings;
    private final byte qualification;
    private final byte status;
    private final int userId;
    private String userName;
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public JobView(Job job) {
        this.jobId = job.getJobId();
        this.jobTitle = job.getJobTitle();
        this.jobDescription = job.getJobDescription();
        this.openings = job.getOpenings();
        this.status = job.getStatus();
        this.qualification = job.getQualification();
        this.createDate = job.getCreateDate();
        this.updateDate = job.getUpdateDate();
        this.userId = job.getUser().getUserId();
        this.userName=job.getUser().getName();

    }

    public int getJobId() {
        return jobId;
    }

    public JobView(int jobId, String jobTitle, String jobDescription, Integer openings, byte qualification, byte status,
            int userId, Date createDate, Date updateDate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.openings = openings;
        this.qualification = qualification;
        this.status = status;
        this.userId = userId;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public Integer getOpenings() {
        return openings;
    }

    public byte getQualification() {
        return qualification;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public byte getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

}
