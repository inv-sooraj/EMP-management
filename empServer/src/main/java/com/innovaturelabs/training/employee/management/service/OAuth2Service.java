package com.innovaturelabs.training.employee.management.service;

import com.innovaturelabs.training.employee.management.view.LoginView;

public interface OAuth2Service {

    LoginView addUser(String idToken, Byte role);

    LoginView login(String idToken);

    String checkEmail(String email);

}
