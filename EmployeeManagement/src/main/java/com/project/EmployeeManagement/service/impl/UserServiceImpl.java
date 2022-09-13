package com.project.EmployeeManagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.LoginForm;
import com.project.EmployeeManagement.form.UserDetailForm;
import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.repository.UserRepository;
import com.project.EmployeeManagement.security.config.SecurityConfig;
import com.project.EmployeeManagement.security.util.InvalidTokenException;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.security.util.TokenExpiredException;
import com.project.EmployeeManagement.security.util.TokenGenerator;
import com.project.EmployeeManagement.security.util.TokenGenerator.Token;
import com.project.EmployeeManagement.service.UserService;
import com.project.EmployeeManagement.view.LoginView;
import com.project.EmployeeManagement.view.UserView;
import static com.project.EmployeeManagement.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;
import java.util.Collection;
import java.util.Date;
import com.project.EmployeeManagement.security.util.TokenGenerator.Status;

@Service
public class UserServiceImpl implements UserService {

    private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public UserView add(UserForm form) {

        if (userRepository.findByUserName(form.getUserName()).isPresent()) {
            throw new BadRequestException("Already Exists");
        }

        return new UserView(userRepository.save(new User(
                form.getUserName(),
                form.getName(),
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()))));
    }

    @Override
    public UserView currentUser() {
        return new UserView(
                userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(NotFoundException::new));
    }

    @Override
    @Transactional
    public UserView edit(Integer userId, UserDetailForm form) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        user.setAddress(form.getAddress());
        user.setPhone(form.getPhone());

        user.setUpdateDate(new Date());
        userRepository.save(user);

        return new UserView(user);

    }

    private static BadRequestException badRequestException() {
        return new BadRequestException("Invalid credentials");
    }

    @Override
    public LoginView login(LoginForm form, Errors errors) throws BadRequestException {
        if (errors.hasErrors()) {
            throw badRequestException();
        }

        User user = userRepository.findByUserName(form.getUserName()).orElseThrow(UserServiceImpl::badRequestException);
        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new BadRequestException("failed");
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

        String id = String.format("%010d", user.getUserId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, securityConfig.getAccessTokenExpiry());
        return new LoginView(
                user,
                new LoginView.TokenView(accessToken.value, accessToken.expiry),
                new LoginView.TokenView(refreshToken, status.expiry));
    }

    @Override
    public Collection<User> list() {
        return userRepository.findAll();
    }

}
