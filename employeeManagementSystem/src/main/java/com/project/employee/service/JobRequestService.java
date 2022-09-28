package com.project.employee.service;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.project.employee.exception.NotFoundException;
import com.project.employee.features.Pager;
import com.project.employee.form.JobRequestForm;
import com.project.employee.view.JobRequestView;

@Service
public interface JobRequestService {
	Collection<JobRequestView> list();

	Collection<JobRequestView> listByUserId();

	Pager<JobRequestView> list(Integer page, Integer limit, String sortBy, String search);

	JobRequestView add(Integer jobId, @Valid JobRequestForm form);

	long jobRequestCount();

	JobRequestView update(Integer reqId, Integer status) throws NotFoundException;

	void csvReq(HttpServletResponse httpServletResponse);

	void delete(Integer reqId) throws NotFoundException;

	void deleteSelected(Collection<Integer> jobIds);

}
