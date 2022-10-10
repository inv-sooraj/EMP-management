package com.project.EmployeeManagement.controller;

import java.security.Principal;
import java.util.Collection;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.EmployeeManagement.form.JobForm;
import com.project.EmployeeManagement.service.JobService;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.view.JobDetailView;
import com.project.EmployeeManagement.view.JobView;
import javax.servlet.http.HttpServletResponse;

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

    @GetMapping("/{jobId}")
    public JobDetailView get(@PathVariable("jobId") Integer jobId) {
        return jobService.get(jobId);
    }

    @PutMapping("/delete/{jobId}")
    public void delete(@PathVariable("jobId") Integer jobId, @RequestBody Integer flag) {
        jobService.delete(jobId, flag);
    }

    @PutMapping("/{jobId}")
    public JobDetailView update(
            @PathVariable("jobId") Integer jobId,
            @Valid @RequestBody JobForm form) {
        return jobService.update(jobId, form);
    }

    // for pagination.................

    @GetMapping("/getjob")
    public Pager<JobDetailView> list(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "limit", required = false, defaultValue = "16") String limit,
            @RequestParam(value = "sort", required = false, defaultValue = "update_date") String sort,
            @RequestParam(value = "filter", required = false, defaultValue = "ALL") String filter
  
    ) {

        return jobService.listItem(search, limit, sort, page, filter);
    }

    // csv download................

    @GetMapping("/download")
    public void jobCsv(HttpServletResponse httpServletResponse) {
        jobService.jobCsv(httpServletResponse);
    }

    @PutMapping("/deleteall")
    public void deleteAll(@RequestBody Collection<Integer> Ids) {
        jobService.deleteAll(Ids);
    }

}
