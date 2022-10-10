package com.project.EmployeeManagement.form;

import com.project.EmployeeManagement.form.validations.Password;

public class ChangePasswordForm {

    @Password
    private String password;
    @Password
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
