package com.innovaturelabs.training.employee.management.form;

import javax.validation.constraints.Size;

public class UserDetailForm {

    @Size(min = 10, max = 12)
    private String phone;

    @Size(max = 255)
    private String address;

    private Byte qualification;

    public String getPhone() {
        return phone;
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

    public Byte getQualification() {
        return qualification;
    }

    public void setQualification(Byte qualification) {
        this.qualification = qualification;
    }


    

    
}
