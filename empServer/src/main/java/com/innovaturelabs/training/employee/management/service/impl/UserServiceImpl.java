
package com.innovaturelabs.training.employee.management.service.impl;

import static com.innovaturelabs.training.employee.management.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.form.UserDetailForm;
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
import com.innovaturelabs.training.employee.management.util.CsvDownload;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.LoginView;
import com.innovaturelabs.training.employee.management.view.UserDetailView;
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

        Byte role = User.Role.EMPLOYEE.value;

        if (form.getRole() == User.Role.EMPLOYER.value) {
            role = User.Role.EMPLOYER.value;
        }

        if (SecurityUtil.getCurrentUserRole() != null && SecurityUtil.getCurrentUserRole().equals("ADMIN")
                && form.getRole() == User.Role.ADMIN.value) {
            role = User.Role.ADMIN.value;
        }

        return new UserView(userRepository.save(new User(
                form.getName(),
                form.getUserName(),
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()),
                role)));
    }

    @Override
    public UserView currentUser() {
        return new UserView(
                userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                        .orElseThrow(NotFoundException::new));
    }

    @Override
    public LoginView login(LoginForm form, Errors errors) throws BadRequestException {

        if (errors.hasErrors()) {
            throw badRequestException();
        }

        User user = userRepository.findByUserName(form.getUserName()).orElseThrow(UserServiceImpl::invalidUsername);

        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException("User Inactive");
        }

        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Password");
        }

        String id = String.format("%010d", user.getUserId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id + String.valueOf(user.getRole()),
                securityConfig.getAccessTokenExpiry());

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
    public Pager<UserView> list(Integer page, Integer limit, String sortBy, String search) {

        if (!userRepository.findColumns().contains(sortBy)) {
            sortBy = "user_id";
        }

        if (page <= 0) {
            page = 1;
        }

        Page<User> users = userRepository.findAllByStatus(User.Status.ACTIVE.value, search,
                PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));

        Pager<UserView> userViews = new Pager<>(limit, (int) users.getTotalElements(), page);

        // Pager<JobView> userViews = new
        // Pager<JobView>(limit,jobRepository.countJobList(Job.Status.PENDING.value,
        // search).intValue(),page);

        userViews.setResult(users.getContent().stream().map(UserView::new).collect(Collectors.toList()));

        return userViews;
    }

    @Override
    public UserView updateUserName(Integer userId, String name) {

        User user = userRepository.findByUserIdAndStatus(userId, User.Status.ACTIVE.value)
                .orElseThrow(UserServiceImpl::badRequestException);

        if (user.getUserName().equals(name)) {
            throw new BadRequestException("Same Username");
        }

        if (userRepository.findByUserName(name).isPresent()) {
            throw new BadRequestException("Username Already Exists");
        }

        user.setUserName(name);

        return new UserView(userRepository.save(user));

    }

    @Override
    public void delete(Integer userId) {

        User user = userRepository.findByUserIdAndStatus(userId, User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new);
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(new Date());
        userRepository.save(user);

    }

    @Override
    public void deleteSelected(Collection<Integer> userIds) {

        for (Integer userId : userIds) {

            Optional<User> user = userRepository.findByUserIdAndStatus(userId, User.Status.ACTIVE.value);

            if (user.isPresent()) {
                userRepository.save(user.get().delete());
            }

        }

    }

    @Override
    public void userCsv(HttpServletResponse httpServletResponse) {

        if (!SecurityUtil.getCurrentUserRole().equals("ADMIN")) {
            throw new BadRequestException("permission Denied");
        }

        Collection<User> exportlist = userRepository.findAll();

        CsvDownload.download(httpServletResponse, exportlist, "Users");

    }

    @Override
    public UserDetailView updateUserDetails(UserDetailForm form) {
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new);

        user.updateDetails(form);

        return new UserDetailView(userRepository.save(user));
    }

}
