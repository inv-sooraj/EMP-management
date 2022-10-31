package com.innovaturelabs.training.employee.management.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OAuth2RegisterForm {
    @NotBlank
    private String userName;
    
    @NotBlank
    private String idToken;

    @NotNull
    private Byte role;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Byte getRole() {
        return role;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    

}
