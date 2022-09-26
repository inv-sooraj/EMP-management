package com.project.employee.service;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.project.employee.exception.NotFoundException;
import com.project.employee.features.Pager;
import com.project.employee.form.JobForm;
import com.project.employee.view.JobView;

@Service
public interface JobService {
	Collection<JobView> list();

	Pager<JobView> list(Integer page, Integer limit, String sortBy, String search);

	JobView add(@Valid JobForm form);

	JobView getJob(Integer jobId);

	JobView update(Integer jobId, JobForm form);

	long jobCount();
	
	void deleteSelected(Collection<Integer> jobIds);

	void csvJob(HttpServletResponse httpServletResponse);

	void delete(Integer jobId) throws NotFoundException;
}
