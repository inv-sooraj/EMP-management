
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
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    public UserView(int userId, String name, String userName, String email, short status, Date createDate,
            Date updateDate) {
        this.userId = userId;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public UserView(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.status = user.getStatus();
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

}
