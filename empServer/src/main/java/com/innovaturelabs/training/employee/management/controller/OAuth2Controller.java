
package com.innovaturelabs.training.employee.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.OAuth2RegisterForm;
import com.innovaturelabs.training.employee.management.service.OAuth2Service;
import com.innovaturelabs.training.employee.management.view.LoginView;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private OAuth2Service oAuth2Service;

    @PostMapping("/login")
    public LoginView login(@RequestBody String idToken) {
        return oAuth2Service.login(idToken);
    }

    @PostMapping("/register")
    public LoginView register(@Valid @RequestBody OAuth2RegisterForm form ) {
        return oAuth2Service.addUser(form);
    }

    @GetMapping(value = "/verify-email")
    public String verifyEMail(@RequestParam("email") String email) {

        return oAuth2Service.checkEmail(email);
    }

}
