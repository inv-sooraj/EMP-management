package com.project.employee.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.project.employee.entity.Job;
import com.project.employee.entity.JobRequest;
import com.project.employee.exception.BadRequestException;
import com.project.employee.exception.NotFoundException;
import com.project.employee.features.Pager;
import com.project.employee.form.JobRequestForm;
import com.project.employee.repository.JobRepository;
import com.project.employee.repository.JobRequestRepository;
import com.project.employee.repository.UserRepository;
import com.project.employee.security.util.SecurityUtil;
import com.project.employee.service.JobRequestService;
import com.project.employee.view.JobRequestView;

@Service
public class JobRequestServiceImpl implements JobRequestService {
	@Autowired
	JobRepository jobRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	JobRequestRepository jobRequestRepository;

	@Override
	public Collection<JobRequestView> list() {
		return jobRequestRepository.findAll().stream().map((jobRequest) -> new JobRequestView(jobRequest)).toList();

	}

	@Override
	public Collection<JobRequestView> listById(Integer jobId) {
		return jobRequestRepository.findAllByJobJobId(jobId).stream()
				.map((jobRequest) -> new JobRequestView(jobRequest)).toList();

	}

	@Override
	public JobRequestView add(Integer jobId, JobRequestForm form) {
		return new JobRequestView(jobRequestRepository.save(
				new JobRequest(jobId, form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()).getUserId())));
	}

	@Override
	public JobRequestView update(Integer reqId, Integer status) {

		JobRequest jobRequest = jobRequestRepository.findById(reqId).orElseThrow(NotFoundException::new);
		jobRequest.update(status);
		jobRequestRepository.save(jobRequest);
		return new JobRequestView(jobRequest);
	}

	@Override
	public void delete(Integer reqId) throws NotFoundException {
		JobRequest jobRequest = jobRequestRepository.findById(reqId).orElseThrow(NotFoundException::new);
		jobRequest.setStatus(JobRequest.Status.INACTIVE.value);
		jobRequestRepository.save(jobRequest);

	}
	
	@Override
    public void deleteSelected(Collection<Integer> reqIds) {

        for (Integer reqId : reqIds) {
            
            Optional<JobRequest> req = jobRequestRepository.findById(reqId);

            if (req.isPresent()) {
                jobRequestRepository.save(req.get().delete());
            }

        }

    }

	@Override
	public long jobRequestCount() {
		return jobRequestRepository.countJobRequest(JobRequest.Status.ACTIVE.value);
	}

	@Override
	public Pager<JobRequestView> list(Integer page, Integer limit, String sortBy, String search) {

		if (!jobRequestRepository.findColumns().contains(sortBy)) {
			sortBy = "req_id";
		}

		if (page <= 0) {
			page = 1;
		}

		Page<JobRequest> jobRequests = jobRequestRepository.findAllByStatus(JobRequest.Status.ACTIVE.value, search,
				PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));
		Pager<JobRequestView> jobRequestViews = new Pager<JobRequestView>(limit, (int) jobRequests.getTotalElements(),
				page + 1);

		// Pager<JobView> jobViews = new
		// Pager<JobView>(limit,jobRepository.countJobList(Job.Status.PENDING.value,
		// search).intValue(),page);

		jobRequestViews
				.setResult(jobRequests.getContent().stream().map(JobRequestView::new).collect(Collectors.toList()));

		return jobRequestViews;
	}

	@Override
	@Transactional
	public void csvReq(HttpServletResponse httpServletResponse) {
		Collection<JobRequestView> exportlist = jobRequestRepository.findAllByStatus(JobRequest.Status.ACTIVE.value);
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String headerKey = "Content-Disposition";
		String headerValue = "jobreqlist" + sdf.format(dt) + ".csv";
		httpServletResponse.setHeader(headerKey, headerValue);
		httpServletResponse.setContentType("text/csv;");
		httpServletResponse.setCharacterEncoding("shift-jis");
		httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

		try {

			ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
					CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = { "Req Id", "Remark", "Job Id", "User Id", "Request Status", "Status", "Create Date",
					"Update Date" };
			String[] nameMapping = { "reqId", "remark", "jobId", "userId", "requestStatus", "status", "createDate",
					"updateDate" };

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
