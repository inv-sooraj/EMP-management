package com.project.EmployeeManagement.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.entity.JobRequest;
import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.JobRequestForm;
import com.project.EmployeeManagement.repository.JobRepository;
import com.project.EmployeeManagement.repository.JobRequestRepository;
import com.project.EmployeeManagement.repository.UserRepository;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.service.JobRequestService;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.view.JobRequestView;

@Service
public class JobRequestImpl implements JobRequestService {

        @Autowired
        private JobRequestRepository jobRequestRepository;

        @Autowired
        private JobRepository jobRepository;

        @Autowired
        private UserRepository userRepository;

        @Override
        public JobRequestView addJobRequest(Integer jobId) {
                Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

                if (userRole.equals(User.Role.USER.value)) {

                        return new JobRequestView(
                                        jobRequestRepository
                                                        .save(new JobRequest(jobId, SecurityUtil.getCurrentUserId())));
                } else
                        throw new BadRequestException("Illegal Access");
        }

        @Override
        public Pager<JobRequestView> listItem(String search, String limit, String sort, String page) {
                Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

                if (!userRole.equals(User.Role.USER.value)) {

                        Page<JobRequest> jobsRequests = jobRequestRepository
                                        .findAllByUserUserId(SecurityUtil.getCurrentUserId(),
                                                        search,
                                                        PageRequest.of(Integer.parseInt(page) - 1,
                                                                        Integer.parseInt(limit),
                                                                        Sort.by(sort).ascending()));

                        Pager<JobRequestView> JobRequestViews = new Pager<JobRequestView>(Integer.parseInt(limit),
                                        (int) jobsRequests.getTotalElements(),
                                        Integer.parseInt(page));

                        JobRequestViews.setResult(
                                        jobsRequests.getContent().stream()
                                                        .map(jobsRequest -> new JobRequestView(jobsRequest))
                                                        .collect(Collectors.toList()));

                        return JobRequestViews;

                } else if (userRole.equals(User.Role.USER.value)) {
                        Page<JobRequest> jobsRequests = jobRequestRepository
                                        .findByJobReqId(SecurityUtil.getCurrentUserId(), search,
                                                        PageRequest.of(Integer.parseInt(page) - 1,
                                                                        Integer.parseInt(limit),
                                                                        Sort.by(sort).ascending()));

                        Pager<JobRequestView> JobRequestViews = new Pager<JobRequestView>(Integer.parseInt(limit),
                                        (int) jobsRequests.getTotalElements(),
                                        Integer.parseInt(page));

                        JobRequestViews.setResult(
                                        jobsRequests.getContent().stream()
                                                        .map(jobsRequest -> new JobRequestView(jobsRequest))
                                                        .collect(Collectors.toList()));

                        return JobRequestViews;

                } else
                        throw new BadRequestException("invalid");

        }

        @Override
        public JobRequestView edit(Integer jobRequestId, JobRequestForm form) {

                JobRequest jobRequest = jobRequestRepository.findById(jobRequestId).orElseThrow(NotFoundException::new);

                if ((form.getStatus() == 0)) {
                        Job job = jobRequest.getJob();

                        job.setOpenings(job.getOpenings() - 1);
                        jobRepository.save(job);

                }

                jobRequest.changeStatus(form);

                jobRequestRepository.save(jobRequest);
                return new JobRequestView(jobRequest);

        }

        @Override
        public Collection<Integer> appliedJobs() {

                return jobRequestRepository.getAppliedJobs(SecurityUtil.getCurrentUserId());

        }

}
