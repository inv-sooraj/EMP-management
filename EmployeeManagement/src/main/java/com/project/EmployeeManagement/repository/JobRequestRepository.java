package com.project.EmployeeManagement.repository;

import com.project.EmployeeManagement.entity.JobRequest;
import org.springframework.data.repository.Repository;

public interface JobRequestRepository extends Repository<JobRequest, Integer> {

    JobRequest save(JobRequest jobRequest);


}