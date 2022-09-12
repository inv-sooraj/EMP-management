/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.training.employee.management.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.innovaturelabs.training.employee.management.form.validaton.Password;

public class LoginForm {

    @NotBlank
    @Size(max = 255)
    private String userName;
    @Password
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
