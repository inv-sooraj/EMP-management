package com.project.employee.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.project.employee.entity.JobRequest;
import com.project.employee.view.JobRequestView;

public interface JobRequestRepository extends Repository<JobRequest, Integer> {
	Collection<JobRequest> findAll();

	Collection<JobRequestView> findAllByStatus(Byte status);

	Collection<JobRequest> findAllByJobJobId(Integer jobId);

	Collection<JobRequest> findAllByUserUserIdAndStatus(Integer userId,Byte Status);

	Optional<JobRequest> findById(Integer reqId);

	JobRequest save(JobRequest jobRequest);

	void delete(JobRequest jobRequest);

	@Query(value = "SELECT `COLUMN_NAME`  FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='job_request'", nativeQuery = true)
	ArrayList<String> findColumns();

	@Query(value = "SELECT * FROM job_request  WHERE job_id IN(select job_id from job where user_id=?1) AND (remark LIKE %?2% )", nativeQuery = true)
	Page<JobRequest> findAllByUserUserId(Integer  userId, String search, Pageable page);
	
//	@Query(value = "SELECT * FROM job_request  WHERE status = ?1 AND (remark LIKE %?2% )", nativeQuery = true)
//	Page<JobRequest> findAllByStatus(Byte status, String search, Pageable page);

	@Query(value = "SELECT COUNT(*) FROM job_request  WHERE status = ?1 AND (remark	 LIKE %?2% )", nativeQuery = true)
	Long countJobRequetList(Byte status, String search);

	@Query(value = "SELECT COUNT(*) FROM job_request  WHERE status = ?1 ", nativeQuery = true)
	Long countJobRequest(Byte status);
}
