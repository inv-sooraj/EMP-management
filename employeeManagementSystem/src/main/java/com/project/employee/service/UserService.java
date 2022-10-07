package com.project.employee.service;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.project.employee.entity.User;
import com.project.employee.exception.BadRequestException;
import com.project.employee.exception.NotFoundException;
import com.project.employee.features.Pager;
import com.project.employee.form.ChangePasswordForm;
import com.project.employee.form.ImageForm;
import com.project.employee.form.LoginForm;
import com.project.employee.form.UserDetailForm;
import com.project.employee.form.UserRegistrationForm;
import com.project.employee.view.LoginView;
import com.project.employee.view.UserDetailView;
import com.project.employee.view.UserView;

@Service
public interface UserService {
//	Collection<User> list();

	Pager<UserView> list(Integer page, Integer limit, String sortBy,Boolean desc,String filter, String search);

	UserView add(@Valid UserRegistrationForm form) throws UnsupportedEncodingException, MessagingException;

	LoginView login(LoginForm form, Errors errors) throws BadRequestException;

	long userCount();

	UserDetailView detailView();

	LoginView refresh(String refreshToken) throws BadRequestException;

	UserView currentUser(); // finds currently logged in user detail

	UserView update(UserDetailForm form);

	UserView changePassword(ChangePasswordForm form) throws NotFoundException;

	User uploadImage(ImageForm form);

	HttpEntity<byte[]> getImg();

	void forgotPassword(String token,String email);

	void resetPswd(String token, String password);

	void csvUser(HttpServletResponse httpServletResponse);

	void delete(Integer userId) throws NotFoundException;

	void deleteSelected(Collection<Integer> userIds);

}
