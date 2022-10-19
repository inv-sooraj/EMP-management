package com.innovaturelabs.training.employee.management.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;

@Entity(name = "job_request_tbl")
public class JobRequest {

    public enum Status {
        PENDING((byte) 0),
        APPROVED((byte) 1),
        REJECT((byte) 2);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobRequestId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Job job;

    private Byte status;

    @Column(length = 255)
    private String remark;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public JobRequest() {
    }

    public JobRequest(Integer userId, Integer jobId) {
        this.user = new User(userId);
        this.job = new Job(jobId);
        this.status = JobRequest.Status.PENDING.value;

        this.remark = "";

        Date date = new Date();
        this.createDate = date;
        this.updateDate = date;
    }

    public Integer getJobRequestId() {
        return jobRequestId;
    }

    public void setJobRequestId(Integer jobRequestId) {
        this.jobRequestId = jobRequestId;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {

        if (Arrays.asList(Status.values()).stream().map(q -> q.value).collect(Collectors.toList())
                .contains(status)) {
            this.status = status;
        } else {
            throw new BadRequestException("Status : " + status + " Not Allowed");
        }
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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