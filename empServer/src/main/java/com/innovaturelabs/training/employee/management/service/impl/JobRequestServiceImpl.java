
package com.innovaturelabs.training.employee.management.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.innovaturelabs.training.employee.management.entity.JobRequest;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.repository.JobRequestRepository;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.JobRequestService;
import com.innovaturelabs.training.employee.management.util.CsvDownload;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobRequestView;

@Service
public class JobRequestServiceImpl implements JobRequestService {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Override
    public JobRequestView add(JobRequestForm form, Integer jobId) {

        return new JobRequestView(
                jobRequestRepository.save(new JobRequest(form, SecurityUtil.getCurrentUserId(), jobId)));

    }

    @Override
    public Pager<JobRequestView> list(Integer page, Integer limit, String sortBy, String search) {

        if (!jobRequestRepository.findColumns().contains(sortBy)) {
            sortBy = "job_request_id";
        }

        if (page <= 0) {
            page = 1;
        }

        Page<JobRequest> jobRequests = jobRequestRepository.findAllByStatus(JobRequest.Status.PENDING.value, search,
                PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));

        Pager<JobRequestView> jobRequestViews = new Pager<>(limit, (int) jobRequests.getTotalElements(), page);

        // Pager<JobView> jobViews = new
        // Pager<JobView>(limit,jobRequestRepository.countJobList(Job.Status.PENDING.value,
        // search).intValue(),page);

        jobRequestViews.setResult(jobRequests.getContent().stream().map(JobRequestView::new)
                .collect(Collectors.toList()));

        return jobRequestViews;

    }

    @Override
    public void jobRequestCsv(HttpServletResponse httpServletResponse) {

        String role = SecurityUtil.getCurrentUserRole();

        if (role.equals("EMPLOYEE")) {
            throw new BadRequestException("Access Denied");
        }

        Collection<JobRequestView> exportlist;

        if (role.equals("ADMIN")) {
            exportlist = jobRequestRepository.findAll().stream().map(JobRequestView::new)
                    .collect(Collectors.toList());
        } else {
            exportlist = jobRequestRepository.findAllByUserUserId(SecurityUtil.getCurrentUserId()).stream()
                    .map(JobRequestView::new).collect(Collectors.toList());

        }

        CsvDownload.download(httpServletResponse, exportlist, "Job_Requests");

    }

}
