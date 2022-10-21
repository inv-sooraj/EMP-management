
package com.innovaturelabs.training.employee.management.service.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.entity.JobRequest;
import com.innovaturelabs.training.employee.management.entity.User;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.repository.JobRepository;
import com.innovaturelabs.training.employee.management.repository.JobRequestRepository;
import com.innovaturelabs.training.employee.management.repository.UserRepository;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.JobRequestService;
import com.innovaturelabs.training.employee.management.util.CsvUtil;
import com.innovaturelabs.training.employee.management.util.EmailUtil;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

@Service
public class JobRequestServiceImpl implements JobRequestService {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    private final Set<String> jobRequestFields = Arrays.stream(JobRequest.class.getDeclaredFields()).map(Field::getName)
            .collect(Collectors.toSet());

    @Override
    public JobRequestView add(Integer jobId) {

        if (!SecurityUtil.isEmployee()) {
            throw new BadRequestException("Need Employee Account For Applying Job");
        }

        Job job = jobRepository.findByJobIdAndStatus(jobId, Job.Status.APPROVED.value)
                .orElseThrow(NotFoundException::new);

        if (jobRequestRepository.findByUserUserIdAndJobJobId(SecurityUtil.getCurrentUserId(), jobId).isPresent()) {
            throw new BadRequestException("Already Applied");
        }

        if (userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new).getQualification() < job.getQualification()) {
            throw new BadRequestException("Insufficient Qualification");

        }
        return new JobRequestView(
                jobRequestRepository.save(new JobRequest(SecurityUtil.getCurrentUserId(), jobId)));

    }

    @Override
    public Pager<JobRequestView> list(Integer page, Integer limit, String sortBy, String search, Boolean desc) {

        // if (!jobRequestRepository.findColumns().contains(sortBy)) {
        // sortBy = "job_request_id";
        // }

        sortBy = jobRequestFields.contains(sortBy) ? sortBy : "jobRequestId";

        Page<JobRequest> jobRequests;

        byte[] status = { JobRequest.Status.PENDING.value, JobRequest.Status.APPROVED.value,
                JobRequest.Status.REJECT.value };

        if (SecurityUtil.isEmployer() || SecurityUtil.isAdmin()) {

            jobRequests = jobRequestRepository.findByJobUserUserIdAndStatusInAndRemarkContaining(
                    SecurityUtil.getCurrentUserId(),
                    status, search,
                    PageRequest.of(page - 1, limit, Sort.by(
                            desc.booleanValue() ? Direction.DESC : Direction.ASC,
                            sortBy)));

        } else {

            jobRequests = jobRequestRepository.findAllByUserUserIdAndStatusInAndRemarkContaining(
                    SecurityUtil.getCurrentUserId(),
                    status, search,
                    PageRequest.of(page - 1, limit, Sort.by(
                            desc.booleanValue() ? Direction.DESC : Direction.ASC,
                            sortBy)));
        }

        Pager<JobRequestView> jobRequestViews = new Pager<>(limit, (int) jobRequests.getTotalElements(), page);

        jobRequestViews.setResult(jobRequests.getContent().stream().map(JobRequestView::new)
                .collect(Collectors.toList()));

        return jobRequestViews;

    }

    @Override
    public void jobRequestCsv(HttpServletResponse httpServletResponse) {

        if (SecurityUtil.isEmployee()) {
            throw new BadRequestException("Access Denied");
        }

        Collection<JobRequestView> exportList;

        if (SecurityUtil.isAdmin()) {
            exportList = jobRequestRepository.findAll().stream().map(JobRequestView::new)
                    .collect(Collectors.toList());
        } else {
            exportList = jobRequestRepository.findAllByUserUserId(SecurityUtil.getCurrentUserId()).stream()
                    .map(JobRequestView::new).collect(Collectors.toList());

        }
        String[] exclude = {};

        CsvUtil.download(httpServletResponse, exportList, "Job_Requests", exclude);

    }

    @Override
    public Collection<Integer> appliedJobs() {
        return jobRequestRepository.getAppliedJobs(SecurityUtil.getCurrentUserId());
    }

    @Override
    public JobRequestView update(Integer jobRequestId, JobRequestForm form) {

        if (SecurityUtil.isEmployee()) {
            throw new BadRequestException("Illegal Access");
        }

        JobRequest jobRequest = jobRequestRepository.findByJobRequestId(jobRequestId)
                .orElseThrow(NotFoundException::new);

        Job job = jobRequest.getJob();

        if (job.getStatus() != Job.Status.APPROVED.value) {
            throw new BadRequestException("Invalid Operation");
        }

        if (!jobRequest.getStatus().equals(JobRequest.Status.APPROVED.value)
                && form.getStatus().equals(JobRequest.Status.APPROVED.value)) {

            job.setOpenings(job.getOpenings() - 1);

            if (job.getOpenings() <= 0) {
                job.setStatus(Job.Status.COMPLETED.value);
            }

            emailUtil.sendJobRequestStatus(jobRequest.getUser().getEmail(), jobRequest.getUser().getName(),
                    jobRequestId,
                    jobRequest.getJob().getTitle(), form.getRemark(), true);

            jobRepository.save(job);

        } else if (jobRequest.getStatus().equals(JobRequest.Status.APPROVED.value)
                && !form.getStatus().equals(JobRequest.Status.APPROVED.value)) {

            if (job.getOpenings() == 0) {
                job.setStatus(Job.Status.APPROVED.value);
            }

            emailUtil.sendJobRequestStatus(jobRequest.getUser().getEmail(), jobRequest.getUser().getName(),
                    jobRequestId,
                    jobRequest.getJob().getTitle(), form.getRemark(), false);

            job.setOpenings(job.getOpenings() + 1);

            jobRepository.save(job);
        }

        jobRequest.setStatus(form.getStatus());

        jobRequest.setRemark(form.getRemark());

        jobRequest.setUpdateDate(new Date());

        return new JobRequestView(jobRequestRepository.save(jobRequest));
    }

}
