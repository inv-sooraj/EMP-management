package com.project.EmployeeManagement.form;

import javax.validation.constraints.Size;

public class UserDetailForm {
    


    @Size(max = 225)
    private String address;

    @Size(max = 10)
    private String phone;

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

    

}
