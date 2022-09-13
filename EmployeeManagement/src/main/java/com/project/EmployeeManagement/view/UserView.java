package com.project.EmployeeManagement.view;

import java.util.Date;

import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.json.Json;

public class UserView {

    private final int userId;
    private final String userName;
    private final String name;
    private final String email;
    private final short status;
    private final String address;
    private final String phone;
    private final short role;
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public UserView(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.name = user.getName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.address=user.getAddress();
        this.phone=user.getPhone();
        this.role=user.getRole();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public short getStatus() {
        return status;
    }
    public short getRole() {
        return role;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }



}
