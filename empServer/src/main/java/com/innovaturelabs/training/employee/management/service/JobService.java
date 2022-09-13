
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
