package com.project.employee.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.project.employee.entity.Job;
import com.project.employee.exception.BadRequestException;
import com.project.employee.exception.NotFoundException;
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
		return jobRepository.findAll();
	}

	@Override
	public JobView add(JobForm form) {
		return new JobView(
				jobRepository.save(new Job(form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()))));
	}

	@Override
	@Transactional
	public JobView update(Integer jobId, JobForm form) {
		return jobRepository
				.findByJobIdAndUserUserIdAndStatus(jobId, SecurityUtil.getCurrentUserId(), Job.Status.ACTIVE.value)
				.map((job) -> {
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
		String headerValue = "attachment; filename=emailList" + sdf.format(dt) + ".csv";
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
}
