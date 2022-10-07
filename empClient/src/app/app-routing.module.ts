import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HeaderComponent } from './components/user-management/admin/header-admin/header.component';
import { JobAddComponent } from './components/job-Management/job-add/job-add.component';
import { JoblistComponent } from './components/job-Management/joblist-admin/joblist.component';
import { ListJobRequestComponent } from './components/job-Request-Management/list-job-request/list-job-request.component';
import { AdminComponent } from './components/user-management/admin/admin.component';
import { EmpJobReqComponent } from './components/job-Request-Management/emp-job-req/emp-job-req.component';
import { EmployeeComponent } from './components/user-management/employee/employee.component';
import { EmployerComponent } from './components/user-management/employer/employer.component';
import { UserListComponent } from './components/user-management/user-list/user-list.component';
import { AuthGuard } from './core/auth-guard/auth.guard';
import { UserLoginComponent } from './core/components/user-login/user-login.component';
import { UserRegistrationComponent } from './core/components/user-registration/user-registration.component';
import { JoblistEmplyrComponent } from './components/job-Management/joblist-emplyr/joblist-emplyr.component';
import { JoblistEmployeeComponent } from './components/job-Management/joblist-emp/joblist-employee.component';
import { ForgotPasswordComponent } from './core/components/forgot-password/forgot-password.component';

const routes: Routes = [
  {path:"",redirectTo:"login",pathMatch:"full"},
  {path:"login",component:UserLoginComponent,pathMatch:"full"},
  {path:"register",component:UserRegistrationComponent,pathMatch:"full"},
  {path:"resetpswd/:url",component:ForgotPasswordComponent,pathMatch:"full"},

  {path:"header",component:HeaderComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"admindashboard",component:AdminComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"employerdashboard",component:EmployerComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"employeedashboard",component:EmployeeComponent,canActivate:[AuthGuard],pathMatch:"full"},

  {path:"users",component:UserListComponent,canActivate:[AuthGuard],pathMatch:"full"},
  
  {path:"job",component:JoblistComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"jobadd",component:JobAddComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"joblistadmin",component:JoblistComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"joblistemp",component:JoblistEmployeeComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"joblistemplyr",component:JoblistEmplyrComponent,canActivate:[AuthGuard],pathMatch:"full"},

  {path:"req",component:ListJobRequestComponent,canActivate:[AuthGuard],pathMatch:"full"},
  {path:"reqlistemp",component:EmpJobReqComponent,canActivate:[AuthGuard],pathMatch:"full"},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
