
package com.innovaturelabs.training.employee.management.service.impl;

import java.util.Collection;
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
import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.repository.JobRepository;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.JobService;
import com.innovaturelabs.training.employee.management.util.CsvDownload;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobView;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public JobView add(JobForm form) {

        return new JobView(jobRepository.save(new Job(form.getTitle(),
                form.getDescription(),
                form.getQualification(),
                form.getOpenings(),
                SecurityUtil.getCurrentUserId(),
                Job.Status.PENDING.value)));

    }

    @Override
    public JobView update(JobForm form, Integer jobId) {

        return new JobView(jobRepository
                .save(jobRepository.findByjobId(jobId).orElseThrow(BadRequestException::new).update(form)));

    }

    @Override
    public Pager<JobView> list(Integer page, Integer limit, String sortBy, String search) {

        if (!jobRepository.findColumns().contains(sortBy)) {
            sortBy = "job_id";
        }

        if (page <= 0) {
            page = 1;
        }

        Page<Job> jobs = jobRepository.findAllByStatus(Job.Status.PENDING.value, search,
                PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));

        Pager<JobView> jobViews = new Pager<>(limit, (int) jobs.getTotalElements(), page);

        // Pager<JobView> jobViews = new
        // Pager<JobView>(limit,jobRepository.countJobList(Job.Status.PENDING.value,
        // search).intValue(),page);

        jobViews.setResult(jobs.getContent().stream().map(JobView::new).collect(Collectors.toList()));

        return jobViews;
    }

    @Override
    public void delete(Integer jobId) {

        Job job = jobRepository.findByjobId(jobId).orElseThrow(BadRequestException::new);
        job.setStatus(Job.Status.DELETED.value);
        jobRepository.save(job);

    }

    @Override
    @Transactional
    public void jobCsv(HttpServletResponse httpServletResponse) {
        Collection<JobView> exportlist = jobRepository.findAll().stream().map(JobView::new)
                .collect(Collectors.toList());

        CsvDownload.download(httpServletResponse, exportlist, "Jobs");

    }

}
