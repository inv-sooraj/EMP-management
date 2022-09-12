package com.project.employee.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.employee.entity.Job;
import com.project.employee.exception.NotFoundException;
import com.project.employee.form.JobForm;
import com.project.employee.repository.JobRepository;
import com.project.employee.repository.UserRepository;
import com.project.employee.security.util.SecurityUtil;
import com.project.employee.service.JobService;
import com.project.employee.view.JobView;

@Service
public class JobServiceImpl implements JobService {
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Collection<JobView> list() {
		return jobRepository.findAll();
	}

	@Override
	public JobView add(JobForm form) {
		return new JobView(
				jobRepository.save(new Job(form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()))));
	}

	@Override
	@Transactional
	public JobView update(Integer jobId, JobForm form) {
		return jobRepository
				.findByJobIdAndUserUserIdAndStatus(jobId, SecurityUtil.getCurrentUserId(), Job.Status.ACTIVE.value)
				.map((job) -> {
					return new JobView(jobRepository.save(job.update(form)));
				}).orElseThrow(NotFoundException::new);
	}

	@Override
	@Transactional
	public void delete(Integer jobId) {
		Job job = jobRepository.findByJobIdAndStatus(jobId, Job.Status.ACTIVE.value)
				.orElseThrow(NotFoundException::new);

		job.setStatus(Job.Status.INACTIVE.value);

		jobRepository.save(job);

		return;
	}
}
