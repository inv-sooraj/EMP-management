package com.project.EmployeeManagement.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.entity.JobRequest;
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
    public JobRequestView addJobRequest(JobRequestForm form) {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole == 0) {

            return new JobRequestView(
                    jobRequestRepository
                            .save(new JobRequest(form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()))));
        } else
            throw new BadRequestException("Illegal Access");
    }

}
