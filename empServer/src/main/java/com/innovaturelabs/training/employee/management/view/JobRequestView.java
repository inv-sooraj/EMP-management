package com.innovaturelabs.training.employee.management.view;

import java.util.Date;

import com.innovaturelabs.training.employee.management.entity.JobRequest;
import com.innovaturelabs.training.employee.management.json.Json;

public class JobRequestView {

    private final int jobRequestId;

    private final Integer userId;
    private final String userName;

    private final Integer jobId;
    private final String jobTitle;
    private final Byte status;
    private final String remark;

    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public JobRequestView(JobRequest jobRequest) {
        this.jobRequestId = jobRequest.getJobRequestId();
        this.userId = jobRequest.getUser().getUserId();
        this.userName = jobRequest.getUser().getUserName();
        this.jobId = jobRequest.getJob().getJobId();
        this.jobTitle = jobRequest.getJob().getTitle();
        this.status = jobRequest.getStatus();
        this.remark = jobRequest.getRemark();
        this.createDate = jobRequest.getCreateDate();
        this.updateDate = jobRequest.getUpdateDate();
    }

    public int getJobRequestId() {
        return jobRequestId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public Byte getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getUserName() {
        return userName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

}
