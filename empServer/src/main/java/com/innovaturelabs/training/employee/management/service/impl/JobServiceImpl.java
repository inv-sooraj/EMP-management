
package com.innovaturelabs.training.employee.management.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobView;
import com.innovaturelabs.training.employee.management.view.StatusView;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public JobView add(JobForm form) {

        if (!SecurityUtil.getCurrentUserRole().equals("ADMIN")
                && !SecurityUtil.getCurrentUserRole().equals("EMPLOYER")) {
            throw new BadRequestException("Illegal Access");
        }

        Byte status = Job.Status.PENDING.value;

        if (SecurityUtil.getCurrentUserRole().equals("ADMIN")) {
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
        return new JobView(jobRepository.findByjobId(jobId).orElseThrow(NotFoundException::new));
    }

    @Override
    public JobView update(JobForm form, Integer jobId) {

        if (!SecurityUtil.getCurrentUserRole().equals("ADMIN")
                && !SecurityUtil.getCurrentUserRole().equals("EMPLOYER")) {
            throw new BadRequestException("Illegal Access");
        }

        return new JobView(jobRepository
                .save(jobRepository.findByjobId(jobId).orElseThrow(BadRequestException::new).update(form)));

    }

    @Override
    public Pager<JobView> list(Integer page, Integer limit, String sortBy, String search, Integer selectedStatus) {

        if (!jobRepository.findColumns().contains(sortBy)) {
            sortBy = "job_id";
        }

        if (page <= 0) {
            page = 1;
        }

        Page<Job> jobs;

        if (SecurityUtil.getCurrentUserRole().equals("EMPLOYEE")) {

            ArrayList<Byte> status = new ArrayList<>();

            // status.add(Job.Status.PENDING.value);
            status.add(Job.Status.APPROVED.value);
            // status.add(Job.Status.COMPLETED.value);

            Byte qualification = userRepository.findByUserId(SecurityUtil.getCurrentUserId())
                    .orElseThrow(NotFoundException::new).getQualification();

            jobs = jobRepository.findAllByStatusAndQualification(status,qualification,
                    search, PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));

        } else if (SecurityUtil.getCurrentUserRole().equals("EMPLOYER")) {
            ArrayList<Byte> status = new ArrayList<>();

            status.add(Job.Status.APPROVED.value);
            status.add(Job.Status.PENDING.value);

            jobs = jobRepository.findAllByUserIdStatus(SecurityUtil.getCurrentUserId(), status,
                    search, PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));

        } else {

            ArrayList<Byte> status = new ArrayList<>();

            if (selectedStatus >= 0 && selectedStatus < 4) {
                status.add(selectedStatus.byteValue());
            } else {

                status.addAll(
                        Arrays.asList(Job.Qualification.values()).stream().map(q -> q.value)
                                .collect(Collectors.toList()));

            }

            jobs = jobRepository.findAllByStatus(status, search,
                    PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));

        }

        Pager<JobView> jobViews = new Pager<>(limit, (int) jobs.getTotalElements(), page);

        // Pager<JobView> jobViews = new
        // Pager<JobView>(limit,jobRepository.countJobList(Job.Status.PENDING.value,
        // search).intValue(),page);

        jobViews.setResult(jobs.getContent().stream().map(JobView::new).collect(Collectors.toList()));

        return jobViews;
    }

    @Override
    public void delete(Integer jobId) {

        if (!SecurityUtil.getCurrentUserRole().equals("ADMIN")
                && !SecurityUtil.getCurrentUserRole().equals("EMPLOYER")) {
            throw new BadRequestException("Illegal Access");
        }

        Job job = jobRepository.findByjobId(jobId).orElseThrow(BadRequestException::new);
        job.setStatus(Job.Status.DELETED.value);
        job.setUpdateDate(new Date());
        jobRepository.save(job);

    }

    @Override
    public void deleteSelected(Collection<Integer> jobIds) {

        if (!SecurityUtil.getCurrentUserRole().equals("ADMIN")
                && !SecurityUtil.getCurrentUserRole().equals("EMPLOYER")) {
            throw new BadRequestException("Illegal Access");
        }

        for (Integer jobId : jobIds) {

            Optional<Job> job = jobRepository.findByjobId(jobId);

            if (job.isPresent()) {
                jobRepository.save(job.get().delete());
            }

        }

    }

    @Override
    @Transactional
    public void jobCsv(HttpServletResponse httpServletResponse) {

        Collection<JobView> exportlist;

        if (!SecurityUtil.getCurrentUserRole().equals("ADMIN")) {
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

        if (SecurityUtil.getCurrentUserRole() == null || SecurityUtil.getCurrentUserRole().equals("EMPLOYEE")) {
            throw new BadRequestException("Illegal Access");
        }

        Job job = jobRepository.findByjobId(jobId).orElseThrow(NotFoundException::new);

        if (SecurityUtil.getCurrentUserRole().equals("ADMIN")) {

            job.setStatus(status);

            job.setUpdateDate(new Date());
        } else if (SecurityUtil.getCurrentUserRole().equals("EMPLOYER") && status == Job.Status.COMPLETED.value) {

            job.setStatus(Job.Status.COMPLETED.value);

            job.setUpdateDate(new Date());

        } else {
            throw new BadRequestException("Illegal Action");
        }

        return new JobView(jobRepository.save(job));
    }

    @Override
    public void changeSelectedStatus(Collection<Integer> jobIds, Byte status) {
        if (!SecurityUtil.getCurrentUserRole().equals("ADMIN")
                && !SecurityUtil.getCurrentUserRole().equals("EMPLOYER")) {
            throw new BadRequestException("Illegal Access");
        }

        for (Integer jobId : jobIds) {

            Optional<Job> job = jobRepository.findByjobId(jobId);

            if (job.isPresent()) {
                Job job2 = job.get();

                if (SecurityUtil.getCurrentUserRole().equals("ADMIN")
                        || (SecurityUtil.getCurrentUserRole().equals("EMPLOYER")
                                && (status == Job.Status.DELETED.value || status == Job.Status.COMPLETED.value))) {
                    job2.setStatus(status);
                    job2.setUpdateDate(new Date());
                }

                jobRepository.save(job2);

            }

        }

    }

    @Override
    public Collection<StatusView> getStat() {

        if (SecurityUtil.getCurrentUserRole().equals("ADMIN")) {

            return jobRepository.countStatus();
        } else if (SecurityUtil.getCurrentUserRole().equals("EMPLOYER")) {
            return jobRepository.countStatusByUserId(SecurityUtil.getCurrentUserId());
        }

        throw new BadRequestException("Illegal");
    }

}
