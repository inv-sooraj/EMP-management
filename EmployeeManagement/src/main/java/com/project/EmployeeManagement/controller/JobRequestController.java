package com.project.EmployeeManagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.EmployeeManagement.form.JobRequestForm;
import com.project.EmployeeManagement.service.JobRequestService;
import com.project.EmployeeManagement.view.JobRequestView;

@RestController
@RequestMapping("/jobrequest")
public class JobRequestController {

    @Autowired
    private JobRequestService jobRequestService;

    @PostMapping("/{jobId}")
    public JobRequestView addJobRequest(
            @PathVariable("jobId") Integer jobId,
            @Valid @RequestBody JobRequestForm form
    ) {
        return jobRequestService.addJobRequest(jobId,form);
    }

}
