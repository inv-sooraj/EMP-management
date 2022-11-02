package com.innovaturelabs.training.employee.management.service.impl;

import static com.innovaturelabs.training.employee.management.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.OAuth2RegisterForm;
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
    public LoginView addUser(OAuth2RegisterForm form) {

        if (userRepository.findByUserName(form.getUserName()).isPresent()) {
            throw new BadRequestException("UserName Already Exists");
        }

        JSONObject idTokenData;
        try {
            idTokenData = googleAuthenticator.googleFilter(form.getIdToken());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Invalid Request");
        }

        String email = "";
        if (idTokenData.has("email"))
            email = idTokenData.get("email").toString();

        String name = "";
        if (idTokenData.has("name"))
            name = idTokenData.get("name").toString();

        String picture = "";
        if (idTokenData.has("picture"))
            picture = idTokenData.get("picture").toString();

        userRepository.findByEmail(email).ifPresent(user -> {
            throw new BadRequestException(user.getEmail() + " : Already Registered");
        });

        User user = new User(name, email,
                form.getRole().byteValue() == User.Role.EMPLOYER.value ? User.Role.EMPLOYER.value
                        : User.Role.EMPLOYEE.value,
                User.UserType.GOOGLE.value, form.getUserName(),
                picture);

        String id = String.format("%010d", userRepository.save(user).getUserId());

        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id + String.valueOf(user.getRole()),
                securityConfig.getAccessTokenExpiry());

        Token refreshToken = tokenGenerator.create(PURPOSE_REFRESH_TOKEN, id + user.getUserType(),
                securityConfig.getRefreshTokenExpiry());

        return new LoginView(user, accessToken, refreshToken);
    }

    @Override
    public LoginView login(String idToken) {

        JSONObject idTokenData;
        try {
            idTokenData = googleAuthenticator.googleFilter(idToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Invalid Request");
        }

        String email = "";
        if (idTokenData.has("email"))
            email = idTokenData.get("email").toString();

        String picture = "";
        if (idTokenData.has("picture"))
            picture = idTokenData.get("picture").toString();

        User user = userRepository.findByEmailAndUserType(email, User.UserType.GOOGLE.value)
                .orElseThrow(NotFoundException::new);

        if (!user.getProfilePic().equals(picture) && user.getProfilePic().contains("googleusercontent")) {
            user.setProfilePic(picture);
            userRepository.save(user);
        }

        if (user.getStatus() != User.Status.ACTIVE.value) {
            throw new BadRequestException("Inactive User");
        }

        String id = String.format("%010d", userRepository.save(user).getUserId());

        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id + String.valueOf(user.getRole()),
                securityConfig.getAccessTokenExpiry());

        Token refreshToken = tokenGenerator.create(PURPOSE_REFRESH_TOKEN, id + user.getUserType(),
                securityConfig.getRefreshTokenExpiry());

        return new LoginView(user, accessToken, refreshToken);

    }

    @Override
    public String checkEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {

            if (user.get().getUserType() == User.UserType.NATIVE.value) {
                return "NATIVE_USER";
            } else {
                return "GOOGLE_USER";
            }
        } else {
            return "NOT_PRESENT";
        }

    }

}
