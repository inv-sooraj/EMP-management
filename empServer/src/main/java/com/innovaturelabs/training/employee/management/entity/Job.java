package com.innovaturelabs.training.employee.management.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.innovaturelabs.training.employee.management.form.JobForm;

@Entity(name = "job_tbl")
public class Job {

    public enum Status {
        PENDING((byte) 0),
        APPROVED((byte) 1),
        COMPLETED((byte) 2),
        DELETED((byte) 3);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    public enum Qualification {
        SSLC((byte) 0),
        PLUSTWO((byte) 1),
        UG((byte) 2),
        PG((byte) 3);

        public final byte value;

        private Qualification(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(length = 50)
    private String title;

    @Column(length = 255)
    private String description;

    private Byte status;

    private Byte qualification;

    private Integer openings;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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

    public Job(String title, String description, Byte qualification, Integer openings,
            Integer userId, Byte status) {

        this.title = title;
        this.description = description;

        this.status = status;
        this.qualification = qualification;
        this.openings = openings;
        this.user = new User(userId);

        Date date = new Date();
        this.createDate = date;
        this.updateDate = date;
    }

    public Job update(JobForm form) {

        this.title = form.getTitle();
        this.description = form.getDescription();

        this.qualification = form.getQualification();
        this.openings = form.getOpenings();

        this.updateDate = new Date();

        return this;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getQualification() {
        return qualification;
    }

    public void setQualification(Byte qualification) {
        this.qualification = qualification;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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