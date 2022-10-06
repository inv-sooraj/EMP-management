
package com.innovaturelabs.training.employee.management.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.service.JobService;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobView;
import com.innovaturelabs.training.employee.management.view.StatusView;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public JobView add(@Valid @RequestBody JobForm form) {
        return jobService.add(form);
    }

    @GetMapping("/page")
    public Pager<JobView> list(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "job_id") String sortBy,
            @RequestParam(name = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(name = "filter", defaultValue = "5") Integer data,
            @RequestParam(name = "search", defaultValue = "") String search) {
        return jobService.list(page, limit, sortBy,desc, search, data);
    }

    @GetMapping("/{jobId}")
    public JobView getJob(@PathVariable("jobId") Integer jobId) {
        return jobService.getJob(jobId);
    }

    @PutMapping("/{jobId}")
    public JobView update(@PathVariable("jobId") Integer jobId, @Valid @RequestBody JobForm form) {
        return jobService.update(form, jobId);
    }

    @PutMapping("/status/{jobId}")
    public JobView updateStatus(@PathVariable("jobId") Integer jobId, @Valid @RequestBody Byte status) {
        return jobService.updateStatus(jobId, status);
    }

    @PutMapping("/status/selected/{status}")
    public void changeSelectedStatus(@RequestBody Collection<Integer> jobIds,
            @PathVariable(name = "status") Byte status) {
        jobService.changeSelectedStatus(jobIds, status);
    }

    @PutMapping("/delete/{jobId}")
    public void delete(@PathVariable("jobId") Integer jobId) {
        jobService.delete(jobId);
    }

    @PutMapping("/delete/selected")
    public void deleteSelected(@RequestBody Collection<Integer> jobIds) {
        jobService.deleteSelected(jobIds);
    }

    @GetMapping("/download")
    public void jobCsv(HttpServletResponse httpServletResponse) {
        jobService.jobCsv(httpServletResponse);
    }

    @GetMapping("/stat")
    public Collection<StatusView> count() {

        return jobService.getStat();
    }
}
