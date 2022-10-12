package com.project.EmployeeManagement.service;

import java.util.Collection;

import com.project.EmployeeManagement.form.JobRequestForm;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.view.JobRequestView;

public interface JobRequestService {

    JobRequestView addJobRequest(Integer jobId);

    // Collection<JobRequestView> list();

    Pager<JobRequestView> listItem(String search, String limit, String sort,Boolean desc, String page);

    JobRequestView edit(Integer jobRequestId, JobRequestForm form);

    Collection<Integer> appliedJobs();

    // Collection<User> list();

}
