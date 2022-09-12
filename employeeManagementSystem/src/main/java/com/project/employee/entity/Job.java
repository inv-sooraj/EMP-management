package com.project.employee.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.project.employee.form.JobForm;

@Entity
public class Job {
	public static enum JobStatus {
        PENDING((byte) 0),
        APPROVED((byte) 1),
        COMPLETED((byte) 2),
        DELETED((byte) 3);

        public final byte value;

        private JobStatus(byte value) {
            this.value = value;
        }
    }
	public static enum Qualification {
        TENTH((byte) 0),
        TWELFTH((byte) 1),
        UG((byte) 2),
        PG((byte) 3);	

        public final byte value;

        private Qualification(byte value) {
            this.value = value;
        }
    }
	
	public static enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;
	private String title;
	private String description;
	private Integer noOfOpenings;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
	private byte qualification;
	private byte jobStatus;
	private byte status;
	@Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    public Job() {}
    
    public Job(Integer jobId) {
    	this.jobId=jobId;
    }

    public Job(JobForm form,User user) {
    	this.user=user;
    	this.title=form.getTitle();
    	this.description=form.getDescription();
    	this.noOfOpenings=form.getNoOfOpenings();
    	this.qualification=form.getQualification();
    	this.jobStatus=Job.JobStatus.PENDING.value;
    	this.status=Job.Status.ACTIVE.value;
    	Date dt = new Date();
        this.createDate = dt;
        this.updateDate = dt;	
    }
    
    public Job update(JobForm form) {
    	this.title=form.getTitle();
    	this.description=form.getDescription();
    	this.noOfOpenings=form.getNoOfOpenings();
    	this.qualification=form.getQualification();
    	Date dt = new Date();
        this.updateDate = dt;
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

	public Integer getNoOfOpenings() {
		return noOfOpenings;
	}

	public void setNoOfOpenings(Integer	 noOfOpenings) {
		this.noOfOpenings = noOfOpenings;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public byte getQualification() {
		return qualification;
	}

	public void setQualification(byte qualification) {
		this.qualification = qualification;
	}
	
	
	public byte getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(byte jobStatus) {
		this.jobStatus = jobStatus;
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

    
}
