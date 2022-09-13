package com.project.EmployeeManagement.service;

import com.project.EmployeeManagement.form.UserForm;
import com.project.EmployeeManagement.view.UserView;

public interface EmployerService {


   UserView  addEmployer(UserForm form);

   void delete(Integer userId);

    
}
