package com.project.employee.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.employee.entity.User;
import com.project.employee.form.UserDetailForm;
import com.project.employee.form.UserRegistrationForm;
import com.project.employee.service.UserService;
import com.project.employee.view.UserView;

@RestController
@RequestMapping("/user")
public class UsersController {

	@Autowired
	private UserService userService;

	
//	api for adding user
	@PostMapping
	public UserView add(@Valid @RequestBody UserRegistrationForm form) {
		return userService.add(form);
	}

//	api for listing all active users
	@GetMapping
	public Collection<User> list() {
		return userService.list();
	}

	
//	api for adding or updating  user details
	@PutMapping()
	public UserView updateUser(@Valid @RequestBody UserDetailForm form) {
		return userService.update(form);
	}

	
//api for logically deleting a user
	@PutMapping("/delete/{userId}")
	public void delete(@PathVariable("userId") Integer userId) {
		userService.delete(userId);
	}
}
