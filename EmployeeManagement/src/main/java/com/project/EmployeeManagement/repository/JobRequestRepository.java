package com.project.EmployeeManagement.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.project.EmployeeManagement.entity.JobRequest;

public interface JobRequestRepository extends Repository<JobRequest, Integer> {

    JobRequest save(JobRequest jobRequest);

    Optional<JobRequest> findById(Integer jobReqId);


}