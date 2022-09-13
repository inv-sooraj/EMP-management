package com.project.EmployeeManagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
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

    @PostMapping
    public JobRequestView addJobRequest(@Valid @RequestBody JobRequestForm form, Errors errors) {
        return jobRequestService.addJobRequest(form);
    }

}
