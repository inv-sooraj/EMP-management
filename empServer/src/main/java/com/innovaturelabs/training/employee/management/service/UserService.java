/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.training.employee.management.service;

import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.form.UserForm;
import com.innovaturelabs.training.employee.management.view.LoginView;
import com.innovaturelabs.training.employee.management.view.UserView;

public interface UserService {

    UserView add(UserForm form);

    // UserView update(Integer userId, UserForm form) throws NotFoundException;

    UserView updateUserName(Integer userId, String name) throws NotFoundException;

    // void delete(Integer userId) throws NotFoundException;

    UserView currentUser();

    LoginView login(LoginForm form, Errors errors) throws BadRequestException;

    LoginView refresh(String refreshToken) throws BadRequestException;

    Page<UserView> list(Integer page);

    // Collection<String> listEmails();

}
