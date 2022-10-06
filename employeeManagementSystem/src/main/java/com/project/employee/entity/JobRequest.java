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

import com.project.employee.form.JobRequestForm;
@Entity
public class JobRequest {
	public static enum RequestStatus {
        PENDING((byte) 0),
        APPROVED((byte) 1),
        REJECTED((byte) 2);

        public final byte value;

        private RequestStatus(byte value) {
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
    private Integer reqId;
	private String remark;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Job job;
	private byte requestStatus;
	private byte status;
	@Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    public JobRequest() {}
    
    public JobRequest(Integer reqId) {
    	this.reqId=reqId;
    }

    public JobRequest(Integer jobId,JobRequestForm form,Integer userId) {
    	this.user=new User(userId);
    	this.job=new Job(jobId);
    	this.remark=form.getRemark();	
    	this.requestStatus=JobRequest.RequestStatus.PENDING.value;
    	this.status=JobRequest.Status.ACTIVE.value;
    	Date dt = new Date();
        this.createDate = dt;
        this.updateDate = dt;	
    }				
    
    public  JobRequest update(Integer status) {
		
    	 if(status==0) {
			this.requestStatus=JobRequest.RequestStatus.PENDING.value;		
		}else if(status==1) {
			this.requestStatus=JobRequest.RequestStatus.APPROVED.value;
		}else if(status==2) {
			this.requestStatus=JobRequest.RequestStatus.REJECTED.value;
		}
    	 Date dt = new Date();
         this.updateDate = dt;
		return this;
	}
    
    public JobRequest delete() {

        this.status = JobRequest.Status.INACTIVE.value;

        this.updateDate = new Date();
        return this;
    }
	public Integer getReqId() {
		return reqId;
	}
	public void setReqId(Integer reqId) {
		this.reqId = reqId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	public User getUserId() {
		return user;
	}

	public void setUserId(User userId) {
		this.user = userId;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public byte getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(byte requestStatus) {
		this.requestStatus = requestStatus;
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
	}	}
