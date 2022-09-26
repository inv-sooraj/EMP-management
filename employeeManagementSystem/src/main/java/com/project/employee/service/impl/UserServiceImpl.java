package com.project.employee.service.impl;

import static com.project.employee.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.project.employee.entity.User;
import com.project.employee.exception.BadRequestException;
import com.project.employee.exception.NotFoundException;
import com.project.employee.exception.UserNotFoundException;
import com.project.employee.features.Pager;
import com.project.employee.form.LoginForm;
import com.project.employee.form.UserDetailForm;
import com.project.employee.form.UserRegistrationForm;
import com.project.employee.repository.UserRepository;
import com.project.employee.security.config.SecurityConfig;
import com.project.employee.security.util.InvalidTokenException;
import com.project.employee.security.util.SecurityUtil;
import com.project.employee.security.util.TokenExpiredException;
import com.project.employee.security.util.TokenGenerator;
import com.project.employee.security.util.TokenGenerator.Status;
import com.project.employee.security.util.TokenGenerator.Token;
import com.project.employee.service.UserService;
import com.project.employee.view.LoginView;
import com.project.employee.view.UserDetailView;
import com.project.employee.view.UserView;

@Service
public class UserServiceImpl implements UserService {
	private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private SecurityConfig securityConfig;

	@Override
	public UserView add(UserRegistrationForm form) {
		return new UserView(userRepository.save(new User(form.getUserName(), form.getEmail(),
				passwordEncoder.encode(form.getPassword()), form.getRole())));
	}

	@Override
	public LoginView login(LoginForm form, Errors errors) throws BadRequestException {
		if (errors.hasErrors()) {
			throw badRequestException();
		}
		User user = userRepository.findByUserNameAndStatus(form.getUserName(), User.Status.ACTIVE.value)
				.orElseThrow(UserServiceImpl::userNotFoundException);

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

		User user = userRepository.findByUserIdAndPasswordAndStatus(userId, password, User.Status.ACTIVE.value)
				.orElseThrow(UserServiceImpl::badRequestException);

		String id = String.format("%010d", user.getUserId());
		Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, securityConfig.getAccessTokenExpiry());
		return new LoginView(user, new LoginView.TokenView(accessToken.value, accessToken.expiry),
				new LoginView.TokenView(refreshToken, status.expiry));
	}

	private static BadRequestException badRequestException() {
		return new BadRequestException("Invalid credentials");
	}

	private static UserNotFoundException userNotFoundException() {
		return new UserNotFoundException("User not Found");
	}

	@Override
	public UserView currentUser() {
		return new UserView(
				userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
						.orElseThrow(NotFoundException::new));
	}

	@Override
	public UserView update(UserDetailForm form) {
		return new UserView(userRepository
				.save(userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
						.orElseThrow(NotFoundException::new).update(form)));
	}

	@Override
	@Transactional
	public void delete(Integer userId) {
		User user = userRepository.findByUserIdAndStatus(userId, User.Status.ACTIVE.value)
				.orElseThrow(NotFoundException::new);

		user.setStatus(User.Status.INACTIVE.value);

		userRepository.save(user);

		return;
	}
	
	@Override
    public void deleteSelected(Collection<Integer> userIds) {

        for (Integer userId : userIds) {
            
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()) {
                userRepository.save(user.get().delete());
            }

        }

    }

	@Override
	public long userCount() {
		return userRepository.countUsers(User.Status.ACTIVE.value);
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
		Pager<UserView> userViews = new Pager<UserView>(limit, (int) users.getTotalElements(), page, limit);

		userViews.setResult(users.getContent().stream().map(UserView::new).collect(Collectors.toList()));

		return userViews;

	}

	@Override
	@Transactional
	public void csvUser(HttpServletResponse httpServletResponse) {
		Collection<UserView> exportlist = userRepository.findAllByStatus(User.Status.ACTIVE.value).stream()
				.map(UserView::new).collect(Collectors.toList());
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String headerKey = "Content-Disposition";
		String headerValue = "userlist" + sdf.format(dt) + ".csv";
		httpServletResponse.setHeader(headerKey, headerValue);
		httpServletResponse.setContentType("text/csv;");
		httpServletResponse.setCharacterEncoding("shift-jis");
		httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

		try {

			ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
					CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = { "User Id", "User Name", "Email", "Role", "Create Date", "Update Date" };
			String[] nameMapping = { "userId", "userName", "email", "role", "createDate", "updateDate" };

			csvWriter.writeHeader(csvHeader);
			for (UserView reservation : exportlist) {
				csvWriter.write(reservation, nameMapping);
			}

			csvWriter.close();
		} catch (IOException e) {
			throw new BadRequestException("Exception while exporting csv");
		}

	}

	@Override
	public UserDetailView detailView() {
		return new UserDetailView(
				userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
						.orElseThrow(NotFoundException::new));
	}

}
