/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.training.employee.management.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.service.JobRequestService;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

@RestController
@RequestMapping("/job-request")
public class JobRequestController {

    @Autowired
    private JobRequestService jobRequestService;

    @PostMapping("/{jobId}")
    public JobRequestView add(@PathVariable("jobId") Integer jobId, @Valid @RequestBody JobRequestForm form) {
        return jobRequestService.add(form, jobId);
    }

    @GetMapping("/all")
    public Collection<JobRequestView> list() {
        return jobRequestService.list();
    }

}
