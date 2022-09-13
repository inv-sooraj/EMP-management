
package com.innovaturelabs.training.employee.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.repository.JobRepository;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.JobService;
import com.innovaturelabs.training.employee.management.view.JobView;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public JobView add(JobForm form ) {

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
    public Page<JobView> list(Integer page, String sortBy) {

        return jobRepository.findAllByStatus(Job.Status.PENDING.value,
                PageRequest.of(page, 2, Sort.by(sortBy).ascending()));
    }

    @Override
    public void delete(Integer jobId) {

        Job job = jobRepository.findByjobId(jobId).orElseThrow(BadRequestException::new);
        job.setStatus(Job.Status.DELETED.value);
        jobRepository.save(job);

    }

}
