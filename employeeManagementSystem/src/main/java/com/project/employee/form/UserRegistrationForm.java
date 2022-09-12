package com.project.employee.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.project.employee.form.validaton.Password;

public class UserRegistrationForm {
		
        @NotBlank
        private String userName;
        @Password
        private String password;
        @Email
        private String email;
        private byte role;
        
        
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
    	public String getEmail() {
    		return email;
    	}
    	public void setEmail(String email) {
    		this.email = email;
    	}
		public byte getRole() {
			return role;
		}
		public void setRole(byte role) {
			this.role = role;
		}
        
        

    }

