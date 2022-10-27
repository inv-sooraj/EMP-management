
package com.innovaturelabs.training.employee.management.service.impl;

import static com.innovaturelabs.training.employee.management.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.AdminAddUserForm;
import com.innovaturelabs.training.employee.management.form.ChangePasswordForm;
import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.form.UserDetailForm;
import com.innovaturelabs.training.employee.management.form.UserEditForm;
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
import com.innovaturelabs.training.employee.management.util.CsvUtil;
import com.innovaturelabs.training.employee.management.util.EmailUtil;
import com.innovaturelabs.training.employee.management.util.FileUtil;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.ChartView;
import com.innovaturelabs.training.employee.management.view.LoginView;
import com.innovaturelabs.training.employee.management.view.StatusView;
import com.innovaturelabs.training.employee.management.view.UserDetailView;
import com.innovaturelabs.training.employee.management.view.UserView;

@Service
public class UserServiceImpl implements UserService {

    private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String PURPOSE_RESET_PASSWORD_TOKEN = "RESET_PASSWORD_TOKEN";
    private static final String PURPOSE_REGISTRATION = "REGISTRATION";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private EmailUtil emailUtil;

    private final Set<String> userFields = Arrays.stream(User.class.getDeclaredFields()).map(Field::getName)
            .collect(Collectors.toSet());

    @Override
    public UserView add(UserForm form) {

        if (userRepository.findByUserNameLike(form.getUserName()).isPresent()) {
            throw userAlreadyNameExists();
        }

        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new BadRequestException("Email Already Exists");
        }

        Byte role = User.Role.EMPLOYEE.value;

        if (form.getRole() == User.Role.EMPLOYER.value) {
            role = User.Role.EMPLOYER.value;
        }

        String data = form.getEmail() + "#" + new Date();

        Token token = tokenGenerator.create(PURPOSE_REGISTRATION, data, Duration.ofMinutes(15));

        emailUtil.userRegistrationConfirm(token, form.getEmail(), form.getName());

