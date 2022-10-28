package com.innovaturelabs.training.employee.management.service.impl;

import static com.innovaturelabs.training.employee.management.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.repository.UserRepository;
import com.innovaturelabs.training.employee.management.security.config.SecurityConfig;
import com.innovaturelabs.training.employee.management.security.util.GoogleAuthenticator;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator.Token;
import com.innovaturelabs.training.employee.management.service.OAuth2Service;
import com.innovaturelabs.training.employee.management.view.LoginView;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private GoogleAuthenticator googleAuthenticator;

    @Override
    public LoginView verifyUser(String idToken) {

        JSONObject idTokenData;
        try {
            idTokenData = googleAuthenticator.googleFilter(idToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Invalid Request");
        }

        String name = "";
        String email = "";
        // String picture = "";
        if (idTokenData.has("email"))
            email = idTokenData.get("email").toString();
        if (idTokenData.has("name"))
            name = idTokenData.get("name").toString();
        // if (idTokenData.has("picture"))
        // picture = idTokenData.get("picture").toString();

        userRepository.findByEmailAndUserType(email, User.UserType.NATIVE.value).ifPresent(user -> {
            throw new BadRequestException(user.getEmail() + " : Already Registered");
        });

        User user = userRepository.findByEmailAndUserType(email, User.UserType.GOOGLE.value)
                .orElse(new User(name, email, User.Role.EMPLOYEE.value,
                        User.UserType.GOOGLE.value));

        String id = String.format("%010d", userRepository.save(user).getUserId());

        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id + String.valueOf(user.getRole()),
                securityConfig.getAccessTokenExpiry());

        Token refreshToken = tokenGenerator.create(PURPOSE_REFRESH_TOKEN, id + user.getUserType(),
                securityConfig.getRefreshTokenExpiry());

        return new LoginView(user, accessToken, refreshToken);

    }

}
