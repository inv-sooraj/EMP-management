/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.training.employee.management.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovaturelabs.training.employee.management.entity.JobRequest;
import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.repository.JobRequestRepository;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.JobRequestService;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

@Service
public class JobRequestServiceImpl implements JobRequestService {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Override
    public JobRequestView add(JobRequestForm form, Integer jobId) {

        return new JobRequestView(
                jobRequestRepository.save(new JobRequest(form, SecurityUtil.getCurrentUserId(), jobId)));

    }

    @Override
    public Collection<JobRequestView> list() {

        return jobRequestRepository.findAll().stream().map(jobRequest -> new JobRequestView(jobRequest))
                .collect(Collectors.toList());
    }

}
