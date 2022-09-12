package com.project.employee.view;

import java.util.Date;

import com.project.employee.entity.Job;
import com.project.employee.json.Json;

public class JobView {
	private final int jobId;
	private final String title;
	private final String description;
	private final Integer noOfOpenings;
	private final String createdBy;
	private final short qualification;
	private final short status;
	@Json.DateTimeFormat
	private final Date createDate;
	@Json.DateTimeFormat
	private final Date updateDate;

	public JobView(Job job) {
		this.jobId = job.getJobId();
		this.title = job.getTitle();
		this.description = job.getDescription();
		this.noOfOpenings = job.getNoOfOpenings();
		this.createdBy = job.getUser().getUserName();
		this.qualification = job.getQualification();
		this.status = job.getStatus();
		this.createDate = job.getCreateDate();
		this.updateDate = job.getUpdateDate();
	}
	
	

	



	public JobView(int jobId, String title, String description, Integer noOfOpenings, String createdBy,
			short qualification, short status, Date createDate, Date updateDate) {
		this.jobId = jobId;
		this.title = title;
		this.description = description;
		this.noOfOpenings = noOfOpenings;
		this.createdBy = createdBy;
		this.qualification = qualification;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}







	public int getJobId() {
		return jobId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getNoOfOpenings() {
		return noOfOpenings;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public short getQualification() {
		return qualification;
	}

	public short getStatus() {
		return status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

}
