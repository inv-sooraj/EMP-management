
package com.innovaturelabs.training.employee.management.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.validation.Errors;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.AdminAddUserForm;
import com.innovaturelabs.training.employee.management.form.ChangePasswordForm;
import com.innovaturelabs.training.employee.management.form.LoginForm;
import com.innovaturelabs.training.employee.management.form.UserDetailForm;
import com.innovaturelabs.training.employee.management.form.UserEditForm;
import com.innovaturelabs.training.employee.management.form.UserForm;
import com.innovaturelabs.training.employee.management.form.UserProfilePicForm;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.LoginView;
import com.innovaturelabs.training.employee.management.view.StatusView;
import com.innovaturelabs.training.employee.management.view.UserDetailView;
import com.innovaturelabs.training.employee.management.view.UserView;

public interface UserService {

    UserView add(UserForm form);

    UserView adminAdd(AdminAddUserForm form);

    // UserView update(Integer userId, UserForm form) throws NotFoundException;

    UserView updateUserName(Integer userId, String name) throws NotFoundException;

    void delete(Integer userId);

    void deleteSelected(Collection<Integer> userIds);

    UserDetailView currentUser();

    UserDetailView getUser(Integer userId);

    LoginView login(LoginForm form, Errors errors) throws BadRequestException;

    LoginView refresh(String refreshToken) throws BadRequestException;

    Pager<UserView> list(Integer page, Integer limit, String sortBy, String search,Byte status, Boolean desc);

    UserDetailView updateUserDetails(UserDetailForm form);

    UserView updateUser(UserEditForm form, Integer userId);

    UserView changePassword(ChangePasswordForm form);

    void userCsv(HttpServletResponse httpServletResponse,Collection<Byte> status,Collection<Byte> roles,Date startDate,Date endDate) ;

    void setProfilePic(UserProfilePicForm form) throws IOException;

    HttpEntity<byte[]> getProfilePic(Integer userId);

    Collection<StatusView> getRoleStat();

    void forgotPassword(String email);

    void resetPassword(String token, String password);

    UserDetailView deleteProfilePic(Integer userId);

}
