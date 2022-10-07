
package com.innovaturelabs.training.employee.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.service.UserService;
import com.innovaturelabs.training.employee.management.view.LoginView;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    // @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public LoginView login(@Valid @RequestBody LoginForm form, Errors errors) {
        return userService.login(form, errors);
    }

    @PutMapping
    public LoginView refresh(@RequestBody String refreshToken) {
        return userService.refresh(refreshToken);
    }

    @PutMapping("/forgot-password")
    public void forgotPassword(@RequestBody String email) {
        userService.forgotPassword(email);
    }

    @PutMapping("/reset-password/{token}")
    public void resetPassword(@PathVariable("token") String token, @RequestBody String password) {
        userService.resetPassword(token, password);
    }

}
