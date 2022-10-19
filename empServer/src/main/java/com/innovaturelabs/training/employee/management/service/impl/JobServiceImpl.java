
package com.innovaturelabs.training.employee.management.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.exception.NotFoundException;
import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.repository.JobRepository;
import com.innovaturelabs.training.employee.management.repository.UserRepository;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.JobService;
import com.innovaturelabs.training.employee.management.util.CsvDownload;
import com.innovaturelabs.training.employee.management.util.EmailUtil;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobView;
import com.innovaturelabs.training.employee.management.view.StatusView;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    private final Set<String> jobFields = Arrays.stream(Job.class.getDeclaredFields()).map(Field::getName)
            .collect(Collectors.toSet());

    @Override
    public JobView add(JobForm form) {

        if (!SecurityUtil.isAdmin()
                && !SecurityUtil.isEmployer()) {
            throw illegalAccess();
        }

        Byte status = Job.Status.PENDING.value;

        if (SecurityUtil.isAdmin()) {
            status = Job.Status.APPROVED.value;
        }

        return new JobView(jobRepository.save(new Job(form.getTitle(),
                form.getDescription(),
                form.getQualification(),
                form.getOpenings(),
                SecurityUtil.getCurrentUserId(),
                status)));

    }

    @Override
    public JobView getJob(Integer jobId) {
        return new JobView(jobRepository.findByJobId(jobId).orElseThrow(NotFoundException::new));
    }

    @Override
    public JobView update(JobForm form, Integer jobId) {

        if (SecurityUtil.isEmployer()) {

            return new JobView(jobRepository
                    .save(jobRepository.findByJobIdAndUserUserId(jobId, SecurityUtil.getCurrentUserId())
                            .orElseThrow(() -> new BadRequestException("Invalid JobId : " + jobId)).update(form)));

        }

        else if (SecurityUtil.isAdmin()) {

            return new JobView(jobRepository.save(jobRepository.findByJobId(jobId)
                    .orElseThrow(() -> new NotFoundException("Job Not Found for JobId : " + jobId)).update(form)));
        }

        throw illegalAccess();

    }

    @Override
    public Pager<JobView> list(Integer page, Integer limit, String sortBy, Boolean desc, String search,
            Integer selectedStatus, Boolean apply) {

        // sortBy = jobRepository.findColumns().contains(sortBy) ? sortBy : "job_id";

        sortBy = jobFields.contains(sortBy) ? sortBy : "jobId";

        Page<Job> jobs;

        if (SecurityUtil.isEmployee() || apply.booleanValue()) {

            byte[] status = { Job.Status.APPROVED.value };

            jobs = jobRepository.findAllByStatusAndOpenings(status,
                    search, PageRequest.of(page - 1, limit, Sort.by(
                            desc.booleanValue() ? Direction.DESC : Direction.ASC,
                            sortBy)));

        } else if (SecurityUtil.isEmployer()) {
            byte[] status = {
                    Job.Status.APPROVED.value,
                    Job.Status.PENDING.value,
                    Job.Status.COMPLETED.value
            };

            jobs = jobRepository.findAllByUserIdStatus(SecurityUtil.getCurrentUserId(), status,
                    search, PageRequest.of(page - 1, limit, Sort.by(
                            desc.booleanValue() ? Direction.DESC : Direction.ASC,
                            sortBy)));

        } else {

            ArrayList<Byte> status = new ArrayList<>();

            if (selectedStatus >= 0 && selectedStatus < 4) {
                status.add(selectedStatus.byteValue());
            } else {

                status.addAll(
                        Arrays.asList(Job.Status.values()).stream().map(q -> q.value)
                                .collect(Collectors.toList()));

            }

            jobs = jobRepository.findByStatusInAndTitleContainingOrDescriptionContaining(status, search,
                    PageRequest.of(page - 1, limit, Sort.by(
                            desc.booleanValue() ? Direction.DESC : Direction.ASC,
                            sortBy)));

        }

        Pager<JobView> jobViews = new Pager<>(limit, (int) jobs.getTotalElements(), page);

        jobViews.setResult(jobs.getContent().stream()
                .map(job -> new JobView(job, userRepository.findByUserId(SecurityUtil.getCurrentUserId()).get()))
                .collect(Collectors.toList()));

        return jobViews;
    }

    @Override
    @Transactional
    public void jobCsv(HttpServletResponse httpServletResponse) {

        Collection<JobView> exportlist;

        if (SecurityUtil.isAdmin()) {
            exportlist = jobRepository.findAll().stream().map(JobView::new)
                    .collect(Collectors.toList());
        } else {

            exportlist = jobRepository.findAllByUserUserId(SecurityUtil.getCurrentUserId()).stream().map(JobView::new)
                    .collect(Collectors.toList());

        }

        CsvDownload.download(httpServletResponse, exportlist, "Jobs");

    }

    @Override
    public JobView updateStatus(Integer jobId, Byte status) {

        Job job = jobRepository.findByJobId(jobId).orElseThrow(NotFoundException::new);

        if (status.byteValue() == job.getStatus()) {
            return null;
        }

        if (job.getStatus() == Job.Status.COMPLETED.value && !SecurityUtil.isAdmin()) {
            throw new BadRequestException("Completed Job Cant be Updated");
        }

        if (SecurityUtil.isAdmin() || (SecurityUtil.isEmployer()
                && (status == Job.Status.DELETED.value || status == Job.Status.COMPLETED.value))) {

            job.setStatus(status);

            job.setUpdateDate(new Date());

            /**
             * Send Mail only if User is admin and job is not created by current user
             */
            if (SecurityUtil.isAdmin() && !SecurityUtil.getCurrentUserId().equals(job.getUser().getUserId())) {
                if (status == Job.Status.APPROVED.value) {
                    emailUtil.sendJobStatus(job.getUser().getEmail(), job.getUser().getName(), jobId, job.getTitle(),
                            true);
                } else if (status == Job.Status.DELETED.value) {
                    emailUtil.sendJobStatus(job.getUser().getEmail(), job.getUser().getName(), jobId, job.getTitle(),
                            false);
                }
            }

        } else {
            throw new BadRequestException("Illegal Action");
        }

        return new JobView(jobRepository.save(job));
    }

    @Override
    public void changeSelectedStatus(Collection<Integer> jobIds, Byte status) {
        if (SecurityUtil.isEmployee()) {
            throw illegalAccess();
        }

        for (Integer jobId : jobIds) {

            // if (SecurityUtil.isAdmin() || (SecurityUtil.isEmployer()
            // && (status == Job.Status.DELETED.value || status ==
            // Job.Status.COMPLETED.value))) {

            // jobRepository.findByJobId(jobId).ifPresent(j -> {

            // if (status.byteValue() != j.getStatus()) {
            // j.setStatus(status);
            // j.setUpdateDate(new Date());
            // jobRepository.save(j);
            // }
            // });

            // }

            this.updateStatus(jobId, status);

        }

    }

    @Override
    public Collection<StatusView> getStat() {

        if (SecurityUtil.isAdmin()) {
            return jobRepository.countStatus();
        } else if (SecurityUtil.isEmployer()) {
            return jobRepository.countStatusByUserId(SecurityUtil.getCurrentUserId());
        }

        throw illegalAccess();
    }

    private static BadRequestException illegalAccess() {
        return new BadRequestException("Illegal Access");
    }

}
