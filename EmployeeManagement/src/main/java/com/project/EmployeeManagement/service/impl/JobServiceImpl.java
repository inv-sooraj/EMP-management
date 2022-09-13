package com.project.EmployeeManagement.service.impl;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.JobForm;
import com.project.EmployeeManagement.repository.JobRepository;
import com.project.EmployeeManagement.repository.UserRepository;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.service.JobService;
import com.project.EmployeeManagement.view.JobDetailView;
import com.project.EmployeeManagement.view.JobView;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public JobView addJob(JobForm form) {

        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole != 0) {

            return new JobView(
                jobRepository.save(new Job(form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()))));
        } else
            throw new BadRequestException("Illegal Access");
        
    }

    @Override
    public Collection<JobView> list() {
        return jobRepository.findAllByUserUserIdAndStatus(SecurityUtil.getCurrentUserId(), Job.Status.ACTIVE.value);

    }

    @Override
    @Transactional
    public void delete(Integer itemId) {
        Job job = jobRepository.findById(itemId).orElseThrow(NotFoundException::new);

        job.setStatus(Job.Status.DELETED.value);

        job.setUpdateDate(new Date());

        jobRepository.save(job);

        return;
    }

    @Override
    @Transactional
    public JobDetailView update(Integer jobId, JobForm form) throws NotFoundException {
        return new JobDetailView(jobRepository.save(
                jobRepository.findByJobIdAndUserUserId(jobId, SecurityUtil.getCurrentUserId()).get().update(form)));
    }

}
