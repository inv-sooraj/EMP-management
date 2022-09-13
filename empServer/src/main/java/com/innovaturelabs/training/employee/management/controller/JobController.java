
package com.innovaturelabs.training.employee.management.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovaturelabs.training.employee.management.entity.Job;
import com.innovaturelabs.training.employee.management.form.JobForm;
import com.innovaturelabs.training.employee.management.repository.JobRepository;
import com.innovaturelabs.training.employee.management.service.JobService;
import com.innovaturelabs.training.employee.management.view.JobView;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;

    @PostMapping
    public JobView add(@Valid @RequestBody JobForm form) {
        return jobService.add(form);
    }

    @GetMapping("/page")
    public Page<JobView> list(@RequestParam Integer page, @RequestParam String sortBy) {
        return jobService.list(page, sortBy);
    }

    @PutMapping("/{jobId}")
    public JobView update(@PathVariable("jobId") Integer jobId, @Valid @RequestBody JobForm form) {
        return jobService.update(form, jobId);
    }

    @PutMapping("/delete/{jobId}")
    public void delete(@PathVariable("jobId") Integer jobId) {
        jobService.delete(jobId);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile() {

        ByteArrayInputStream in;

        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Job job : jobRepository.findAll()) {
                List<String> data = Arrays.asList(
                        String.valueOf(job.getJobId()),
                        job.getTitle(),
                        job.getDescription(),
                        String.valueOf(job.getCreateDate()));
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            in = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }

        String filename = "sample.csv";
        InputStreamResource file = new InputStreamResource(in);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
