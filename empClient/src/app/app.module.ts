import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { InterceptorInterceptor } from './core/interceptor/interceptor.interceptor';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './core/components/login/login.component';
import { RegisterComponent } from './core/components/register/register.component';
import { UserListComponent } from './components/user-management/user-list/user-list.component';
import { UserEditComponent } from './components/user-management/user-edit/user-edit.component';
import { JobListComponent } from './components/job-management/job-list/job-list.component';
import { JobFormComponent } from './components/job-management/job-form/job-form.component';
import { UserProfileComponent } from './components/user-management/user-profile/user-profile.component';
import { JobRequestListComponent } from './components/job-request-management/job-request-list/job-request-list.component';
import { JobApplyComponent } from './components/job-management/job-apply/job-apply.component';
import { HeaderComponent } from './components/header/header.component';
import { AdminUserEditComponent } from './components/user-management/admin-user-edit/admin-user-edit.component';
import { ForgotPasswordComponent } from './core/components/forgot-password/forgot-password.component';
import { AdminUserAddComponent } from './components/user-management/admin-user-add/admin-user-add.component';

import { NgIdleModule } from '@ng-idle/core';
import { TestComponentComponent } from './components/test-component/test-component.component';

import { UserchartComponent } from './components/user-management/userchart/userchart.component';

import { ToastrModule } from 'ngx-toastr';
import { HotToastModule } from '@ngneat/hot-toast';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    UserListComponent,
    UserEditComponent,
    JobListComponent,
    JobFormComponent,
    UserProfileComponent,
    JobRequestListComponent,
    JobApplyComponent,
    HeaderComponent,
    AdminUserEditComponent,
    ForgotPasswordComponent,
    AdminUserAddComponent,

    TestComponentComponent,

    UserchartComponent,
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgIdleModule.forRoot(),

    ToastrModule.forRoot(),
    HotToastModule.forRoot(),
    BrowserAnimationsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
