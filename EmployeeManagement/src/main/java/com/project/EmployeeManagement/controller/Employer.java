package com.project.EmployeeManagement.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.service.EmployerService;
import com.project.EmployeeManagement.service.UserService;
import com.project.EmployeeManagement.view.UserView;

@RestController
@RequestMapping("/employer")
public class Employer {

    @Autowired
    private EmployerService employerService;

    @Autowired
    private UserService userService;


    @PostMapping
    public UserView add(@Valid @RequestBody UserForm form,Errors errors ){
        return employerService.addEmployer(form);

    }

    @GetMapping
    public Collection<User> list() {
        return employerService.list();
    }

    @PutMapping("/delete/{userId}")
    public void delete(@PathVariable("userId")Integer userId) {
        employerService.delete(userId);
    }

    // @PutMapping
    // public UserView edit(@Valid @RequestBody UserDetailForm form) {
    //     return userService.edit(SecurityUtil.getCurrentUserId(), form);
    // }
    
}
