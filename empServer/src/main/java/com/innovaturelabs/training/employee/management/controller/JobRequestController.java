
package com.innovaturelabs.training.employee.management.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.service.JobRequestService;
import com.innovaturelabs.training.employee.management.util.Pager;
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

    @GetMapping("/page")
    public Pager<JobRequestView> list(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "job_request_id") String sortBy,
            @RequestParam(name = "search", defaultValue = "") String search) {
        return jobRequestService.list(page, limit, sortBy, search);
    }

    @GetMapping("/download")
    public void jobRequestCsv(HttpServletResponse httpServletResponse) {
        jobRequestService.jobRequestCsv(httpServletResponse);
    }

}
