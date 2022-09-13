package com.project.EmployeeManagement.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.view.JobView;

public interface JobRepository extends Repository<Job,Integer>{

    Collection<Job> findAll();
    
    Optional<Job> findById(Integer userId);
    Job save(Job job);

    Optional<Job> findByJobIdAndUserUserIdAndStatus(Integer jobId, Integer userId,Byte status);
    Collection<JobView> findAllByUserUserIdAndStatus(Integer userId,Byte status);

    Optional<Job>findByJobIdAndUserUserId(Integer jobId,Integer userId);
    void delete(Job job);
    
    


    
}