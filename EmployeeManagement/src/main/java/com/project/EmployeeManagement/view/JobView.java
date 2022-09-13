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
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public JobView(Job job) {
        this.jobId = job.getJobId();
        this.jobTitle = job.getJobTitle();
        this.jobDescription = job.getJobDescription();
        this.openings = job.getOpenings();
        this.qualification = job.getQualification();
        this.createDate = job.getCreateDate();
        this.updateDate = job.getUpdateDate();

    }
    

    public int getJobId() {
        return jobId;
    }

    public JobView(int jobId, String jobTitle, String jobDescription, Integer openings, byte qualification,
            Date createDate, Date updateDate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.openings = openings;
        this.qualification = qualification;
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

}
