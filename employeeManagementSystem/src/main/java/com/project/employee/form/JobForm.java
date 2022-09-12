package com.project.employee.form;

public class JobForm {
    private String title;
    private String description;
    private int noOfOpenings;
//    private byte status;
    private byte qualification;
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
	public int getNoOfOpenings() {
		return noOfOpenings;
	}
	public void setNoOfOpenings(int noOfOpenings) {
		this.noOfOpenings = noOfOpenings;
	}
//	public byte getStatus() {
//		return status;
//	}
//	public void setStatus(byte status) {
//		this.status = status;
//	}
	public byte getQualification() {
		return qualification;
	}
	public void setQualification(byte qualification) {
		this.qualification = qualification;
	}
    
    
}
