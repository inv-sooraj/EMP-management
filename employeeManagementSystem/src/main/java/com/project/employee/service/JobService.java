package com.project.employee.service;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.project.employee.exception.NotFoundException;
import com.project.employee.form.JobForm;
import com.project.employee.view.JobView;
@Service
public interface JobService {
	Collection<JobView> list();

	JobView add(@Valid JobForm form);
	
	JobView update(Integer jobId, JobForm form);
	
	void csvJob(HttpServletResponse httpServletResponse);
	
	void delete(Integer jobId) throws NotFoundException;
}
