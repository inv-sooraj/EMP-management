/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.training.employee.management.service;

import org.springframework.data.domain.Page;

import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.view.JobView;

public interface JobService {

    JobView add(JobForm form);

    JobView update(JobForm form,Integer jobId);

    void delete(Integer jobId);

    Page<JobView> list(Integer page,String sortBy);

    

}
