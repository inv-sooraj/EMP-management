
package com.innovaturelabs.training.employee.management.repository;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.JobRequest;

public interface JobRequestRepository extends Repository<JobRequest, Integer> {

    JobRequest save(JobRequest jobRequest);

    Collection<JobRequest> findAll();

    @Query(value = "SELECT * FROM jobrequest_tbl  WHERE status = ?1 AND (feedback LIKE %?2% OR remark LIKE %?2%)", nativeQuery = true)
    Page<JobRequest> findAllByStatus(byte status, String search, Pageable page);

    @Query(value = "SELECT `COLUMN_NAME`  FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='jobrequest_tbl'", nativeQuery = true)
    ArrayList<String> findColumns();

}