        return new UserView(userRepository.save(new User(
                form.getName(),
                form.getUserName(),
                data,
                passwordEncoder.encode(form.getPassword()),
                role)));
    }

    @Override
    public UserView adminAdd(AdminAddUserForm form) {

        if (!SecurityUtil.isAdmin()) {
            throw new BadRequestException("Illegal Access");
        }

        if (userRepository.findByUserName(form.getUserName()).isPresent()) {
            throw userAlreadyNameExists();
        }

        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new BadRequestException("Email Already Exists");
        }

        Byte role = User.Role.EMPLOYEE.value;

        if (form.getRole() == User.Role.EMPLOYER.value || form.getRole() == User.Role.ADMIN.value) {
            role = form.getRole();
        }

        // SecureRandom rand = new SecureRandom();

        // String password = "#" + rand.nextInt(1000)
        // + (char) (rand.nextInt(26) + 'a') + (char) (rand.nextInt(26) + 'A')
        // + RandomString.make(5);

        // System.out.println("Password : " + password);

        String data = form.getEmail() + "#" + new Date();

        Token token = tokenGenerator.create(PURPOSE_RESET_PASSWORD_TOKEN, data, Duration.ofDays(1));

        emailUtil.adminUserRegistrationConfirm(token, form.getEmail(), form.getName());

        // emailUtil.sendRegisterSuccess(form.getEmail(), form.getUserName(), password);

        return new UserView(userRepository.save(new User(
                form.getName(),
                form.getUserName(),
                data,
                null,
                role)));
    }

    @Override
    public UserView verifyUser(String token) {

        Status status;
        try {
            status = tokenGenerator.verify(PURPOSE_REGISTRATION, token);
        } catch (InvalidTokenException e) {
            throw invalidToken(e);
        } catch (TokenExpiredException e) {
            throw new BadRequestException("Registration Token Expired", e);
        }

        String email;
        try {
            email = status.data;
        } catch (NumberFormatException e) {
            throw invalidToken(e);
        }

        String parsedEmail = email.split("#")[0];

        if (userRepository.findByEmail(parsedEmail).isPresent()) {
            throw new BadRequestException("Already Verified");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundException::new);

        user.setEmail(parsedEmail);

        user.setStatus(User.Status.ACTIVE.value);
        user.setUpdateDate(new Date());
        userRepository.save(user);

        return null;

    }

    @Override
    public UserDetailView currentUser() {
        return new UserDetailView(
                userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                        .orElseThrow(NotFoundException::new));
    }

    @Override
    public LoginView login(LoginForm form, Errors errors) throws BadRequestException {

        if (errors.hasErrors()) {
            throw badRequestException();
        }

        User user = userRepository.findByUserName(form.getUserName())
                .orElseThrow(() -> new BadRequestException("Invalid Username"));

        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException("User Inactive");
        } else if (user.getStatus() == User.Status.REJECTED.value) {
            throw new BadRequestException("User Rejected");
        }

        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Password");
        }

        String id = formatUserId(user.getUserId());
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
            throw invalidToken(e);
        } catch (TokenExpiredException e) {
            throw new BadRequestException("Token expired", e);
        }

        int userId;
        try {
            userId = Integer.parseInt(status.data.substring(0, 10));
        } catch (NumberFormatException e) {
            throw invalidToken(e);
        }

        String password = status.data.substring(10);

        User user = userRepository.findByUserIdAndPassword(userId, password)
                .orElseThrow(UserServiceImpl::badRequestException);

        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException("Inactive User");
        } else if (user.getStatus() == User.Status.REJECTED.value) {
            throw new BadRequestException("Rejected User");
        }

        String id = formatUserId(user.getUserId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id + String.valueOf(user.getRole()),
                securityConfig.getAccessTokenExpiry());
        return new LoginView(
                user,
                new LoginView.TokenView(accessToken.value, accessToken.expiry),
                new LoginView.TokenView(refreshToken, status.expiry));
    }

    @Override
    public Pager<UserView> list(Integer page, Integer limit, String sortBy, String search, Byte status, Boolean desc,
            Byte role) {

        if (!SecurityUtil.isAdmin()) {
            throw new BadRequestException("Illegal Access");
        }

        // if (!userRepository.findColumns().contains(sortBy)) {
        // sortBy = "user_id";
        // }

        sortBy = userFields.contains(sortBy) ? sortBy : "userId";

        Collection<Byte> statuses = new ArrayList<>();

        if (status.equals(User.Status.INACTIVE.value) || status.equals(User.Status.ACTIVE.value)
                || status.equals(User.Status.REJECTED.value)) {
            statuses.add(status);

        } else {
            statuses.add(User.Status.INACTIVE.value);
            statuses.add(User.Status.ACTIVE.value);
            statuses.add(User.Status.REJECTED.value);
        }

        Collection<Byte> roles = new ArrayList<>();

        if (role.equals(User.Role.ADMIN.value) || role.equals(User.Role.EMPLOYEE.value)
                || role.equals(User.Role.EMPLOYER.value)) {
            roles.add(role);

        } else {
            roles.add(User.Role.ADMIN.value);
            roles.add(User.Role.EMPLOYEE.value);
            roles.add(User.Role.EMPLOYER.value);
        }

        // byte[] statuses = { User.Status.INACTIVE.value, User.Status.ACTIVE.value };

        Page<User> users = userRepository.findAllByStatus(statuses, roles, search,
                PageRequest.of(page - 1, limit, Sort.by(
                        desc.booleanValue() ? Direction.DESC : Direction.ASC,
                        sortBy)));

        Pager<UserView> userViews = new Pager<>(limit, (int) users.getTotalElements(), page);

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
            throw userAlreadyNameExists();
        }

        user.setUserName(name);

        return new UserView(userRepository.save(user));

    }

    @Override
    public UserView delete(Integer userId) {

        if (SecurityUtil.isAdmin() || SecurityUtil.getCurrentUserId().equals(userId)) {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(NotFoundException::new);

            if (user.getStatus() == User.Status.REJECTED.value) {
                user.setStatus(User.Status.ACTIVE.value);
                emailUtil.sendUserActiveAlertMAil(userId, user.getEmail(), user.getName());
            } else {

                user.setStatus(User.Status.REJECTED.value);
                emailUtil.sendUserDeleteAlertMail(userId, user.getEmail(), user.getName());
            }
            user.setUpdateDate(new Date());

            return new UserView(userRepository.save(user));

        } else
            throw new BadRequestException("Invalid user");

    }

    @Override
    public Collection<UserView> deleteSelected(Collection<Integer> userIds) {

        Collection<UserView> userViews = new ArrayList<>();

        for (Integer userId : userIds) {

            Optional<User> user = userRepository.findByUserIdAndStatus(userId, User.Status.ACTIVE.value);

            if (user.isPresent()) {

                userViews.add(new UserView(userRepository.save(user.get().delete())));
                emailUtil.sendUserDeleteAlertMail(userId, user.get().getEmail(), user.get().getName());
            }
        }

        return userViews;
    }

    @Override
    public void userCsv(HttpServletResponse httpServletResponse, Collection<Byte> status, Collection<Byte> roles,
            Date startDate, Date endDate) {

        if (!SecurityUtil.isAdmin()) {
            throw new BadRequestException("permission Denied");
        }

        Collection<User> exportList = userRepository.findQueryCsv(status, roles, startDate,
                Date.from(endDate.toInstant().plus(Duration.ofDays(1))));

        if (exportList.isEmpty()) {
            throw new NotFoundException("No Records Found");
        }

        if (exportList.size() > CsvUtil.MAX_LENGTH) {
            throw new BadRequestException(
                    "Max Record Length : " + CsvUtil.MAX_LENGTH + " , Current : " + exportList.size());
        }

        String[] exclude = { "password", "passwordResetRequest", "profilePic" };

        CsvUtil.download(httpServletResponse, exportList, "Users", exclude);

    }

    @Override
    public UserDetailView updateUserDetails(UserDetailForm form) {
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new);

        user.updateDetails(form);

        return new UserDetailView(userRepository.save(user));
    }

    @Override
    public UserView updateUser(UserEditForm form, Integer userId) {

        return userRepository.findByUserId(userId)
                .map(user -> new UserView(userRepository.save(user.updateDetails(form))))
                .orElseThrow(NotFoundException::new);

    }

    @Override
    public UserDetailView getUser(Integer userId) {

        return new UserDetailView(userRepository.findByUserId(userId).orElseThrow(NotFoundException::new));
    }

    @Override
    public void setProfilePic(MultipartFile profilePic) throws IOException {

        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new);

        String fileName = user.getUserId().toString() + ".png";

        FileUtil.saveProfilePic(fileName, profilePic);

        user.setProfilePic(fileName);

        user.setUpdateDate(new Date());

        userRepository.save(user);

    }

    @Override
    public HttpEntity<byte[]> getProfilePic(Integer userId) {

        if (!SecurityUtil.isAdmin() || userId.equals(0)) {
            userId = SecurityUtil.getCurrentUserId();
        }

        String profilePic = userRepository.findByUserIdAndStatus(userId, User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new).getProfilePic();

        byte[] file = FileUtil.getProfilePic(profilePic);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(file.length);

        return new HttpEntity<>(file, headers);

    }

    @Override
    public UserView changePassword(ChangePasswordForm form) {
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new);

        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Password Does not Match");
        }

        emailUtil.changePasswordMail(user.getEmail(), user.getName());

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));

        user.setUpdateDate(new Date());

        return new UserView(userRepository.save(user));
    }

    @Override
    public Collection<StatusView> getRoleStat() {

        if (SecurityUtil.isAdmin()) {
            return userRepository.countUserRoles();
        }

        throw new BadRequestException("Illegal");
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User Not Found"));

        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException("Email not Verified");
        } else if (user.getStatus() == User.Status.REJECTED.value) {
            throw new BadRequestException("Rejected User");
        }

        String data = user.getEmail();

        Token token2 = tokenGenerator.create(PURPOSE_RESET_PASSWORD_TOKEN, data, Duration.ofMinutes(10));

        emailUtil.sendForgotPasswordRequest(token2, email);

        user.setPasswordResetRequest(true);

        userRepository.save(user);

    }

    @Override
    public void resetPassword(String token, String password) {

        Status status;
        try {
            status = tokenGenerator.verify(PURPOSE_RESET_PASSWORD_TOKEN, token);
        } catch (InvalidTokenException e) {
            throw invalidToken(e);
        } catch (TokenExpiredException e) {
            throw new BadRequestException("Token expired", e);
        }

        // Integer userId;
        // try {
        // userId = Integer.parseInt(status.data.substring(0, 10));
        // } catch (NumberFormatException e) {
        // throw invalidToken(e);
        // }

        String email = status.data;

        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundException::new);

        if (!user.isPasswordResetRequest() && user.getPassword() != null) {
            throw new BadRequestException("Invalid Request");
        }

        userRepository.save(user.resetPassword(passwordEncoder.encode(password)));

    }

    private static BadRequestException badRequestException() {
        return new BadRequestException("Invalid credentials");
    }

    private static BadRequestException userAlreadyNameExists() {
        return new BadRequestException("Username Already Exists");
    }

    private static BadRequestException invalidToken(Exception e) {
        return new BadRequestException("Invalid Token", e);
    }

    private static String formatUserId(Integer userId) {
        return String.format("%010d", userId);
    }

    @Override
    public UserDetailView deleteProfilePic(Integer userId) {

        if (!SecurityUtil.isAdmin() && !userId.equals(SecurityUtil.getCurrentUserId())) {
            throw new BadRequestException("Invalid");
        }

        return userRepository.findByUserId(userId).map(
                u -> {
                    u.setProfilePic("");
                    u.setUpdateDate(new Date());
                    return new UserDetailView(userRepository.save(u));
                }).orElseThrow(NotFoundException::new);

        // user.setProfilePic("");
        // user.setUpdateDate(new Date());
        // userRepository.save(user);
        // return new UserDetailView(user);

        // return new UserDetailView(userRepository.findByUserId(userId).map(
        // user -> {
        // user.setProfilePic("null");
        // user.setUpdateDate(new Date());
        // return user;
        // }).orElseThrow(() -> new BadRequestException("Invalid User")));
    }

    @Override
    public Map<String, Integer> getUserCount() {

        Collection<ChartView> chartViewvalues = userRepository.getJoinDates();
        DateTime today = new DateTime();
        int n = 7;
        int i;
        HashMap<String, Integer> datawithdate = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        for (i = 0; i < n; i++) {

            for (ChartView chart : chartViewvalues) {
                if (formatter.parseLocalDate(today.toString("yyyy-MM-dd"))
                        .isEqual(formatter.parseLocalDate(chart.getDate()))) {
                    datawithdate.put(chart.getDate(), chart.getCount());

                } else {
                    datawithdate.putIfAbsent(today.toString("yyyy-MM-dd"), 0);
                }
            }
            today = today.minusDays(1);
        }
        Map<String, Integer> treeMap = new TreeMap<String, Integer>(datawithdate);

        System.out.println(treeMap);

        return treeMap;

    }

    public static String addOneDayJodaTime(String date) {
        DateTime dateTime = new DateTime(date);
        return dateTime
                .minusDays(1)
                .toString("yyyy-MM-dd");
    }

}
