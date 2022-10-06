package com.project.employee.service.impl;

import static com.project.employee.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
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
import com.project.employee.features.EmailSenderService;
import com.project.employee.features.Pager;
import com.project.employee.form.ChangePasswordForm;
import com.project.employee.form.ForgotPasswordForm;
import com.project.employee.form.ImageForm;
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
	private EmailSenderService emailService;
	
	private TextEncryptor textEncryptor;


	@Autowired
	private SecurityConfig securityConfig;

	@Override
	public UserView add(UserRegistrationForm form) throws UnsupportedEncodingException, MessagingException {
		if (userRepository.findByUserName(form.getUserName()).isPresent()) {
			throw new BadRequestException("Username Already Exists");
		}

		if (userRepository.findByEmail(form.getEmail()).isPresent()) {
			throw new BadRequestException("Email Already Exists");
		}
		User user=userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
				.orElseThrow(NotFoundException::new);
		if(user.getRole()==0) {
			String emailId = form.getEmail();
			System.out.println(emailId);
			String subject = "Login Credentials";
			String body = "Your Username and Password to log on to the JobFinder are: \n"
					+ "\n Username : "+form.getUserName()
					+ "\n Password : "+form.getPassword();
			emailService.sendEmail(emailId, subject, body);
		}
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
	public User uploadImage(ImageForm form) {
		User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(NotFoundException::new);

		String fileName = user.getName() + user.getUserId() + user.getRole() + "."
				+ FilenameUtils.getExtension(form.getProfilePhoto().getOriginalFilename());
		String path = "src/main/resources/static/images/";

		Path uploadPath = Paths.get(path);

		try {
			InputStream inputStream = form.getProfilePhoto().getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		user.setImagePath(fileName);
		userRepository.save(user);
		return user;
	}

	public byte[] getImgBin(String fileName) {
		try {
			return Files.readAllBytes(Path.of("src/main/resources/static/images/" + fileName));
		} catch (IOException e) {
			throw new BadRequestException("File Not Found");
		}
	}

	// to get profile picture
	public HttpEntity<byte[]> getImg() {

		User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(NotFoundException::new);
		String fileName = user.getImagePath();
		byte[] file = getImgBin(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentLength(file.length);
		return new HttpEntity<>(file, headers);
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

	@Override
	public UserView changePassword(ChangePasswordForm form) throws NotFoundException {
		User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());

		if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
			throw new BadRequestException("Password Mismatch");
		}
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		user.setUpdateDate(new Date());
		return new UserView(userRepository.save(user));
	}

	@Override
	public void forgotPassword(String token, ForgotPasswordForm form) {
		System.out.println(form.getEmail());
				User user = userRepository.findByEmail(form.getEmail()).orElseThrow(NotFoundException::new);
				if(user != null) {
					
					long t = System.currentTimeMillis();
					Duration passwordResetTokenExpiry=Duration.ofMinutes(10);
					textEncryptor = Encryptors.text("7C481ADD4AF55AB8", "374195D5E3080DC1");
					String tokenBfrEncript = token+"#"+t+"#"+passwordResetTokenExpiry.toMillis();
					String finalToken=textEncryptor.encrypt(tokenBfrEncript);
					user.setResetPswdToken(token);
					userRepository.save(user);
							
		
					String url="http://localhost:4200/resetpswd/"+finalToken;
					String emailId=user.getEmail();
					String subject="Reset your password";
					String body= "<h3>Click the link to reset the password </h3>"
							+"<a href="+url+">Click here to reset password</a>";
					try {
						emailService.sendEmail(emailId, subject, body);
					} catch (UnsupportedEncodingException | MessagingException e) {
						e.printStackTrace();
					}
					
				} 
		
	}

	@Override
	public void resetPswd(String token, String password) {
		String decryptedToken = textEncryptor.decrypt(token);
        String[] parts = decryptedToken.split("#");
        String tokenFromUrl=parts[0];
        long createTime=Long.parseLong(parts[1]);
        long tokenExpiry=Long.parseLong(parts[2]);
        long currentTime=System.currentTimeMillis();
        long timeDiff=currentTime-createTime;
        
        if(timeDiff>=tokenExpiry) {
        	throw new TokenExpiredException("Reset link expired");
        }
        else {
        	
        		User user = userRepository.findByResetPswdToken(tokenFromUrl).orElseThrow(NotFoundException::new);
        		user.setPassword(passwordEncoder.encode(password));
        		user.setResetPswdToken(token);
        		System.out.println("employee ex");
        		userRepository.save(user);
        	}
		
	}

}
