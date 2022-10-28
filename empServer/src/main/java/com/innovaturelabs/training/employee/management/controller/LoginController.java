
package com.innovaturelabs.training.employee.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.form.validaton.Password;
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
    public void resetPassword(@PathVariable("token") String token, @RequestBody @Password String password) {
        userService.resetPassword(token, password);
    }

    @GetMapping
    public String hi(@AuthenticationPrincipal OAuth2User principal) {
        return "hi" + principal;
    }

    // /oauth2/code/google

    // http://localhost:8080/login/oauth2/code/google?
    // state=fQrO5R9X9A7u1GLyQYcLTKo8wOPfw0d9lFrFRcPQlCk%3D
    // &code=4%2F0ARtbsJo_Fm11sL_0vpAXxcrI3LksUmScoschV-tUYh2e3YI_eydyXkIv6jqzlnbxhZ24yg
    // &scope=email+profile+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email
    // &authuser=0
    // &prompt=consent

    // @GetMapping("/oauth2/code/google")
    // public String his(@AuthenticationPrincipal OAuth2User principal) {
    // return "hi" + principal;
    // }

}
