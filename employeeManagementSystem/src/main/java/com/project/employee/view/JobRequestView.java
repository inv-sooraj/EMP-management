package com.project.employee.view;

import java.util.Date;

import com.project.employee.entity.JobRequest;
import com.project.employee.json.Json;

public class JobRequestView {
	private final Integer reqId;
	private final String remark;
	private final String jobId;
	private final String userId;
	private final short status;
	private final short requestStatus;

	@Json.DateTimeFormat
	private final Date createDate;
	@Json.DateTimeFormat
	private final Date updateDate;

	public JobRequestView(JobRequest jobRequest) {
		this.reqId = jobRequest.getReqId();
		this.remark = jobRequest.getRemark();
		this.jobId = jobRequest.getJob().getTitle();
		this.userId = jobRequest.getUserId().getName();
		this.status = jobRequest.getStatus();
		this.requestStatus = jobRequest.getRequestStatus();
		this.createDate = jobRequest.getCreateDate();
		this.updateDate = jobRequest.getUpdateDate();
	}

	public JobRequestView(Integer reqId, String remark, String jobId, String userId, short status,
			short requestStatus, Date createDate, Date updateDate) {

		this.reqId = reqId;
		this.remark = remark;
		this.jobId = jobId;
		this.userId = userId;
		this.status = status;
		this.requestStatus = requestStatus;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Integer getReqId() {
		return reqId;
	}

	public String getRemark() {
		return remark;
	}

	public String getJobId() {
		return jobId;
	}

	public String getUserId() {
		return userId;
	}

	public short getStatus() {
		return status;
	}

	public short getRequestStatus() {
		return requestStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

}
