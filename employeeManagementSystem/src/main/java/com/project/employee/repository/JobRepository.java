package com.project.employee.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.project.employee.entity.Job;
import com.project.employee.view.JobView;

public interface JobRepository  extends Repository<Job, Integer>{
	Collection<JobView> findAll();
	
	Optional<JobView> findByJobId(Integer jobId);

	Optional<Job> findByJobIdAndStatus(Integer jobId,byte status);
	
	Optional<Job> findByJobIdAndUserUserIdAndStatus(Integer jobId,Integer userId,byte status);
	
	Job save(Job job);
}
	