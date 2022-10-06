package com.innovaturelabs.training.employee.management.form;

import com.innovaturelabs.training.employee.management.form.validaton.Password;

public class ChangePasswordForm {
    @Password
    private String currentPassword;
    @Password
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
