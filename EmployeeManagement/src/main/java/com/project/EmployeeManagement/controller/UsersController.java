package com.project.EmployeeManagement.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.form.ChangePasswordForm;
import com.project.EmployeeManagement.form.UserAddForm;
import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.form.userProfilePictureForm;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.service.UserService;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.view.UserView;

// @CrossOrigin("http://localhost:4200")

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    // user registration
    @PostMapping
    public UserView add(@Valid @RequestBody UserForm form) {
        return userService.add(form);
    }

    // Add new user
    @PostMapping("/details")
    public UserView addUser(@Valid @RequestBody UserForm form) {
        return userService.addUser(form);
    }

    @GetMapping
    public Collection<User> list() {
        return userService.list();
    }

    @GetMapping("/{userId}")
    public UserView get(@PathVariable("userId") Integer userId) {
        return userService.get(userId);
    }

    @PutMapping
    public UserView edit(@Valid @RequestBody UserAddForm form) {
        return userService.edit(SecurityUtil.getCurrentUserId(), form);
    }

    @PutMapping("/{userId}")
    public UserView edit(
            @PathVariable("userId") Integer userId,
            @Valid @RequestBody UserAddForm form) {
        return userService.edit(userId, form);
    }

    @PutMapping("/delete/{userId}")
    public void delete(@PathVariable("userId") Integer userId) {
        userService.delete(userId);
    }

    // ..................................pagination.................................

    @GetMapping("/list")
    public Pager<UserView> list(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "limit", required = false, defaultValue = "16") String limit,
            @RequestParam(value = "sort", required = false, defaultValue = "update_date") String sort) {

        return userService.listItem(search, limit, sort, page);
    }

    // ................................CSV_Download..................................

    @GetMapping("/download")
    public void jobCsv(HttpServletResponse httpServletResponse) {
        userService.jobCsv(httpServletResponse);
    }

    @PutMapping("/changepassword")
    public UserView change(@Valid @RequestBody ChangePasswordForm form) {
        return userService.change(form);
    }

    @PutMapping("/deleteall")
    public void deleteAll(@RequestBody Collection<Integer> Ids) {

        userService.deleteAll(Ids);
    }

    // profilepicture upload
    @PostMapping("/uploadimage")
    public UserView addUserDetails(@ModelAttribute userProfilePictureForm form) throws Exception {

        return userService.addUserDetails(form);

    }

    @GetMapping("/viewProfile")
    public HttpEntity<byte[]> downloadFile() {

        byte[] file = userService.getFileData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(file.length);

        return new HttpEntity<>(file, headers);

    }
}
