package com.project.EmployeeManagement.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.project.EmployeeManagement.entity.JobRequest;
import com.project.EmployeeManagement.view.JobRequestView;

public interface JobRequestRepository extends Repository<JobRequest, Integer> {

    JobRequest save(JobRequest jobRequest);

    Optional<JobRequest> findById(Integer jobReqId);

    Collection<JobRequestView> findAllByStatus(byte value);

    Collection<JobRequestView> findByUserUserIdAndStatus(Integer currentUserId, byte value);

    @Query(value = "SELECT * FROM job_request  WHERE job_id IN (select job_id from job where user_id=?1) AND (remarks LIKE %?2% )", nativeQuery = true)
    Page<JobRequest> findAllByUserUserId(Integer userId, String search, Pageable page);
    
    @Query(value = "SELECT * FROM job_request  WHERE  user_id=?1 AND (remarks LIKE %?2% )", nativeQuery = true)
    Page<JobRequest> findByJobReqId(Integer userId, String search, PageRequest of);

}