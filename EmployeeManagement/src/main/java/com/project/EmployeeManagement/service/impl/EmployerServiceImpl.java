package com.project.EmployeeManagement.service.impl;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.repository.UserRepository;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.service.EmployerService;
import com.project.EmployeeManagement.view.UserView;

@Service
public class EmployerServiceImpl implements EmployerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserView addEmployer(UserForm form) {
        if (userRepository.findByUserName(form.getUserName()).isPresent()) {
            throw new BadRequestException("Already Exists");
        }

        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole.equals(User.Role.ADMIN.value)) {

            return new UserView(userRepository.save(new User(form.getUserName(),
                    form.getName(),
                    form.getEmail(),
                    passwordEncoder.encode(form.getPassword()), User.Role.EMPLOYER.value)));
        } else
            throw new BadRequestException("Illegal Access");
    }

    @Override
    @Transactional
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

    @Override
    public Collection<User> list() {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole.equals(User.Role.ADMIN.value)) {
            return userRepository.findByRoleAndStatus(User.Role.EMPLOYER.value, User.Status.ACTIVE.value);
        } else
            throw new BadRequestException("Illegal Access");

    }

}
