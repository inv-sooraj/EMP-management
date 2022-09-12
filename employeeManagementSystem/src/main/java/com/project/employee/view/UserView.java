package com.project.employee.view;

import java.util.Date;

import com.project.employee.entity.User;
import com.project.employee.json.Json;


public class UserView {
	private final int userId;
    private final String userName;
    private final String password;
    private final String email;
    private final short role;
    private final short status;
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public UserView(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.password=user.getPassword();
        this.email = user.getEmail();
        this.role=user.getRole();
        this.status = user.getStatus();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();
    }

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public short getRole() {
		return role;
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
