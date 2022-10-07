import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserLoginComponent } from './core/components/user-login/user-login.component';
import { UserRegistrationComponent } from './core/components/user-registration/user-registration.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthGuard } from './core/auth-guard/auth.guard';
import { AuthInterceptor } from './core/interceptor/auth.interceptor';
import { AdminComponent } from './components/user-management/admin/admin.component';
import { EmployerComponent } from './components/user-management/employer/employer.component';
import { EmployeeComponent } from './components/user-management/employee/employee.component';
import { HeaderComponent } from './components/user-management/admin/header-admin/header.component';
import { JoblistComponent } from './components/job-Management/joblist-admin/joblist.component';
import { UserListComponent } from './components/user-management/user-list/user-list.component';
import { GaugeModule } from 'angular-gauge';
import { JobAddComponent } from './components/job-Management/job-add/job-add.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ListJobRequestComponent } from './components/job-Request-Management/list-job-request/list-job-request.component';
import { UpdateUserComponent } from './components/user-management/update-User-Details/update-user/update-user.component';
import { EmployeeHeaderComponent } from './components/user-management/employee/employee-header/employee-header.component';
import { EmpJobReqComponent } from './components/job-Request-Management/emp-job-req/emp-job-req.component';
import { EmplyrHeaderComponent } from './components/user-management/employer/emplyr-header/emplyr-header.component';
import { JoblistEmplyrComponent } from './components/job-Management/joblist-emplyr/joblist-emplyr.component';
import { JoblistEmployeeComponent } from './components/job-Management/joblist-emp/joblist-employee.component';
import { ForgotPasswordComponent } from './core/components/forgot-password/forgot-password.component';

@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent,
    UserRegistrationComponent,
    AdminComponent,
    EmployerComponent,
    EmployeeComponent,
    HeaderComponent,
    JoblistComponent,
    UserListComponent,
    JobAddComponent,
    ListJobRequestComponent,
    UpdateUserComponent,
    EmployeeHeaderComponent,
    EmpJobReqComponent,
    EmplyrHeaderComponent,
    JoblistEmplyrComponent,
    JoblistEmployeeComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    GaugeModule.forRoot(),
    BrowserAnimationsModule
    
  ],
  providers: [AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass:AuthInterceptor,
      multi:true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
