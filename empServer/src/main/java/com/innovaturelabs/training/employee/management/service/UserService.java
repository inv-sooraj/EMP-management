
package com.innovaturelabs.training.employee.management.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.Errors;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.form.UserDetailForm;
import com.innovaturelabs.training.employee.management.form.UserForm;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.LoginView;
import com.innovaturelabs.training.employee.management.view.UserDetailView;
import com.innovaturelabs.training.employee.management.view.UserView;

public interface UserService {

    UserView add(UserForm form);

    // UserView update(Integer userId, UserForm form) throws NotFoundException;

    UserView updateUserName(Integer userId, String name) throws NotFoundException;

    void delete(Integer userId);

    UserView currentUser();

    LoginView login(LoginForm form, Errors errors) throws BadRequestException;

    LoginView refresh(String refreshToken) throws BadRequestException;

    Pager<UserView> list(Integer page,Integer limit,String sortBy,String search);


    UserDetailView updateUserDetails(UserDetailForm form);

    void userCsv(HttpServletResponse httpServletResponse);


}
