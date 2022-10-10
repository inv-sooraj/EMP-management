package com.project.EmployeeManagement.form;

import javax.validation.constraints.Size;

import com.project.EmployeeManagement.form.validations.Password;

public class UserDetailForm {

    // private MultipartFile image;
    private String userName;
    private String name;
    private String email;
    @Password
    private String password;

    @Size(max = 225)
    private String address;

    @Size(max = 10)
    private String phone;

    private byte qualification;

    private byte role;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public byte getQualification() {
        return qualification;
    }

    public void setQualification(byte qualification) {
        this.qualification = qualification;
    }

    public byte getRole() {
        return role;
    }

    public void setRole(byte role) {
        this.role = role;
    }

    // public MultipartFile getImage() {
    // return image;
    // }

    // public void setImage(MultipartFile image) {
    // this.image = image;
    // }

}
