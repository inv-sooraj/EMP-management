package com.innovaturelabs.training.employee.management.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.innovaturelabs.training.employee.management.json.Json;

public class UserDetailForm {

    @NotBlank
    @Size(max = 50)
    private String name;
    
    @Json.DateFormat
    private Date dob;

    @Size(max = 13)
    private String phone;

    @Size(max = 255)
    private String address;
    
    @Size(max = 255)
    private String city;
    
    @Size(max = 13)
    private String zipCode;

    private Byte qualification;

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Byte getQualification() {
        return qualification;
    }

    public void setQualification(Byte qualification) {
        this.qualification = qualification;
    }

}
