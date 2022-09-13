package com.project.EmployeeManagement.controller;
import com.project.EmployeeManagement.form.LoginForm;
import com.project.EmployeeManagement.service.UserService;
import com.project.EmployeeManagement.view.LoginView;
import com.project.EmployeeManagement.view.UserView;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public UserView currentUser() {
        return userService.currentUser();
    }

    @PostMapping
    public LoginView login(@Valid @RequestBody LoginForm form, Errors errors) {
        return userService.login(form, errors);
    }

    @PutMapping
    public LoginView refresh(@RequestBody String refreshToken) {
        return userService.refresh(refreshToken);
    }
}
