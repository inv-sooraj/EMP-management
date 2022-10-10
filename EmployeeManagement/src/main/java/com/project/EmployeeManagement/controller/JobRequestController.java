package com.project.EmployeeManagement.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.EmployeeManagement.form.JobRequestForm;
import com.project.EmployeeManagement.service.JobRequestService;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.view.JobRequestView;

@RestController
@RequestMapping("/jobrequest")
public class JobRequestController {

    @Autowired
    private JobRequestService jobRequestService;

    @PostMapping
    public JobRequestView addJobRequest(

            @RequestBody Integer jobId) {
        return jobRequestService.addJobRequest(jobId);
    }

    // change status
    @PutMapping("/confirm/{jobReqId}")
    public JobRequestView edit(
            @PathVariable("jobReqId") Integer jobRequestId,
            @RequestBody JobRequestForm form) {
        return jobRequestService.edit(jobRequestId, form);
    }

    // for pagination.................

    @GetMapping("/getjobrequest")
    public Pager<JobRequestView> list(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "limit", required = false, defaultValue = "16") String limit,
            @RequestParam(value = "sort", required = false, defaultValue = "update_date") String sort

    ) {

        return jobRequestService.listItem(search, limit, sort, page);
    }


    @GetMapping("/applied")
    public Collection<Integer> appliedJobs() {
        return jobRequestService.appliedJobs();
    }

    // ...............................................................

}
