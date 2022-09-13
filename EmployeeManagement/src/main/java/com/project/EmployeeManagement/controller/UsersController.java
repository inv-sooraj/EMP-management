package com.project.EmployeeManagement.controller;

import java.util.Collection;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.form.UserDetailForm;
import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.service.UserService;
import com.project.EmployeeManagement.view.UserView;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserView add(@Valid @RequestBody UserForm form) {
        return userService.add(form);
    }

    @GetMapping
    public Collection<User> list() {
        return userService.list();
    }

    @PutMapping
    public UserView edit(@Valid @RequestBody UserDetailForm form) {
        return userService.edit(SecurityUtil.getCurrentUserId(), form);
    }

}
