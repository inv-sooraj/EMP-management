package com.project.EmployeeManagement.service;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.JobForm;
import com.project.EmployeeManagement.util.Pager;
// import com.project.EmployeeManagement.security.util.Pager;
import com.project.EmployeeManagement.view.JobDetailView;
import com.project.EmployeeManagement.view.JobView;

public interface JobService {

    Collection<JobView> list();

    JobView addJob(JobForm form);
    void delete(Integer jobId) throws NotFoundException;
    
     JobDetailView update(Integer jobId,JobForm form)throws NotFoundException;

    public Pager<JobDetailView> listItem(String search, String limit, String sort, String page);

    void jobCsv(HttpServletResponse httpServletResponse);

    JobDetailView get(Integer jobId);
        
    
    
}
