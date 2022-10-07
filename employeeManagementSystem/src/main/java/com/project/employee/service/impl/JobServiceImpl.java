package com.project.employee.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.project.employee.entity.Job;
import com.project.employee.entity.User;
import com.project.employee.exception.BadRequestException;
import com.project.employee.exception.NotFoundException;
import com.project.employee.features.Pager;
import com.project.employee.form.JobForm;
import com.project.employee.repository.JobRepository;
import com.project.employee.repository.UserRepository;
import com.project.employee.security.util.SecurityUtil;
import com.project.employee.service.JobService;
import com.project.employee.view.JobView;

@Service
public class JobServiceImpl implements JobService {
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Collection<JobView> list() {
		return jobRepository.findAll().stream().map((job) -> new JobView(job)).collect(Collectors.toList());
	}

	public Pager<JobView> list(Integer page, Integer limit, String sortBy, Boolean desc,String filter, String search) {
		User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());

		if (!jobRepository.findColumns().contains(sortBy)) {
			sortBy = "job_id";
		}

		if (page <= 0) {
			page = 1;
		}
		Page<Job> jobs;
		if (user.getRole() == 0) {

			ArrayList<Byte> status = new ArrayList<>();
			if (filter.equals("0")) {
				status.add(Job.JobStatus.PENDING.value);
			} else if (filter.equals("1")) {
				status.add(Job.JobStatus.APPROVED.value);
			} else if (filter.equals("2")) {
				status.add(Job.JobStatus.REJECTED.value);
			} else {
				
				status.add(Job.JobStatus.PENDING.value);
				status.add(Job.JobStatus.APPROVED.value);
				status.add(Job.JobStatus.REJECTED.value);
			}
			jobs = jobRepository.findAllByStatus(status, search,
					PageRequest.of(page - 1, limit, Sort.by(desc.booleanValue() ? Direction.DESC : Direction.ASC,
                            sortBy)));

		} else if (user.getRole() == 1) {

			jobs = jobRepository.findAllByUserIdAndStatus(SecurityUtil.getCurrentUserId(), Job.Status.ACTIVE.value,
					search, PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));
		} else if (user.getRole() == 2) {
			jobs = jobRepository.findAllByStatusAndJobStatus(Job.Status.ACTIVE.value, Job.JobStatus.APPROVED.value,
					search, PageRequest.of(page - 1, limit, Sort.by(sortBy).ascending()));
		} else
			throw new BadRequestException("Hii");

		Pager<JobView> jobViews = new Pager<JobView>(limit, (int) jobs.getTotalElements(), page, limit);

		jobViews.setResult(jobs.getContent().stream().map(JobView::new).collect(Collectors.toList()));

		return jobViews;
	}

	@Override
	public JobView add(JobForm form) {
		return new JobView(
				jobRepository.save(new Job(form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()))));
	}

	@Override
	@Transactional
	public JobView update(Integer jobId, JobForm form) {
		return jobRepository.findByJobIdAndStatus(jobId, Job.Status.ACTIVE.value).map((job) -> {
			return new JobView(jobRepository.save(job.update(form)));
		}).orElseThrow(NotFoundException::new);
	}

	@Override
	@Transactional
	public void csvJob(HttpServletResponse httpServletResponse) {
		Collection<JobView> exportlist = jobRepository.findAllByStatus(Job.Status.ACTIVE.value);
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String headerKey = "Content-Disposition";
		String headerValue = "joblist" + sdf.format(dt) + ".csv";
		httpServletResponse.setHeader(headerKey, headerValue);
		httpServletResponse.setContentType("text/csv;");
		httpServletResponse.setCharacterEncoding("shift-jis");
		httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

		try {

			ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
					CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = { "Job Id", "Title", "Description", "No Of Openings", "Created By", "Qualification",
					"Create Date", "Update Date" };
			String[] nameMapping = { "jobId", "title", "description", "noOfOpenings", "createdBy", "qualification",
					"createDate", "updateDate" };

			csvWriter.writeHeader(csvHeader);
			for (JobView reservation : exportlist) {
				csvWriter.write(reservation, nameMapping);
			}

			csvWriter.close();
		} catch (IOException e) {
			throw new BadRequestException("Exception while exporting csv");
		}

	}

	@Override
	@Transactional
	public void delete(Integer jobId) {
		Job job = jobRepository.findByJobIdAndStatus(jobId, Job.Status.ACTIVE.value)
				.orElseThrow(NotFoundException::new);

		job.setStatus(Job.Status.INACTIVE.value);

		jobRepository.save(job);

		return;
	}

	@Override
	public void deleteSelected(Collection<Integer> jobIds) {

		for (Integer jobId : jobIds) {

			Optional<Job> job = jobRepository.findById(jobId);

			if (job.isPresent()) {
				jobRepository.save(job.get().delete());
			}

		}

	}

	@Override
	public long jobCount() {
		return jobRepository.countJobs(Job.Status.ACTIVE.value);
	}

	@Override
	public JobView getJob(Integer jobId) {

		return new JobView(
				jobRepository.findByJobIdAndStatus(jobId, Job.Status.ACTIVE.value).orElseThrow(NotFoundException::new));
	}

	@Override
	public JobView approve(Integer jobId, Integer status) {
		Job job = jobRepository.findById(jobId).orElseThrow(NotFoundException::new);
		job.approve(status);
		jobRepository.save(job);
		return new JobView(job);
	}

}
