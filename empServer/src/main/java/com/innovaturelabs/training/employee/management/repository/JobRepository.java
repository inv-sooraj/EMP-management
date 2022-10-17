
package com.innovaturelabs.training.employee.management.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.view.StatusView;

public interface JobRepository extends Repository<Job, Integer> {

    Optional<Job> findByJobId(Integer jobId);

    Optional<Job> findByJobIdAndUserUserId(Integer jobId, Integer userId);

    Optional<Job> findByJobIdAndStatus(Integer jobId, Byte status);

    Job save(Job job);

    // Collection<Job> saveAll(Iterable<Job> jobs);

    Collection<Job> findAll();

    Collection<Job> findAllByUserUserId(Integer userId);

    // @Query(value = "SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS`  WHERE `TABLE_NAME`='job_tbl'", nativeQuery = true)
    // ArrayList<String> findColumns();

    @Query(value = "SELECT job FROM com.innovaturelabs.training.employee.management.entity.Job job WHERE status IN ?1 AND (title LIKE %?2% OR description LIKE %?2% )")
    Page<Job> findByStatusInAndTitleContainingOrDescriptionContaining(Collection<Byte> status, String search ,Pageable page);

    @Query(value = "SELECT job FROM com.innovaturelabs.training.employee.management.entity.Job job WHERE openings > 0 AND status IN ?1 AND (title LIKE %?2% OR description LIKE %?2% )")
    Page<Job> findAllByStatusAndOpenings(byte[] status, String search, Pageable page);

    @Query(value = "SELECT job FROM com.innovaturelabs.training.employee.management.entity.Job job WHERE user_id=?1 AND status IN ?2 AND (title LIKE %?3% OR description LIKE %?3% )")
    Page<Job> findAllByUserIdStatus(Integer userId, byte[] status, String search, Pageable page);

    // @Query(value = "SELECT status, COUNT(*) AS count FROM job_tbl GROUP BY status;", nativeQuery = true)
    @Query(value = "SELECT job.status AS status, COUNT(*) AS count FROM com.innovaturelabs.training.employee.management.entity.Job job GROUP BY status")
    Collection<StatusView> countStatus();

    // @Query(value = "SELECT status, COUNT(*) AS count FROM job_tbl WHERE user_id=?1 GROUP BY status;", nativeQuery = true)
    @Query(value = "SELECT job.status AS status, COUNT(*) AS count FROM com.innovaturelabs.training.employee.management.entity.Job job WHERE job.user.userId=?1 GROUP BY job.status")
    Collection<StatusView> countStatusByUserId(Integer userId);

}
