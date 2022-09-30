package com.project.EmployeeManagement.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.EmployeeManagement.entity.Job;
import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.exception.BadRequestException;
import com.project.EmployeeManagement.exception.NotFoundException;
import com.project.EmployeeManagement.form.JobForm;
import com.project.EmployeeManagement.repository.JobRepository;
import com.project.EmployeeManagement.repository.UserRepository;
import com.project.EmployeeManagement.util.Pager;
import com.project.EmployeeManagement.security.util.SecurityUtil;
import com.project.EmployeeManagement.service.JobService;
import com.project.EmployeeManagement.view.JobDetailView;
import com.project.EmployeeManagement.view.JobView;

import ch.qos.logback.core.joran.conditional.ElseAction;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;

    // @Override
    // public JobView addJob(JobForm form) {

    // Byte userRole =
    // userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

    // if (!userRole.equals(User.Role.USER.value)) {

    // return new JobView(
    // jobRepository.save(new Job(form,
    // userRepository.findByUserId(SecurityUtil.getCurrentUserId()))));
    // } else
    // throw new BadRequestException("Illegal Access");

    // }

    @Override
    public JobView addJob(JobForm form) {

        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (!userRole.equals(User.Role.USER.value)) {

            return new JobView(
                    jobRepository.save(
                            new Job(form, userRepository.findByUserId(SecurityUtil.getCurrentUserId()), userRole)));

        } else
            throw new BadRequestException("Illegal Access");

    }

    @Override
    public Collection<JobView> list() {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();
        if (!userRole.equals(User.Role.EMPLOYER.value)) {
            return jobRepository.findAllByStatus(Job.Status.ACTIVE.value);
        } else {
            return jobRepository.findByUserUserIdAndStatus(SecurityUtil.getCurrentUserId(), Job.Status.ACTIVE.value);

        }
    }

    @Override
    @Transactional
    public void delete(Integer itemId, Integer flag) {

        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        if (!userRole.equals(User.Role.USER.value)) {
            Job job = jobRepository.findById(itemId).orElseThrow(NotFoundException::new);
            if (flag.equals(0)) {

                job.setStatus(Job.Status.DELETED.value);
            } else if (flag.equals(3)) {
                job.setStatus(Job.Status.ACTIVE.value);

            }else
                throw new BadRequestException("Invalid");

            job.setUpdateDate(new Date());

            jobRepository.save(job);
        } else
            throw new BadRequestException("illegal access");
        // return;
    }

    @Override
    @Transactional
    public JobDetailView update(Integer jobId, JobForm form) throws NotFoundException {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        // if (userRole.equals(User.Role.ADMIN.value)) {
        if (!userRole.equals(User.Role.USER.value)) {

            return new JobDetailView(jobRepository.save(
                    jobRepository.findByJobIdAndStatus(jobId, Job.Status.ACTIVE.value)
                            .orElseThrow(NotFoundException::new).update(form)));
        } else
            throw new BadRequestException("illegal access");
    }

    // ...........................pagination.........................

    @Override
    public Pager<JobDetailView> listItem(String search, String limit, String sort, String page, String filter) {
        Byte userRole = userRepository.findById(SecurityUtil.getCurrentUserId()).get().getRole();

        Page<Job> jobs;
        if (!userRole.equals(User.Role.EMPLOYER.value)) {

            ArrayList<Byte> status = new ArrayList<>();

            if (filter.equals("1")) {
                status.add(Job.Status.ACTIVE.value);
            } else if (filter.equals("2")) {
                status.add(Job.Status.PENDING.value);

            } else {
                status.add(Job.Status.PENDING.value);
                status.add(Job.Status.ACTIVE.value);
            }

            jobs = jobRepository.find(status, search,
                    PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(sort).ascending()));
        } else {
            ArrayList<Byte> status = new ArrayList<>();

            status.add(Job.Status.PENDING.value);
            status.add(Job.Status.ACTIVE.value);

            jobs = jobRepository.findAllByUserIdStatus(SecurityUtil.getCurrentUserId(),
                    status, search,
                    PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(sort).ascending()));
        }
        Pager<JobDetailView> jobDetailViews = new Pager<JobDetailView>(Integer.parseInt(limit),
                (int) jobs.getTotalElements(),
                Integer.parseInt(page));

        jobDetailViews.setResult(
                jobs.getContent().stream().map(job -> new JobDetailView(job)).collect(Collectors.toList()));

        return jobDetailViews;

    }

    // .......................................................

    // ...........................CSV Download.....................
    @Override
    @Transactional
    public void jobCsv(HttpServletResponse httpServletResponse) {
        Collection<JobView> exportlist = jobRepository.findAll().stream().map(job -> new JobView(job))
                .collect(Collectors.toList());
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
            String[] csvHeader = { "Job Id", "Title", "Description", "Status", "Qualification", "Openings", "UserId",
                    "Create Date", "Update Date" };
            String[] nameMapping = { "jobId", "jobTitle", "jobDescription", "status", "qualification", "openings",
                    "userId", "createDate", "updateDate" };

            csvWriter.writeHeader(csvHeader);
            for (JobView reservation : exportlist) {
                csvWriter.write(reservation, nameMapping);
            }

            csvWriter.close();
        } catch (IOException e) {
            throw new BadRequestException("Exception while exporting csv");
        }

    }
    // ................................................................................

    @Override
    public JobDetailView get(Integer jobId) {

        return jobRepository.findByJobId(jobId)
                .map((job) -> {
                    return new JobDetailView(job);
                }).orElseThrow(NotFoundException::new);

    }

}
