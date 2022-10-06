package com.project.employee.controller;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.employee.entity.User;
import com.project.employee.features.Pager;
import com.project.employee.form.ChangePasswordForm;
import com.project.employee.form.ForgotPasswordForm;
import com.project.employee.form.ImageForm;
import com.project.employee.form.ResetPasswordForm;
import com.project.employee.form.UserDetailForm;
import com.project.employee.form.UserRegistrationForm;
import com.project.employee.service.UserService;
import com.project.employee.view.UserDetailView;
import com.project.employee.view.UserView;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService userService;

//	api for adding user
	@PostMapping
	public UserView add(@Valid @RequestBody UserRegistrationForm form) throws UnsupportedEncodingException, MessagingException {
		return userService.add(form);
	}
	
	@PostMapping("/admin")
	public UserView adminAdd(@Valid @RequestBody UserRegistrationForm form) throws UnsupportedEncodingException, MessagingException {
		return userService.add(form);
	}

//	api for listing all active users
	@GetMapping
	public Pager<UserView> list(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = false, defaultValue = "job_id") String sortBy,
			@RequestParam(name = "search", required = false, defaultValue = "") String search) {
		return userService.list(page, limit, sortBy, search);
	}

	@GetMapping("/detail")
	public UserDetailView detailView() {
		return userService.detailView();
	}

	@GetMapping("/download")
	public void csvUser(HttpServletResponse httpServletResponse) {
		userService.csvUser(httpServletResponse);
	}

	@GetMapping("/count")
	public long userCount() {
		return userService.userCount();
	}
	
	//to find user with email
		@PostMapping("/resetPswdEmail")
		public void validateEmail(@Valid @RequestBody ForgotPasswordForm form) {
			String token= RandomString.make(45);
			 userService.forgotPassword(token,form);
		}
		
//		// to reset password
		@PostMapping("/forgotPswd/{token}")
		public void resetPassword(@PathVariable("token") String token, @Valid @RequestBody ResetPasswordForm form) {
			 userService.resetPswd(token,form.getPassword());
		}

//	api for adding or updating  user details
	@PutMapping()
	public UserView updateUser(@Valid @RequestBody UserDetailForm form) {
		return userService.update(form);
	}
	
	@PutMapping("/changepswd")
	public UserView changePassword(@Valid @RequestBody ChangePasswordForm form) {
		return userService.changePassword(form);
	}
	
	

	@PutMapping("/profilePic")
	public User add(@ModelAttribute ImageForm form) throws Exception {
		System.out.println(form.getProfilePhoto().getOriginalFilename());
		return userService.uploadImage(form);
	}

	@GetMapping("/getImg")
	public HttpEntity<byte[]> getImg() {
		return userService.getImg();
	}

//api for logically deleting a user
	@PutMapping("/delete")
	public void delete(@RequestBody Integer userId) {
		userService.delete(userId);
	}

	@PutMapping("/delete/selected")
	public void deleteSelected(@RequestBody Collection<Integer> jobIds) {
		userService.deleteSelected(jobIds);
	}
}
