package com.project.employee.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.employee.exception.NotFoundException;
import com.project.employee.form.JobForm;
import com.project.employee.service.JobService;
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

//	api for listing  all jobs
	@GetMapping
	public Collection<JobView> list() {
		return jobService.list();
	}

//	api for logicaly deleting a job
	@PutMapping("/{jobId}")
	public JobView update(@PathVariable("jobId") Integer jobId, @Valid @RequestBody JobForm form) {
		return jobService.update(jobId, form);
	}
	
//	api for logically deleting a job 
@PutMapping("/delete/{jobId}")
public void delete(@PathVariable("jobId") Integer jobId) {
	jobService.delete(jobId);
}

}
