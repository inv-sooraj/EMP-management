package com.project.EmployeeManagement.service.impl;

import static com.project.EmployeeManagement.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

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
import com.project.EmployeeManagement.security.util.TokenGenerator.Status;
import com.project.EmployeeManagement.security.util.TokenGenerator.Token;
import com.project.EmployeeManagement.service.UserService;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.view.LoginView;
import com.project.EmployeeManagement.view.UserView;

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
    public UserView get(Integer userId) {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole.equals(User.Role.ADMIN.value)) {
            return userRepository.findById(userId)
                    .map((user) -> {
                        return new UserView(user);
                    }).orElseThrow(NotFoundException::new);
        } else
            throw new BadRequestException("Illegal Access");

    }

    @Override
    @Transactional
    public UserView edit(Integer userId, UserDetailForm form) throws NotFoundException {

        if (!userId.equals(SecurityUtil.getCurrentUserId())
                && userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(NotFoundException::new)
                        .getRole() != User.Role.ADMIN.value) {
            throw new BadRequestException("Permission Denied");
        }

        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        user.setUserName(form.getUserName());
        user.setName(form.getName());
        user.setEmail(form.getEmail());
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

        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();
        if (userRole.equals(User.Role.ADMIN.value)) {
            return userRepository.findByStatus(User.Status.ACTIVE.value);
        } else
            throw new BadRequestException("illegal Access");
    }

    // ###################################pagination################################################
    @Override
    public Pager<UserView> listItem(String search, String limit, String sort, String page) {

        Page<User> users = userRepository.find(User.Status.ACTIVE.value, search,
                PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(sort).ascending()));

        Pager<UserView> userViews = new Pager<UserView>(Integer.parseInt(limit),
                (int) users.getTotalElements(),
                Integer.parseInt(page));

        userViews.setResult(users.getContent().stream().map(user -> new UserView(user)).collect(Collectors.toList()));

        return userViews;
    }


    // ################################CSVDownload########################################
    @Override
    @Transactional
    public void jobCsv(HttpServletResponse httpServletResponse) {
        Collection<UserView> exportlist = userRepository.findAll().stream().map(job -> new UserView(job))
                .collect(Collectors.toList());
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=emailList" + sdf.format(dt) + ".csv";
        httpServletResponse.setHeader(headerKey, headerValue);
        httpServletResponse.setContentType("text/csv;");
        httpServletResponse.setCharacterEncoding("shift-jis");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        try {

            ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] csvHeader = { "UserId", "UserName", "Status", "Email", "Name", "Address", "Phone", "Role",
                    "Qualification",
                    "Create Date", "Update Date" };
            String[] nameMapping = { "userId", "userName", "status", "email", "name", "address", "phone", "role",
                    "qualification", "createDate", "updateDate" };

            csvWriter.writeHeader(csvHeader);
            for (UserView reservation : exportlist) {
                csvWriter.write(reservation, nameMapping);
            }

            csvWriter.close();
        } catch (IOException e) {
            throw new BadRequestException("Exception while exporting csv");
        }

    }
    // ....................................................................................................

    @Override
    public void delete(Integer userId) {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole.equals(User.Role.ADMIN.value)) {
            User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

            user.setStatus(User.Status.DELETED.value);

            user.setUpdateDate(new Date());

            userRepository.save(user);
        } else
            throw new BadRequestException("illegal access");
    }

}
