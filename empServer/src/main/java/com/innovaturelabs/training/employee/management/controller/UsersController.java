
package com.innovaturelabs.training.employee.management.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.UserDetailForm;
import com.innovaturelabs.training.employee.management.form.UserForm;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.UserService;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.UserDetailView;
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

    @GetMapping("/page")
    public Pager<UserView> list(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "user_id") String sortBy,
            @RequestParam(name = "search", defaultValue = "") String search) {

        return userService.list(page, limit, sortBy, search);
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

    @PutMapping("/details")
    public UserDetailView updateUserDetails(
            @RequestBody UserDetailForm form) {
        return userService.updateUserDetails(form);
    }

    // @PutMapping("/{UserId}")
    // public UserView update(
    // @PathVariable("UserId") Integer userId,
    // @Valid @RequestBody UserForm form) {
    // return userService.update(userId, form);
    // }

    @PutMapping("/delete/{UserId}")
    public void delete(@PathVariable("UserId") Integer userId) {
        userService.delete(userId);
    }

    @PutMapping("/delete/selected")
    public void deleteSelected(@RequestBody Collection<Integer> userIds) {
        userService.deleteSelected(userIds);
    }

    @GetMapping("/download")
    public void userCsv(HttpServletResponse httpServletResponse) {
        userService.userCsv(httpServletResponse);
    }
}
