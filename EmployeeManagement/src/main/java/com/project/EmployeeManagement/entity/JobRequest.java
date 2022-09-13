package com.project.EmployeeManagement.entity;

import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.project.EmployeeManagement.form.JobRequestForm;

@Entity
public class JobRequest {

    public static enum Status {
        APPROVE((byte) 0),
        PENDING((byte) 1),
        REJECT((byte) 2);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobReqId;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Job job;
    private byte status;

    private String remarks;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public JobRequest() {
    }

    public JobRequest(Integer jobReqId) {
        this.jobReqId = jobReqId;
    }

    public JobRequest(Integer jobReqId, User user, Job job, byte status, String remarks, Date createDate,
            Date updateDate) {
        this.jobReqId = jobReqId;
        this.user = user;
        this.job = job;
        this.status = status;
        this.remarks = remarks;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public JobRequest(JobRequestForm form, User userId) {


            this.user = userId;
            // this.job=jobId;
            this.remarks = form.getRemarks();
            this.status = Status.PENDING.value;
            Date dt = new Date();
    
            this.createDate = dt;
            this.updateDate = dt;
    
        }

    public Integer getJobReqId() {
        return jobReqId;
    }

    public void setJobReqId(Integer jobReqId) {
        this.jobReqId = jobReqId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
