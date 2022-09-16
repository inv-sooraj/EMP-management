package com.project.EmployeeManagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.EmployeeManagement.entity.JobRequest;
import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.form.JobRequestForm;
import com.project.EmployeeManagement.repository.JobRequestRepository;
import com.project.EmployeeManagement.repository.UserRepository;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.service.JobRequestService;
import com.project.EmployeeManagement.view.JobRequestView;

@Service
public class JobRequestImpl implements JobRequestService {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired

    private UserRepository userRepository;

    @Override
    public JobRequestView addJobRequest(Integer jobId, JobRequestForm form) {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole.equals(User.Role.USER.value)) {

            return new JobRequestView(
                    jobRequestRepository
                            .save(new JobRequest(form, jobId, SecurityUtil.getCurrentUserId())));
        } else
            throw new BadRequestException("Illegal Access");
    }

}
