
package com.innovaturelabs.training.employee.management.service;

import javax.servlet.http.HttpServletResponse;

import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

public interface JobRequestService {

    JobRequestView add(JobRequestForm form, Integer jobId);

    // Collection<JobRequestView> list();

    Pager<JobRequestView> list(Integer page, Integer limit, String sortBy, String search);

    void jobRequestCsv(HttpServletResponse httpServletResponse);

}
