package com.project.EmployeeManagement.service;

import java.util.Collection;

import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.JobForm;
import com.project.EmployeeManagement.view.JobDetailView;
import com.project.EmployeeManagement.view.JobView;

public interface JobService {

    Collection<JobView> list();

    JobView addJob(JobForm form);
    void delete(Integer jobId) throws NotFoundException;
    
     JobDetailView update(Integer jobId,JobForm form)throws NotFoundException;
         
    
    
}
