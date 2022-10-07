package com.project.employee.controller;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.mail.MessagingException;
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

import com.project.employee.exception.NotFoundException;
import com.project.employee.features.Pager;
import com.project.employee.form.JobRequestForm;
import com.project.employee.service.JobRequestService;
import com.project.employee.view.JobRequestView;

@RestController
@RequestMapping("/jobrequest")
public class JobRequestController {

	@Autowired
	private JobRequestService jobRequestService;

//	api for creating a job request
	@PostMapping("/{jobId}")
	public JobRequestView add(@PathVariable("jobId") Integer jobId, @Valid @RequestBody JobRequestForm form) {
		return jobRequestService.add(jobId, form);
	}

//	api for changing job request status
	@PutMapping("/{reqId}")
	public JobRequestView update(@PathVariable("reqId") Integer reqId, @RequestBody Integer status)
			throws NotFoundException, UnsupportedEncodingException, MessagingException {
		return jobRequestService.update(reqId, status);
	}

//	api for paginated listing  job requests	
	@GetMapping
	public Pager<JobRequestView> list(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(name = "sortBy", required = false, defaultValue = "job_id") String sortBy,
			@RequestParam(name = "desc", required = false, defaultValue = "false") Boolean desc,
			@RequestParam(name = "filter", required = false, defaultValue = "") String filter,
			@RequestParam(name = "search", required = false, defaultValue = "") String search) {
		return jobRequestService.list(page, limit, sortBy, desc, filter, search);
	}

//	fetch total req count
	@GetMapping("/count")
	public long jobRequestCount() {
		return jobRequestService.jobRequestCount();
	}

//	api for listing  requestids of current logged in user	
	@GetMapping("/reqIds")
	public Collection<Integer> listByUserId() {
		return jobRequestService.listByUserId();
	}

//	for listing job requests of current logged in user
	@GetMapping("/emp")
	public Collection<JobRequestView> reqlistById() {
		return jobRequestService.reqlistById();
	}

// csv download of job requests
	@GetMapping("/download")
	public void csvReq(HttpServletResponse httpServletResponse) {
		jobRequestService.csvReq(httpServletResponse);
	}

//	api for logically deleting a job request
	@PutMapping("/delete")
	public void delete(@RequestBody Integer reqId) {
		jobRequestService.delete(reqId);
	}

//	logical deletion of multiple jobs
	@PutMapping("/delete/selected")
	public void deleteSelected(@RequestBody Collection<Integer> reqIds) {
		jobRequestService.deleteSelected(reqIds);
	}

}
