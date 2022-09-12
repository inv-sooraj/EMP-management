package com.project.employee.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.employee.entity.JobRequest;
import com.project.employee.exception.NotFoundException;
import com.project.employee.form.JobRequestForm;
import com.project.employee.repository.JobRepository;
import com.project.employee.repository.JobRequestRepository;
import com.project.employee.repository.UserRepository;
import com.project.employee.security.util.SecurityUtil;
import com.project.employee.service.JobRequestService;
import com.project.employee.view.JobRequestView;

@Service
public class JobRequestServiceImpl implements JobRequestService {
	@Autowired
	JobRepository jobRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	JobRequestRepository jobRequestRepository;

	@Override
	public Collection<JobRequestView> list() {
		return jobRequestRepository.findAll().stream().map((jobRequest) -> new JobRequestView(jobRequest)).toList();

	}

	@Override
	public Collection<JobRequestView> listById(Integer jobId) {
		return jobRequestRepository.findAllByJobJobId(jobId).stream()
				.map((jobRequest) -> new JobRequestView(jobRequest)).toList();

	}

	@Override
	public JobRequestView add(Integer jobId, JobRequestForm form) {
		return new JobRequestView(jobRequestRepository.save(
				new JobRequest(jobId, form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()).getUserId())));
	}

	@Override
	public JobRequestView update(Integer reqId, Integer status) {

		JobRequest jobRequest = jobRequestRepository.findById(reqId).orElseThrow(NotFoundException::new);
		jobRequest.update(status);
		jobRequestRepository.save(jobRequest);
		return new JobRequestView(jobRequest);
	}

	@Override
	public void delete(Integer reqId) throws NotFoundException {
		JobRequest jobRequest = jobRequestRepository.findById(reqId).orElseThrow(NotFoundException::new);
		jobRequest.setStatus(JobRequest.Status.INACTIVE.value);
		jobRequestRepository.save(jobRequest);

	}
}
