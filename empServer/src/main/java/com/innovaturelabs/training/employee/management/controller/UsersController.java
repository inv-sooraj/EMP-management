/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.training.employee.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.UserForm;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.UserService;
import com.innovaturelabs.training.employee.management.view.UserView;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserView add(@Valid @RequestBody UserForm form) {
        return userService.add(form);
    }

    @GetMapping()
    public UserView currentUser() {
        return userService.currentUser();
    }

    @GetMapping("/page/{page}")
    public Page<UserView> list(@PathVariable("page") Integer page) {
        return userService.list(page);
    }

    // @PutMapping()
    // public UserView update(
    // @Valid @RequestBody UserForm form) {
    // return userService.update(SecurityUtil.getCurrentUserId(), form);
    // }

    @PutMapping("/username")
    public UserView updateUserName(
            @RequestBody String name) {
        return userService.updateUserName(SecurityUtil.getCurrentUserId(), name);
    }

    // @PutMapping("/{UserId}")
    // public UserView update(
    // @PathVariable("UserId") Integer userId,
    // @Valid @RequestBody UserForm form) {
    // return userService.update(userId, form);
    // }

    // @DeleteMapping("/{UserId}")
    // public void delete(@PathVariable("UserId") Integer userId) {
    // userService.delete(userId);
    // }
}
