
package com.innovaturelabs.training.employee.management.service;

import java.util.Collection;

import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

public interface JobRequestService {

    JobRequestView add(JobRequestForm form,Integer jobId);

    Collection<JobRequestView> list();

}
