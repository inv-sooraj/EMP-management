
package com.innovaturelabs.training.employee.management.view;

import java.util.Date;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.json.Json;

public class UserDetailView {

    private final int userId;
    private final String name;
    private final String userName;
    private final String email;
    private final short status;
    private final short role;
    private final short qualification;

    private final String address;
    private final String phone;

    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;

    private final boolean hasProfilePic;

    public UserDetailView(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.status = user.getStatus();

        this.role = user.getRole();

        this.qualification = user.getQualification();

        this.address = user.getAddress();

        this.phone = user.getPhone();

        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();

        this.hasProfilePic = (user.getProfilePic() != null);

    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public short getStatus() {
        return status;
    }

    public short getQualification() {
        return qualification;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public short getRole() {
        return role;
    }

    public boolean isHasProfilePic() {
        return hasProfilePic;
    }

}
