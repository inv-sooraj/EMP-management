package com.project.EmployeeManagement.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.project.EmployeeManagement.form.validations.Password;

public class LoginForm {

    @NotNull
    @Size(max=50)
    private String userName;

    @Password
    private String password;

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

   

    

}
