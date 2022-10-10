package com.project.EmployeeManagement.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.view.JobView;

public interface JobRepository extends Repository<Job, Integer> {

    Collection<Job> findAll();

    Optional<Job> findById(Integer userId);

    Optional<Job> findByJobIdAndStatus(Integer jobId, Byte status);

    Job save(Job job);

    Optional<Job> findByJobIdAndUserUserIdAndStatus(Integer jobId, Integer userId, Byte status);

    Collection<JobView> findAllByUserUserIdAndStatus(Integer userId, Byte status);

    Optional<Job> findByJobIdAndUserUserId(Integer jobId, Integer userId);

    void delete(Job job);

    Collection<JobView> findAllByStatus(byte status);

    Collection<JobView> findByUserUserIdAndStatus(Integer userId, byte status);

    // Page<Job> find(byte value, String search, PageRequest of);

    // pagination........................................!

    // @Query(value = "SELECT COUNT(*) FROM job as j WHERE j.status in ?1 AND j.openings = ?2 AND(j.job_title like %?3% OR j.job_description like %?3% ) ", nativeQuery = true)
    // Long countJobList(byte status, Integer openings, String search);

    @Query(value = "SELECT * FROM job  WHERE status IN ?1  AND (job_title like %?2% OR job_description like %?2% ) ", nativeQuery = true)
    Page<Job> find(ArrayList<Byte> status, String search, Pageable pageable);

    @Query(value = "SELECT * FROM job WHERE user_id=?1 AND status IN ?2  AND (job_title  LIKE %?3% OR job_description  LIKE %?3% )", nativeQuery = true)
    Page<Job> findAllByUserIdStatus(Integer userId, ArrayList<Byte> status, String search, Pageable page);
    // Page<Job> findById(User userId, String search, Pageable pageable);

    // @Query(value = "SELECT * FROM job WHERE user_id=?1 AND status IN ?2 AND openings>0 AND (job_title  LIKE %?3% OR job_description  LIKE %?3% )", nativeQuery = true)
    // Page<Job> findForUser(Integer userId, ArrayList<Byte> status, String search, Pageable page);

    @Query(value = "SELECT * FROM job  WHERE status IN ?1 AND openings>0 AND (job_title like %?2% OR job_description like %?2% ) ", nativeQuery = true)
    Page<Job> findForUser(ArrayList<Byte> status, String search, Pageable pageable);

    Optional<Job> findByJobId(Long jobId);

    // Streamable<Order> findByJobId(Integer jobId);
    Optional<Job> findByJobId(Integer jobId);

    // ..................................................!

}