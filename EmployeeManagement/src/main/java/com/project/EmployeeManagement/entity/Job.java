package com.project.EmployeeManagement.entity;

import java.util.Date;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.project.EmployeeManagement.form.JobForm;

@Entity
public class Job {

    public static enum Qualification {
        SSLC((byte) 0),
        HIGHER((byte) 1),
        UG((byte) 2),
        PG((byte) 3);

        public final byte value;

        private Qualification(byte value) {
            this.value = value;
        }
    }

    public static enum Status {
        DELETED((byte) 0),
        ACTIVE((byte) 1),
        REJECT((byte) 2);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    private String jobTitle;
    private String jobDescription;
    private byte qualification;
    private byte status;
    private Integer openings;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public Job() {

    }

    public Job(Integer jobId) {
        this.jobId = jobId;

    }

    

    public Job(Integer jobId, String jobTitle, String jobDescription, byte qualification, byte status, Integer openings,
            User user, Date createDate, Date updateDate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.qualification = qualification;
        this.status = status;
        this.openings = openings;
        this.user = user;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Job(JobForm form, User userId) {

        this.user = userId;
        this.jobTitle = form.getJobTitle();
        this.jobDescription = form.getJobDescription();
        this.openings = form.getOpenings();
        this.qualification = Job.Qualification.HIGHER.value;
        this.status = Status.ACTIVE.value;
        Date dt = new Date();

        this.createDate = dt;
        this.updateDate = dt;

    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public byte getQualification() {
        return qualification;
    }

    public void setQualification(byte qualification) {
        this.qualification = qualification;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job update(JobForm form) {
        this.jobTitle = form.getJobTitle();
        this.jobDescription = form.getJobDescription();
        this.openings = form.getOpenings();
        Date dt = new Date();
        this.updateDate = dt;
        return this;
    }

}
