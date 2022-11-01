
package com.innovaturelabs.training.employee.management.service;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

public interface JobRequestService {

    JobRequestView add(Integer jobId);

    JobRequestView update(Integer jobRequestId, JobRequestForm status);

    Collection<Integer> appliedJobs();

    Pager<JobRequestView> list(Integer page, Integer limit, String sortBy, String search, Boolean desc,Integer jobId);

    void jobRequestCsv(HttpServletResponse httpServletResponse);

}
