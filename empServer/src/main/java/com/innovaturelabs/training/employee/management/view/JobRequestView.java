
package com.innovaturelabs.training.employee.management.view;

import java.util.Date;

import com.innovaturelabs.training.employee.management.entity.JobRequest;
import com.innovaturelabs.training.employee.management.json.Json;

public class JobRequestView {

    private final int jobRequestId;

    private final Integer userId;
    private final Integer jobId;
    private final Byte status;
    private final String feedback;
    private final String remark;

    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public JobRequestView(int jobRequestId, Integer userId, Integer jobId, Byte status, String feedback, String remark,
            Date createDate, Date updateDate) {
        this.jobRequestId = jobRequestId;
        this.userId = userId;
        this.jobId = jobId;
        this.status = status;
        this.feedback = feedback;
        this.remark = remark;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public JobRequestView(JobRequest jobRequest) {
        this.jobRequestId = jobRequest.getJobRequestId();
        this.userId = jobRequest.getUser().getUserId();
        this.jobId = jobRequest.getJob().getJobId();
        this.status = jobRequest.getStatus();
        this.feedback = jobRequest.getFeedback();
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

    public Integer   getJobId() {
        return jobId;
    }

    public Byte getStatus() {
        return status;
    }

    public String getFeedback() {
        return feedback;
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

}
