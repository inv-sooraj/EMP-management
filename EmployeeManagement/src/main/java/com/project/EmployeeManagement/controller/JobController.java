package com.project.EmployeeManagement.controller;

import java.security.Principal;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.EmployeeManagement.form.JobForm;
import com.project.EmployeeManagement.service.JobService;
import com.project.EmployeeManagement.view.JobDetailView;
import com.project.EmployeeManagement.view.JobView;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public JobView add(@Valid @RequestBody JobForm form, Errors errors) {
        return jobService.addJob(form);
    }

    @GetMapping
    public Collection<JobView> list(Principal p) {
        return jobService.list();
    } 


    @DeleteMapping("/{jobId}")
    public void delete(@PathVariable("jobId")Integer jobId) {
        jobService.delete(jobId);
    }

    @PutMapping("/{jobId}")
    public JobDetailView update(
            @PathVariable("jobId") Integer jobId,
            @Valid @RequestBody JobForm form
    ) {
        return jobService.update(jobId,form);
    }

}
