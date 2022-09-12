/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.training.employee.management.service;

import java.util.Collection;

import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

public interface JobRequestService {

    JobRequestView add(JobRequestForm form,Integer jobId);

    Collection<JobRequestView> list();

}
