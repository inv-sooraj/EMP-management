package com.project.EmployeeManagement.service;

import java.util.Collection;

import org.springframework.validation.Errors;

import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.LoginForm;
import com.project.EmployeeManagement.form.UserDetailForm;
import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.view.LoginView;
import com.project.EmployeeManagement.view.UserView;

public interface UserService {
    Collection<User> list();

    UserView add(UserForm form);

    UserView currentUser();

    LoginView login(LoginForm form, Errors errors) throws BadRequestException;

    LoginView refresh(String refreshToken) throws BadRequestException;

    UserView edit(Integer userId,UserDetailForm form)throws NotFoundException;


}
