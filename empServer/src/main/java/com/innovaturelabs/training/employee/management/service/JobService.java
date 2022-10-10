
package com.innovaturelabs.training.employee.management.service;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.util.Pager;
import com.innovaturelabs.training.employee.management.view.JobView;
import com.innovaturelabs.training.employee.management.view.StatusView;

public interface JobService {

    JobView add(JobForm form);

    JobView getJob(Integer jobId);

    JobView update(JobForm form, Integer jobId);

    JobView updateStatus(Integer jobId, Byte status);

    void changeSelectedStatus(Collection<Integer> jobIds, Byte status);

    Pager<JobView> list(Integer page, Integer limit, String sortBy, Boolean desc, String search, Integer data,
            Boolean apply);

    void jobCsv(HttpServletResponse httpServletResponse);

    Collection<StatusView> getStat();

}
