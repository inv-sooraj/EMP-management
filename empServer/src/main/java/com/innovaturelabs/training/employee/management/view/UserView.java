
package com.innovaturelabs.training.employee.management.view;

import java.util.Date;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.json.Json;

public class UserView {

    private final int userId;
    private final String name;
    private final String userName;
    private final String email;
    private final short status;
    private final short role;
    private final short userType;
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public UserView(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.role = user.getRole();

        userType = user.getUserType();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();

    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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

    public String getUserName() {
        return userName;
    }

    public short getRole() {
        return role;
    }

    public short getUserType() {
        return userType;
    }   

}
