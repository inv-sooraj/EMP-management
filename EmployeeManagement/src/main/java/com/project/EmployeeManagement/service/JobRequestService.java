package com.project.EmployeeManagement.service;

import com.project.EmployeeManagement.form.JobRequestForm;
import com.project.EmployeeManagement.view.JobRequestView;

public interface JobRequestService {

    JobRequestView addJobRequest(Integer jobId, JobRequestForm form);

    // Collection<User> list();

}
