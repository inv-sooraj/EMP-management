package com.innovaturelabs.training.employee.management.service;

import com.innovaturelabs.training.employee.management.form.OAuth2RegisterForm;
import com.innovaturelabs.training.employee.management.view.LoginView;

public interface OAuth2Service {

    LoginView addUser(OAuth2RegisterForm form);

    LoginView login(String idToken);

    String checkEmail(String email);

}
