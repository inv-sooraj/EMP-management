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
import com.project.employee.form.JobRequestForm;
import com.project.employee.service.JobRequestService;
import com.project.employee.view.JobRequestView;

@RestController
@RequestMapping("/jobrequest")
public class JobRequestController {

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
	public Pager<JobRequestView> list(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = false, defaultValue = "job_id") String sortBy,
			@RequestParam(name = "search", required = false, defaultValue = "") String search) {
		return jobRequestService.list(page, limit, sortBy, search);
	}

	@GetMapping("/count")
	public long jobRequestCount() {
		return jobRequestService.jobRequestCount();
	}

//	api for listing  job requests of current logged in user	
	@GetMapping("/{jobId}")
	public Collection<JobRequestView> listById(@PathVariable("jobId") Integer jobId) {
		return jobRequestService.listById(jobId);
	}

	@GetMapping("/download")
	public void csvReq(HttpServletResponse httpServletResponse) {
		jobRequestService.csvReq(httpServletResponse);
	}

//		api for logically deleting a job request
	@PutMapping("/delete")
	public void delete(@RequestBody Integer reqId) {
		jobRequestService.delete(reqId);
	}
	
	@PutMapping("/delete/selected")
    public void deleteSelected(@RequestBody Collection<Integer> reqIds) {
        jobRequestService.deleteSelected(reqIds);
    }

}
