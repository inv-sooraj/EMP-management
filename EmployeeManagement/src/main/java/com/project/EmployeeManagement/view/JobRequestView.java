package com.project.EmployeeManagement.view;

import java.util.Date;

import com.project.EmployeeManagement.entity.JobRequest;
import com.project.EmployeeManagement.json.Json;

public class JobRequestView {

    private final int reqId;
    private final int userId;
    private final int jobId;
    private final byte status;
    private String remarks;
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public JobRequestView(JobRequest jobRequest) {
        this.reqId = jobRequest.getJobReqId();
        this.userId = jobRequest.getUser().getUserId();
        this.jobId = jobRequest.getJob().getJobId();
        this.remarks = jobRequest.getRemarks();
        this.createDate = jobRequest.getCreateDate();
        this.updateDate = jobRequest.getUpdateDate();
        this.status = jobRequest.getStatus();

    }

    public JobRequestView(int reqId, String remarks, int userId, int jobId, byte status, Date createDate,
            Date updateDate) {
        this.reqId = reqId;
        this.remarks = remarks;
        this.userId = userId;
        this.jobId = jobId;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public int getReqId() {
        return reqId;
    }

    public int getUserId() {
        return userId;
    }

    public int getJobId() {
        return jobId;
    }

    public byte getStatus() {
        return status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
