package com.project.employee.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.project.employee.entity.Job;
import com.project.employee.view.JobView;

public interface JobRepository extends Repository<Job, Integer> {
	Collection<Job> findAll();

	Collection<JobView> findAllByStatus(byte status);

	Optional<JobView> findByJobId(Integer jobId);

	Optional<Job> findById(Integer jobId);

	Optional<Job> findByJobIdAndStatus(Integer jobId, byte status);

	Optional<Job> findByJobIdAndUserUserIdAndStatus(Integer jobId, Integer userId, byte status);

	// SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE
	// `TABLE_NAME`='user_tbl'
	@Query(value = "SELECT `COLUMN_NAME`  FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='job'", nativeQuery = true)
	ArrayList<String> findColumns();

	@Query(value = "SELECT * FROM job  WHERE status = ?1 AND (title LIKE %?2% OR description LIKE %?2% OR update_date LIKE %?2%  )", nativeQuery = true)
	Page<Job> findAllByStatus(Byte status, String search, Pageable page);

	@Query(value = "SELECT COUNT(*) FROM job  WHERE status = ?1 AND (title LIKE %?2% OR description LIKE %?2% OR update_date LIKE %?2%)", nativeQuery = true)
	Long countJobList(Byte status, String search);

	@Query(value = "SELECT COUNT(*) FROM job  WHERE status = ?1 ", nativeQuery = true)
	Long countJobs(Byte status);

	Job save(Job job);
}
