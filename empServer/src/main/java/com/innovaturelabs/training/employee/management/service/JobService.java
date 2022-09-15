
package com.innovaturelabs.training.employee.management.service;

import javax.servlet.http.HttpServletResponse;

import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobView;

public interface JobService {

    JobView add(JobForm form);

    JobView update(JobForm form,Integer jobId);

    void delete(Integer jobId);

    Pager<JobView> list(Integer page,Integer limit,String sortBy,String search);

    void jobCsv(HttpServletResponse httpServletResponse);

    

}
