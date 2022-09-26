package com.project.employee.view;

import com.project.employee.entity.User;

public class UserDetailView extends UserView {

	private final String name;
	private final String address;
	private final String phoneNo;
	private final short qualification;

//	public UserDetailView(User user) {
//		super(				user.getUserId(),
//				user.getUserName(),
//				user.getEmail(),
//				user.getRole(),
//				user.getStatus(),
//				user.getCreateDate(),
//				user.getUpdateDate()
//				
//	);
//	this.name=user.getName();
//	this.address=user.getAddress();
//	this.phoneNo=user.getPhoneNo();
//	this.qualification=user.getQualification();
//	}


	public UserDetailView(User user) {
		super(user);
		this.name = user.getName();
		this.address =user.getAddress();
		this.phoneNo = user.getPhoneNo();
		this.qualification = user.getQualification();
	}
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public short getQualification() {
		return qualification;
	}

}
