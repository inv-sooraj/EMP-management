package com.project.EmployeeManagement.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.JobForm;
import com.project.EmployeeManagement.repository.JobRepository;
import com.project.EmployeeManagement.repository.UserRepository;
import com.project.EmployeeManagement.util.Pager;
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

        // if (userRole != 2) {
        if (!userRole.equals(User.Role.USER.value)) {

            return new JobView(
                    jobRepository.save(new Job(form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()))));
        } else
            throw new BadRequestException("Illegal Access");

    }

    @Override
    public Collection<JobView> list() {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();
        if (userRole.equals(User.Role.ADMIN.value)) {
            return jobRepository.findAllByStatus(Job.Status.ACTIVE.value);
        } else {
            return jobRepository.findByUserUserIdAndStatus(SecurityUtil.getCurrentUserId(), Job.Status.ACTIVE.value);

        }
    }

    @Override
    @Transactional
    public void delete(Integer itemId) {

        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (!userRole.equals(User.Role.USER.value)) {
            Job job = jobRepository.findById(itemId).orElseThrow(NotFoundException::new);

            job.setStatus(Job.Status.DELETED.value);

            job.setUpdateDate(new Date());

            jobRepository.save(job);
        } else
            throw new BadRequestException("illegal access");
        // return;
    }

    @Override
    @Transactional
    public JobDetailView update(Integer jobId, JobForm form) throws NotFoundException {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (userRole.equals(User.Role.ADMIN.value)) {
        return new JobDetailView(jobRepository.save(
                jobRepository.findByJobIdAndUserUserId(jobId, SecurityUtil.getCurrentUserId())
                        .orElseThrow(NotFoundException::new).update(form)));
        }else
        throw new BadRequestException("illegal access");
    }

    // ...........................pagination.........................

    @Override
    public Pager<JobDetailView> listItem(String search, String limit, String sort, String page) {

        Page<Job> jobs = jobRepository.find(Job.Status.ACTIVE.value, search,
                PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(sort).ascending()));

        Pager<JobDetailView> jobDetailViews = new Pager<JobDetailView>(Integer.parseInt(limit),
                (int) jobs.getTotalElements(),
                Integer.parseInt(page));

        jobDetailViews.setResult(
                jobs.getContent().stream().map(job -> new JobDetailView(job)).collect(Collectors.toList()));

        return jobDetailViews;
    }

    // ...........................pagination.........................

}
