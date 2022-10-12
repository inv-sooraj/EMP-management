package com.project.EmployeeManagement.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
                Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(NotFoundException::new).getRole();

                if (userRole.equals(User.Role.USER.value)) {

                        return new JobRequestView(
                                        jobRequestRepository
                                                        .save(new JobRequest(jobId, SecurityUtil.getCurrentUserId())));
                } else
                        throw new BadRequestException("Illegal Access");
        }

        @Override
        public Pager<JobRequestView> listItem(String search, String limit, String sort, Boolean desc, String page) {
                Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(NotFoundException::new).getRole();
                Page<JobRequest> jobsRequests;
                if (!userRole.equals(User.Role.USER.value)) {

                        jobsRequests = jobRequestRepository
                                        .findAllByUserUserId(SecurityUtil.getCurrentUserId(),
                                                        search,
                                                        PageRequest.of(Integer.parseInt(page) - 1,
                                                                        Integer.parseInt(limit),
                                                                        Sort.by(desc.booleanValue() ? Direction.DESC
                                                                                        : Direction.ASC,
                                                                                        sort)));

                } else if (userRole.equals(User.Role.USER.value)) {
                        jobsRequests = jobRequestRepository
                                        .findByJobReqId(SecurityUtil.getCurrentUserId(), search,
                                                        PageRequest.of(Integer.parseInt(page) - 1,
                                                                        Integer.parseInt(limit),
                                                                        Sort.by(sort).ascending()));

                } else
                        throw new BadRequestException("invalid");

                Pager<JobRequestView> jobRequestViews = new Pager<>(Integer.parseInt(limit),
                                (int) jobsRequests.getTotalElements(),
                                Integer.parseInt(page));

                jobRequestViews.setResult(
                                jobsRequests.getContent().stream()
                                                .map(jobsRequest -> new JobRequestView(jobsRequest))
                                                .collect(Collectors.toList()));

                return jobRequestViews;

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
