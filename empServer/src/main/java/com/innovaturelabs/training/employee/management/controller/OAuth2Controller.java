
package com.innovaturelabs.training.employee.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.OAuth2UserForm;
import com.innovaturelabs.training.employee.management.service.OAuth2Service;
import com.innovaturelabs.training.employee.management.view.LoginView;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private OAuth2Service oAuth2Service;

    @PostMapping("/user")
    public LoginView verifyUser(@Valid @RequestBody OAuth2UserForm form) {
        return oAuth2Service.verifyUser(form);
    }

    // @GetMapping("/user")
    // public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {

    //     Map<String, Object> data = new HashMap<>();

    //     data.put("Name", principal.getAttribute("name"));
    //     data.put("Email", principal.getAttribute("email"));

    //     System.out.println("Priiincipal" + principal);

    //     return data;

    //     // return Collections.singletonMap("name", principal.getAttribute("name"));
    // }

}
