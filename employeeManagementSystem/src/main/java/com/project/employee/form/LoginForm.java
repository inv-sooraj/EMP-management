package com.project.employee.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.project.employee.form.validaton.Password;


public class LoginForm {
	@NotBlank
    @Size(max = 255)	
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
