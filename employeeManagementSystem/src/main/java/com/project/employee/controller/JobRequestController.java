package com.project.employee.controller;

import java.security.Principal;
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

import com.project.employee.form.JobRequestForm;
import com.project.employee.service.JobRequestService;
import com.project.employee.service.JobService;
import com.project.employee.view.JobRequestView;

@RestController
@RequestMapping("/jobrequest")
public class JobRequestController {
	@Autowired
	private JobService jobService;

	@Autowired
	private JobRequestService jobRequestService;

//		api for creating a job request
	@PostMapping("/{jobId}")
	public JobRequestView add(@PathVariable("jobId") Integer jobId, @Valid @RequestBody JobRequestForm form) {
		return jobRequestService.add(jobId, form);
	}

//		api for changing job request status
	@PutMapping("/{reqId}/{status}")
	public JobRequestView update(@PathVariable("reqId") Integer reqId, @PathVariable("status") Integer status) {
		return jobRequestService.update(reqId, status);
	}

//		api for listing  job requests of current logged in user	
	@GetMapping
	public Collection<JobRequestView> list(Principal p) {
		return jobRequestService.list();
	}

//	api for listing  job requests of current logged in user	
	@GetMapping("/{jobId}")
	public Collection<JobRequestView> listById(@PathVariable("jobId") Integer jobId) {
		return jobRequestService.listById(jobId);
	}

//		api for logically deleting a job request
	@PutMapping("/delete/{reqId}")
	public void delete(@PathVariable("reqId") Integer reqId) {
		jobRequestService.delete(reqId);
	}

}
