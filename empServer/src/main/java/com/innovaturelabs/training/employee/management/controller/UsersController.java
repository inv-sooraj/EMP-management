
package com.innovaturelabs.training.employee.management.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.innovaturelabs.training.employee.management.form.AdminAddUserForm;
import com.innovaturelabs.training.employee.management.form.ChangePasswordForm;
import com.innovaturelabs.training.employee.management.form.UserDetailForm;
import com.innovaturelabs.training.employee.management.form.UserEditForm;
import com.innovaturelabs.training.employee.management.form.UserForm;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.UserService;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.StatusView;
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

    @PutMapping("/verify-user")
    public UserView verifyUser(@RequestBody String token) {
        return userService.verifyUser(token);
    }

    @PostMapping("/admin-register")
    public UserView adminAdd(@Valid @RequestBody AdminAddUserForm form) {
        return userService.adminAdd(form);
    }

    @GetMapping()
    public UserDetailView currentUser() {
        return userService.currentUser();
    }

    @GetMapping("/{userId}")
    public UserDetailView getUser(@PathVariable("userId") Integer userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/page")
    public Pager<UserView> list(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
            @RequestParam(name = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(name = "sortBy", defaultValue = "user_id") String sortBy,
            @RequestParam(name = "status", defaultValue = "3") Byte status,
            @RequestParam(name = "role", defaultValue = "3") Byte role,
            @RequestParam(name = "search", defaultValue = "") String search) {

        page = page <= 0 ? 1 : page;
        return userService.list(page, limit, sortBy, search, status, desc, role);
    }

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

    @PutMapping("/user-update/{userId}")
    public UserView updateUser(@PathVariable(value = "userId") Integer useId,
            @RequestBody UserEditForm form) {
        return userService.updateUser(form, useId);
    }

    @PutMapping("/delete/{UserId}")
    public UserView delete(@PathVariable("UserId") Integer userId) {
        return userService.delete(userId);
    }

    @PutMapping("/delete")
    public void delete() {
        userService.delete(SecurityUtil.getCurrentUserId());
    }

    @PutMapping("/delete/selected")
    public Collection<UserView> deleteSelected(@RequestBody Collection<Integer> userIds) {
        return userService.deleteSelected(userIds);
    }

    @GetMapping("/download")
    public void userCsv(HttpServletResponse httpServletResponse,
            @RequestParam(name = "status") Collection<Byte> status,
            @RequestParam(name = "roles") Collection<Byte> roles,
            @RequestParam(name = "startDate") Date startDate,
            @RequestParam(name = "endDate") Date endDate) {

        userService.userCsv(httpServletResponse, status, roles, startDate, endDate);
    }

    @PutMapping("/profile")
    public void setProfile(@RequestParam("profilePic") MultipartFile profilePic)
            throws IOException {

        userService.setProfilePic(profilePic);
    }

    @GetMapping("/profile")
    public HttpEntity<byte[]> getProfilePic(@RequestParam(name = "userId", defaultValue = "0") Integer userId) {
        return userService.getProfilePic(userId);

    }

    @PutMapping("/change-password")
    public UserView changePassword(@RequestBody ChangePasswordForm form) {
        return userService.changePassword(form);
    }

    @GetMapping("/role-stat")
    public Collection<StatusView> count() {
        return userService.getRoleStat();
    }

    @GetMapping("/chart")
    public Map<String, Integer> chart(
            @RequestParam(name = "days") Integer days) {
        return userService.getUserCount(days);
    }

    @PutMapping("/profile/deleted/{userId}")
    public UserDetailView deleteProfilePic(@PathVariable Integer userId) {
        return userService.deleteProfilePic(userId);
    }

    // @GetMapping("/")
    // public String helloworld(){
    // return "Hello World";
    // }
    // @GetMapping("/restricted")
    // public String restricted(){
    // return "welcome ";
    // }
}
