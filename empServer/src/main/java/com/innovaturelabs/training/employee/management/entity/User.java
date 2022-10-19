package com.innovaturelabs.training.employee.management.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.form.UserDetailForm;
import com.innovaturelabs.training.employee.management.form.UserEditForm;

@Entity(name = "user_tbl")
public class User {

    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1),
        REJECTED((byte) 2);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    public enum Role {
        EMPLOYEE((byte) 0),
        EMPLOYER((byte) 1),
        ADMIN((byte) 2);

        public final byte value;

        private Role(byte value) {
            this.value = value;
        }
    }

    public enum Qualification {
        NIL((byte) 0),
        SSLC((byte) 1),
        PLUSTWO((byte) 2),
        UG((byte) 3),
        PG((byte) 4);

        public final byte value;

        private Qualification(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 50)
    private String name;

    @Column(length = 50, unique = true)
    private String userName;

    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    private byte status;
    private byte role;
    private byte qualification;

    private boolean passwordResetRequest;

    @Column(length = 255)
    private String profilePic;

    @Column(length = 255)
    private String address;

    @Column(length = 10)
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(String name, String userName, String email, String password, byte role) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.status = Status.INACTIVE.value;

        this.role = role;

        this.qualification = Qualification.NIL.value;

        Date date = new Date();
        this.createDate = date;
        this.updateDate = date;
    }

    public User updateDetails(UserDetailForm form) {

        this.name = form.getName();

        setQualification(form.getQualification());

        this.address = form.getAddress();

        this.phone = form.getPhone();

        this.updateDate = new Date();

        return this;
    }

    public User updateDetails(UserEditForm form) {

        this.name = form.getName();

        this.email = form.getEmail();

        this.userName = form.getUserName();

        this.updateDate = new Date();

        return this;
    }

    public User delete() {

        this.status = User.Status.REJECTED.value;

        this.updateDate = new Date();

        return this;
    }

    public User resetPassword(String newPassword) {

        this.password = newPassword;

        this.passwordResetRequest = false;

        this.updateDate = new Date();

        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getRole() {
        return role;
    }

    public void setRole(byte role) {
        this.role = role;
    }

    public byte getQualification() {
        return qualification;
    }

    public void setQualification(byte qualification) {

        if (Arrays.asList(Qualification.values()).stream().map(q -> q.value).collect(Collectors.toList())
                .contains(qualification)) {
            this.qualification = qualification;
        } else {
            throw new BadRequestException("Qualification : " + qualification + " Not Allowed");
        }

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isPasswordResetRequest() {
        return passwordResetRequest;
    }

    public void setPasswordResetRequest(boolean passwordResetRequest) {
        this.passwordResetRequest = passwordResetRequest;
    }

}