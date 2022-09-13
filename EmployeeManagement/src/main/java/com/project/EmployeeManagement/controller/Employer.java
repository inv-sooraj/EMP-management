package com.project.EmployeeManagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.service.EmployerService;
import com.project.EmployeeManagement.view.UserView;

@RestController
@RequestMapping("/employer")
public class Employer {

    @Autowired
    private EmployerService employerService;



    @PostMapping
    public UserView add(@Valid @RequestBody UserForm form,Errors errors ){
        return employerService.addEmployer(form);

    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId")Integer userId) {
        employerService.delete(userId);
    }

    // @DeleteMapping("/{jobId}")
    // public void delete(@PathVariable("jobId")Integer jobId) {
    //     jobService.delete(jobId);
    // }
    
}
