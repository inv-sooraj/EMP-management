package com.project.EmployeeManagement.view;
import java.util.Date;

import com.project.EmployeeManagement.entity.JobRequest;
import com.project.EmployeeManagement.json.Json;


public class JobRequestView {

    private final int reqId;

    private  String remarks;
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate; 


    public JobRequestView(JobRequest jobRequest) {
        this.reqId=jobRequest.getJobReqId();
        // this.jobI
        this.remarks=jobRequest.getRemarks();
        this.createDate=jobRequest.getCreateDate();
        this.updateDate=jobRequest.getUpdateDate();
    }


    public JobRequestView(int reqId,String remarks, Date createDate, Date updateDate) {
        this.reqId = reqId;
        this.remarks=remarks;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }


    public int getReqId() {
        return reqId;
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
