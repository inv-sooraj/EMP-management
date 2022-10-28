package com.innovaturelabs.training.employee.management.service.impl;

import static com.innovaturelabs.training.employee.management.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.form.OAuth2UserForm;
import com.innovaturelabs.training.employee.management.repository.UserRepository;
import com.innovaturelabs.training.employee.management.security.config.SecurityConfig;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator.Token;
import com.innovaturelabs.training.employee.management.service.OAuth2Service;
import com.innovaturelabs.training.employee.management.view.LoginView;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public LoginView verifyUser(OAuth2UserForm form) {

        userRepository.findByEmailAndUserType(form.getEmail(), User.UserType.NATIVE.value).ifPresent(user -> {
            throw new BadRequestException(user.getEmail() + " : Already Registered");
        }); 

        User user = userRepository.findByEmailAndUserType(form.getEmail(), User.UserType.GOOGLE.value)
                .orElse(new User(form.getName(), form.getEmail(), User.Role.EMPLOYEE.value,
                        User.UserType.GOOGLE.value));

        String id = String.format("%010d", userRepository.save(user).getUserId());

        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id + String.valueOf(user.getRole()),
                securityConfig.getAccessTokenExpiry());

        Token refreshToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id + user.getEmail(),
                securityConfig.getRefreshTokenExpiry());

        return new LoginView(user, accessToken, refreshToken);

    }

}
