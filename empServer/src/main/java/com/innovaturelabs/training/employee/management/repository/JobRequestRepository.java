
package com.innovaturelabs.training.employee.management.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.JobRequest;

public interface JobRequestRepository extends Repository<JobRequest, Integer> {

    JobRequest save(JobRequest jobRequest);

    Collection<JobRequest> findAll();

    Optional<JobRequest> findByUserUserIdAndJobJobId(Integer userId, Integer jobId);

    Optional<JobRequest> findByJobRequestId(Integer jobRequestId);

    // @Query(value = "SELECT * FROM job_request_tbl  WHERE status IN ?1 AND  remark LIKE %?2%", nativeQuery = true)
    // Page<JobRequest> findAllByStatus(byte[] status, String search, Pageable page);

    // @Query(value = "SELECT * FROM job_request_tbl  WHERE job_id IN (select job_id from job_tbl where user_id=?1) AND status IN ?2 AND  remark LIKE %?3%", nativeQuery = true)
    Page<JobRequest> findByJobUserUserIdAndStatusInAndRemarkContaining(Integer userId, byte[] status, String search, Pageable page);

    // @Query(value = "SELECT * FROM job_request_tbl  WHERE user_id=?1 AND status IN ?2 AND  remark LIKE %?3%", nativeQuery = true)
    Page<JobRequest> findAllByUserUserIdAndStatusInAndRemarkContaining(Integer userId, byte[] status, String search, Pageable page);

    // @Query(value = "SELECT `COLUMN_NAME`  FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='job_request_tbl'", nativeQuery = true)
    // ArrayList<String> findColumns();

    Collection<JobRequest> findAllByUserUserId(Integer userId);

    // @Query(value = "SELECT job_id FROM job_request_tbl  WHERE user_id=?1", nativeQuery = true)
    @Query(value = "SELECT jobRequest.job.jobId FROM com.innovaturelabs.training.employee.management.entity.JobRequest jobRequest  WHERE jobRequest.user.userId=?1")
    Collection<Integer> getAppliedJobs(Integer userId);

}
