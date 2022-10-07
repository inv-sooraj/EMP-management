package com.project.employee.controller;

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

import com.project.employee.features.Pager;
import com.project.employee.form.JobForm;
import com.project.employee.service.JobService;
import com.project.employee.view.JobRequestView;
import com.project.employee.view.JobView;

@RestController
@RequestMapping("/job")
public class JobController {
	@Autowired
	private JobService jobService;

//	api creating a job
	@PostMapping
	public JobView add(@Valid @RequestBody JobForm form) {
		return jobService.add(form);
	}

//	api for paginated listing  of all jobs
	@GetMapping
	public Pager<JobView> list(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = false, defaultValue = "job_id") String sortBy,
			@RequestParam(name = "desc", required = false,	defaultValue = "false") Boolean desc,
			@RequestParam(name = "filter", required = false, defaultValue = "") String filter,
			@RequestParam(name = "search", required = false, defaultValue = "") String search) {
		return jobService.list(page, limit, sortBy, desc, filter,search);
	}

//	api for downloading csv file of jobs
	@GetMapping("/download")
	public void csvJob(HttpServletResponse httpServletResponse) {
		jobService.csvJob(httpServletResponse);
	}

	
//	api for fetching job count
	@GetMapping("/count")
	public long jobCount() {
		return jobService.jobCount();
	}

//	api for fetching details of a job by id
	@GetMapping("/{jobId}")
	public JobView getJob(@PathVariable("jobId") Integer jobId) {
		return jobService.getJob(jobId);
	}

//	for editing job details
	@PutMapping("/{jobId}")
	public JobView update(@PathVariable("jobId") Integer jobId, @Valid @RequestBody JobForm form) {
		return jobService.update(jobId, form);
	}
	
//	for approving/rejecing a job
	@PutMapping("/approve/{jobId}")
	public JobView approve(@PathVariable("jobId") Integer jobId, @RequestBody Integer status) {
		return jobService.approve(jobId, status);
	}
	
//	api for logically deleting a job 
	@PutMapping("/delete")
	public void delete(@RequestBody Integer jobId) {
		jobService.delete(jobId);
	}
	
//	logical deletion of multiple jobs
	@PutMapping("/delete/selected")
	public void deleteSelected(@RequestBody Collection<Integer> jobIds) {
		jobService.deleteSelected(jobIds);
	}



}
