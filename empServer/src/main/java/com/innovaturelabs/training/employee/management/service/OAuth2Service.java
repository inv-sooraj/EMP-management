package com.innovaturelabs.training.employee.management.service;

import com.innovaturelabs.training.employee.management.form.OAuth2UserForm;
import com.innovaturelabs.training.employee.management.view.LoginView;

public interface OAuth2Service {

    LoginView verifyUser(OAuth2UserForm form);
    
}
