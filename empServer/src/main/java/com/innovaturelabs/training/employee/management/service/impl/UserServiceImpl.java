
package com.innovaturelabs.training.employee.management.service.impl;

import static com.innovaturelabs.training.employee.management.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.form.UserForm;
import com.innovaturelabs.training.employee.management.repository.UserRepository;
import com.innovaturelabs.training.employee.management.security.config.SecurityConfig;
import com.innovaturelabs.training.employee.management.security.util.InvalidTokenException;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.security.util.TokenExpiredException;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator.Status;
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator.Token;
import com.innovaturelabs.training.employee.management.service.UserService;
import com.innovaturelabs.training.employee.management.view.LoginView;
import com.innovaturelabs.training.employee.management.view.UserView;

@Service
public class UserServiceImpl implements UserService {

    private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public UserView add(UserForm form) {

        if (userRepository.findByUserName(form.getUserName()).isPresent()) {
            throw new BadRequestException("Username Already Exists");
        }

        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new BadRequestException("Email Already Exists");
        }

        return new UserView(userRepository.save(new User(
                form.getName(),
                form.getUserName(),
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()))));
    }

    @Override
    public UserView currentUser() {
        return new UserView(
                userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(NotFoundException::new));
    }

    @Override
    public LoginView login(LoginForm form, Errors errors) throws BadRequestException {
        if (errors.hasErrors()) {
            throw badRequestException();

        }

        // System.err.println();
        User user = userRepository.findByUserName(form.getUserName()).orElseThrow(UserServiceImpl::invalidUsername);

        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw badRequestException();
        }

        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw badRequestException();
        }

        String id = String.format("%010d", user.getUserId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, securityConfig.getAccessTokenExpiry());
        Token refreshToken = tokenGenerator.create(PURPOSE_REFRESH_TOKEN, id + user.getPassword(),
                securityConfig.getRefreshTokenExpiry());
        return new LoginView(user, accessToken, refreshToken);
    }

    @Override
    public LoginView refresh(String refreshToken) throws BadRequestException {

        Status status;
        try {
            status = tokenGenerator.verify(PURPOSE_REFRESH_TOKEN, refreshToken);
        } catch (InvalidTokenException e) {
            throw new BadRequestException("Invalid token", e);
        } catch (TokenExpiredException e) {
            throw new BadRequestException("Token expired", e);
        }

        int userId;
        try {
            userId = Integer.parseInt(status.data.substring(0, 10));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid token", e);
        }

        String password = status.data.substring(10);

        User user = userRepository.findByUserIdAndPassword(userId, password)
                .orElseThrow(UserServiceImpl::badRequestException);

        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException("Invalid User");
        }

        // if(!logoutRepository.findAllByUserUserId(user.getUserId()).isEmpty()){
        // throw new BadRequestException("Invalid Credentials");
        // }

        String id = String.format("%010d", user.getUserId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, securityConfig.getAccessTokenExpiry());
        return new LoginView(
                user,
                new LoginView.TokenView(accessToken.value, accessToken.expiry),
                new LoginView.TokenView(refreshToken, status.expiry));
    }

    private static BadRequestException badRequestException() {
        return new BadRequestException("Invalid credentials");
    }

    private static BadRequestException invalidUsername() {
        return new BadRequestException("Invalid Username");
    }

    @Override
    public Page<UserView> list(Integer page) {
        // return userRepository.findAll();
        return userRepository.findAllByStatus(User.Status.ACTIVE.value, PageRequest.of(page, 1));

    }

    @Override
    public UserView updateUserName(Integer userId, String name) {

        User user = userRepository.findById(userId).orElseThrow(UserServiceImpl::badRequestException);

        if (user.getUserName().equals(name)) {
            throw new BadRequestException("Same Username");
        }

        if (userRepository.findByUserName(name).isPresent()) {
            throw new BadRequestException("Username Already Exists");
        }

        user.setUserName(name);

        return new UserView(userRepository.save(user));

    }

}
