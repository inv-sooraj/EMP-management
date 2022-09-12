package com.project.employee.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.project.employee.entity.User;
import com.project.employee.exception.BadRequestException;
import com.project.employee.exception.NotFoundException;
import com.project.employee.form.LoginForm;
import com.project.employee.form.UserDetailForm;
import com.project.employee.form.UserRegistrationForm;
import com.project.employee.view.LoginView;
import com.project.employee.view.UserView;

@Service
public interface UserService {
	Collection<User> list();

	UserView add(@Valid UserRegistrationForm form);
	
	LoginView login(LoginForm form, Errors errors) throws BadRequestException;
	
	LoginView refresh(String refreshToken) throws BadRequestException;
	
	UserView currentUser(); //finds currently logged in user detail

	UserView update(UserDetailForm form);
	
	void delete(Integer userId) throws NotFoundException;


}
