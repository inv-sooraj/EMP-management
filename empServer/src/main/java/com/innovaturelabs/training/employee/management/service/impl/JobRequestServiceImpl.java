
package com.innovaturelabs.training.employee.management.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.innovaturelabs.training.employee.management.util.CsvDownload;
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

    // @Override
    // public JobRequestView add(JobRequestForm form, Integer jobId) {

    // return new JobRequestView(
    // jobRequestRepository.save(new JobRequest(form,
    // SecurityUtil.getCurrentUserId(), jobId)));

    // }

    @Override
    public JobRequestView add(Integer jobId) {

        Job job = jobRepository.findByJobIdAndStatus(jobId, Job.Status.APPROVED.value)
                .orElseThrow(NotFoundException::new);

        if (jobRequestRepository.findByUserUserIdAndJobJobId(SecurityUtil.getCurrentUserId(), jobId).isPresent()) {
            throw new BadRequestException("Already Applied");
        }

        if (userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value)
                .orElseThrow(NotFoundException::new).getQualification() < job.getQualification()) {
            throw new BadRequestException("Insufficien Qualification");

        }
        return new JobRequestView(
                jobRequestRepository.save(new JobRequest(SecurityUtil.getCurrentUserId(), jobId)));

    }

    @Override
    public Pager<JobRequestView> list(Integer page, Integer limit, String sortBy, String search) {

        if (!jobRequestRepository.findColumns().contains(sortBy)) {
            sortBy = "job_request_id";
        }

        if (page <= 0) {
            page = 1;
        }
        Page<JobRequest> jobRequests;

        byte[] status = { JobRequest.Status.PENDING.value, JobRequest.Status.APPROVED.value,
                JobRequest.Status.REJECT.value };

        if (SecurityUtil.getCurrentUserRole().equals("EMPLOYER") || SecurityUtil.getCurrentUserRole().equals("ADMIN")) {

            jobRequests = jobRequestRepository.findAllByUserIdStatus(SecurityUtil.getCurrentUserId(),
                    status, search,
                    PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));

        } else if (SecurityUtil.getCurrentUserRole().equals("EMPLOYEE")) {

            jobRequests = jobRequestRepository.findAllByUserUserIdStatus(SecurityUtil.getCurrentUserId(),
                    status, search,
                    PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));
        } else {
            throw new BadRequestException("Illegal Access");

        }

        Pager<JobRequestView> jobRequestViews = new Pager<>(limit, (int) jobRequests.getTotalElements(), page);

        // Pager<JobView> jobViews = new
        // Pager<JobView>(limit,jobRequestRepository.countJobList(Job.Status.PENDING.value,
        // search).intValue(),page);

        jobRequestViews.setResult(jobRequests.getContent().stream().map(JobRequestView::new)
                .collect(Collectors.toList()));

        return jobRequestViews;

    }

    @Override
    public void jobRequestCsv(HttpServletResponse httpServletResponse) {

        String role = SecurityUtil.getCurrentUserRole();

        if (role.equals("EMPLOYEE")) {
            throw new BadRequestException("Access Denied");
        }

        Collection<JobRequestView> exportlist;

        if (role.equals("ADMIN")) {
            exportlist = jobRequestRepository.findAll().stream().map(JobRequestView::new)
                    .collect(Collectors.toList());
        } else {
            exportlist = jobRequestRepository.findAllByUserUserId(SecurityUtil.getCurrentUserId()).stream()
                    .map(JobRequestView::new).collect(Collectors.toList());

        }

        CsvDownload.download(httpServletResponse, exportlist, "Job_Requests");

    }

    @Override
    public Collection<Integer> appliedJobs() {
        return jobRequestRepository.getAppliedJobs(SecurityUtil.getCurrentUserId());
    }

    @Override
    public JobRequestView update(Integer jobRequestId, JobRequestForm form) {

        if (SecurityUtil.getCurrentUserRole() == null || SecurityUtil.getCurrentUserRole().equals("EMPLOYEE")) {
            throw new BadRequestException("Illegal Access");
        }

        JobRequest jobRequest = jobRequestRepository.findByJobRequestId(jobRequestId)
                .orElseThrow(NotFoundException::new);

        Job job = jobRequest.getJob();

        if (!jobRequest.getStatus().equals(JobRequest.Status.APPROVED.value)
                && form.getStatus().equals(JobRequest.Status.APPROVED.value)) {

            job.setOpenings(job.getOpenings() - 1);

            jobRepository.save(job);

        } else if (jobRequest.getStatus().equals(JobRequest.Status.APPROVED.value)
                && !form.getStatus().equals(JobRequest.Status.APPROVED.value)) {
            job.setOpenings(job.getOpenings() + 1);
            jobRepository.save(job);
        }

        jobRequest.setStatus(form.getStatus());

        jobRequest.setRemark(form.getRemark());

        jobRequest.setUpdateDate(new Date());

        return new JobRequestView(jobRequestRepository.save(jobRequest));
    }

}
