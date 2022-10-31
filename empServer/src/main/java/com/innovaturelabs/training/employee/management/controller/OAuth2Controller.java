
package com.innovaturelabs.training.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/register/{role}")
    public LoginView register(@RequestBody String idToken, @PathVariable("role") Byte role) {
        return oAuth2Service.addUser(idToken, role);
    }

    @GetMapping(value = "/verify-email")
    public String verifyEMail(@RequestParam("email") String email) {

        return oAuth2Service.checkEmail(email);
    }

}
