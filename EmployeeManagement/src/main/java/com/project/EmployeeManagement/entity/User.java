package com.project.EmployeeManagement.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class User {

    public static enum Status {
        DELETED((byte) 0),
        ACTIVE((byte) 1),
        REJECT((byte) 2);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    public static enum Role {
        ADMIN((byte) 0),
        EMPLOYER((byte) 1),
        USER((byte) 2);

        public final byte value;

        private Role(byte value) {
            this.value = value;
        }
    }

    public static enum Qualification {
        SSLC((byte) 0),
        HIGHER((byte) 1),
        UG((byte) 2),
        PG((byte) 3);

        public final byte value;

        private Qualification(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 50)
    private String userName;
    private String password;
    private byte status;
    private String email;
    private String name;
    private String address;
    private String phone;
    private byte role;
    private byte qualification;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public User(){}

    public User(Integer userId) {
        this.userId = userId;
    }


    public User(String userName, String name, String email, String password) {
        this.userName = userName;
        this.name=name;
        this.email = email;
        this.password = password;

        this.status = Status.ACTIVE.value;
        this.role=Role.USER.value;
        Date dt = new Date();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public User(String userName, String name, String email, String password,Byte role) {
        this.userName = userName;
        this.name=name;
        this.email = email;
        this.password = password;

        this.status = Status.ACTIVE.value;
        this.role=role;
        Date dt = new Date();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        this.qualification = qualification;
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
    
    

}