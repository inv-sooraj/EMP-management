package com.innovaturelabs.training.employee.management.form;

import com.innovaturelabs.training.employee.management.form.validaton.Password;

public class ChangePasswordForm {
    @Password
    private String currentPassword;
    @Password
    private String password;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
