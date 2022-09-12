package com.project.employee.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.project.employee.entity.JobRequest;

public interface JobRequestRepository extends Repository<JobRequest, Integer> {
	Collection<JobRequest> findAll();
	
	Collection<JobRequest> findAllByJobJobId(Integer jobId);
	
	Collection<JobRequest>findAllByUserIdAndStatus(Integer userId,byte status);

	Optional<JobRequest> findById(Integer reqId);

	JobRequest save(JobRequest jobRequest);

	void delete(JobRequest jobRequest);
}
	