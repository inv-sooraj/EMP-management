
package com.innovaturelabs.training.employee.management.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.view.JobView;

public interface JobRepository extends Repository<Job, Integer> {

    Optional<Job> findByjobId(Integer jobId);

    Job save(Job job);

    Collection<Job> findAll();

    Page<JobView> findAllByStatus(Byte status, Pageable page);

}
