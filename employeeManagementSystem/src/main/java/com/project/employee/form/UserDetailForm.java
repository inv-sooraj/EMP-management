package com.project.employee.form;

import javax.validation.constraints.Size;

	
public class UserDetailForm {
	
    @Size(max = 50)
    private String name;	
    @Size(max = 200)
    private String address;
    @Size(max = 20)
    private String phoneNo;
    
    private byte qualification; 
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public byte getQualification() {
		return qualification;
	}
	public void setQualification(byte qualification) {
		this.qualification = qualification;
	}
    

}
