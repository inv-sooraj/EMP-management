package com.project.employee.view;

import java.util.Date;

import com.project.employee.entity.JobRequest;
import com.project.employee.json.Json;

public class JobRequestView {
	private final Integer reqId;
	private final String remark;
	private final Integer jobId;
	private final Integer userId;
	private final short status;
	@Json.DateTimeFormat
	private final Date createDate;
	@Json.DateTimeFormat
	private final Date updateDate;

	public JobRequestView(JobRequest jobRequest) {
		this.reqId = jobRequest.getReqId();
		this.remark = jobRequest.getRemark();
		this.jobId = jobRequest.getJob().getJobId();
		this.userId = jobRequest.getUserId().getUserId();
		this.status = jobRequest.getStatus();
		this.createDate = jobRequest.getCreateDate();
		this.updateDate = jobRequest.getUpdateDate();
	}
	
	

	public JobRequestView(Integer reqId, String remark, Integer jobId, Integer userId, short status, Date createDate,
			Date updateDate) {
		
		this.reqId = reqId;
		this.remark = remark;
		this.jobId = jobId;
		this.userId = userId;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}



	public Integer getReqId() {
		return reqId;
	}

	public String getRemark() {
		return remark;
	}

	public Integer getJobId() {
		return jobId;
	}

	public Integer getUserId() {
		return userId;
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
