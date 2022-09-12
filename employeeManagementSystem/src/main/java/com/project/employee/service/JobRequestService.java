package com.project.employee.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.project.employee.exception.NotFoundException;
import com.project.employee.form.JobRequestForm;
import com.project.employee.view.JobRequestView;

@Service
public interface JobRequestService {
	Collection<JobRequestView> list();
	
	Collection<JobRequestView> listById(Integer jobId);

	JobRequestView add(Integer jobId,@Valid JobRequestForm form);

	JobRequestView update(Integer reqId, Integer status) throws NotFoundException;
	
	void delete(Integer reqId) throws NotFoundException;
}
	