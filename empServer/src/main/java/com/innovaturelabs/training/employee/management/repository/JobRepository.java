
package com.innovaturelabs.training.employee.management.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.Job;

public interface JobRepository extends Repository<Job, Integer> {

    Optional<Job> findByjobId(Integer jobId);

    // Optional<Job> findByjobIdAndStaus(Integer jobId,Byte status);

    Job save(Job job);

    // Collection<Job> saveAll(Iterable<Job> jobs);

    Collection<Job> findAll();

    @Query(value = "SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='job_tbl'", nativeQuery = true)
    ArrayList<String> findColumns();

    @Query(value = "SELECT * FROM job_tbl WHERE status = ?1 AND (title LIKE %?2% OR description LIKE %?2% )", nativeQuery = true)
    Page<Job> findAllByStatus(Byte status, String search, Pageable page);

    @Query(value = "SELECT COUNT(*) FROM job_tbl WHERE status = ?1 AND (title LIKE %?2% OR description LIKE %?2% )", nativeQuery = true)
    Long countJobList(Byte status, String search);

}
