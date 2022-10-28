package com.innovaturelabs.training.employee.management.service;

import com.innovaturelabs.training.employee.management.view.LoginView;

public interface OAuth2Service {

    LoginView verifyUser(String idToken);
    
}
