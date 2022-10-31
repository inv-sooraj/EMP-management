
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
    @Json.DateFormat
    private final Date dob;
    private final String address;
    private final String city;
    private final String zipCode;
    private final String phone;
    @Json.DateTimeFormat
    private final Date createDate;
    @Json.DateTimeFormat
    private final Date updateDate;
    

    
    

    private final boolean hasProfilePic;

    public UserDetailView(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.userName = user.getName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.role = user.getRole();
        this.qualification = user.getQualification();
        this.dob = user.getDob();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.phone = user.getPhone();
        this.zipCode = user.getZipCode();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();
        this.hasProfilePic = !(user.getProfilePic() == null || user.getProfilePic().isEmpty());

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

    public Date getDob() {
        return dob;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
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
