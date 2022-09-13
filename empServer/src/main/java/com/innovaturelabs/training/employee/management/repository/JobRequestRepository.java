
package com.innovaturelabs.training.employee.management.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.JobRequest;

public interface JobRequestRepository extends CrudRepository<JobRequest, Integer> {

    JobRequest save(JobRequest jobRequest);

    Collection<JobRequest> findAll();

}
