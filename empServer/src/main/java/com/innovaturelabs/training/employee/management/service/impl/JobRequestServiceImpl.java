
package com.innovaturelabs.training.employee.management.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.innovaturelabs.training.employee.management.entity.JobRequest;
import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.form.JobRequestForm;
import com.innovaturelabs.training.employee.management.repository.JobRequestRepository;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
import com.innovaturelabs.training.employee.management.service.JobRequestService;
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

        Pager<JobRequestView> jobRequestViews = new Pager<JobRequestView>(limit, (int) jobRequests.getTotalElements(),
                page + 1);

        // Pager<JobView> jobViews = new
        // Pager<JobView>(limit,jobRequestRepository.countJobList(Job.Status.PENDING.value,
        // search).intValue(),page);

        jobRequestViews.setResult(jobRequests.getContent().stream().map(jobRequest -> new JobRequestView(jobRequest))
                .collect(Collectors.toList()));

        return jobRequestViews;

    }

    @Override
    public void jobCsv(HttpServletResponse httpServletResponse) {
        Collection<JobRequestView> exportlist = jobRequestRepository.findAll().stream()
                .map(jobRequest -> new JobRequestView(jobRequest)).collect(Collectors.toList());
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=emailList" + sdf.format(dt) + ".csv";
        httpServletResponse.setHeader(headerKey, headerValue);
        httpServletResponse.setContentType("text/csv;");
        httpServletResponse.setCharacterEncoding("shift-jis");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        try {

            ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] csvHeader = { "Job Request Id", "User", "Job", "Status", "Feedback", "Remark",

                    "Create Date", "Update Date" };
            String[] nameMapping = { "jobRequestId", "userId", "jobId", "status", "feedback", "remark",

                    "createDate", "updateDate" };

            csvWriter.writeHeader(csvHeader);
            for (JobRequestView reservation : exportlist) {
                csvWriter.write(reservation, nameMapping);
            }

            csvWriter.close();
        } catch (IOException e) {
            throw new BadRequestException("Exception while exporting csv");
        }

    }

}
