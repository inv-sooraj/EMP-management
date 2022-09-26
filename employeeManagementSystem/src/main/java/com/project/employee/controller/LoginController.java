package com.project.employee.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.employee.form.LoginForm;
import com.project.employee.service.UserService;
import com.project.employee.view.LoginView;
import com.project.employee.view.UserView;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;

//    api for fetching currently logged in user
	@GetMapping
	public UserView currentUser() {
		return userService.currentUser();
	}

//    api for login
	@PostMapping
	public LoginView login(@Valid @RequestBody LoginForm form, Errors errors) {
		return userService.login(form, errors);
	}

	@PutMapping
	public LoginView refresh(@RequestBody String refreshToken) {
		return userService.refresh(refreshToken);
	}
}
