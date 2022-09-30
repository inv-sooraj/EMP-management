package com.project.EmployeeManagement.service;

import com.project.EmployeeManagement.form.JobRequestForm;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.view.JobRequestView;

public interface JobRequestService {

    JobRequestView addJobRequest(Integer jobId);

    // Collection<JobRequestView> list();

    Pager<JobRequestView> listItem(String search, String limit, String sort, String page);

    JobRequestView edit(Integer jobRequestId, JobRequestForm form);

    // Collection<User> list();

}
