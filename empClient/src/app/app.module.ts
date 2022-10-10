import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './core/components/login/login.component';
import { RegistrationComponent } from './core/components/registration/registration.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { AuthInterceptorService } from './core/interceptor/auth-interceptor.service';
import { JobViewComponent } from './components/job-view/job-view.component';
import { EditJobComponent } from './components/edit-job/edit-job.component';
import { AddEditUserComponent } from './components/add-edit-user/add-edit-user.component';
import { UserDashboardComponent } from './components/user-dashboard/user-dashboard.component';
import { UserJobViewComponent } from './components/user-job-view/user-job-view.component';
import { JobRequestViewComponent } from './components/job-request-view/job-request-view.component';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    AdminDashboardComponent,
    JobViewComponent,
    EditJobComponent,
    AddEditUserComponent,
    UserDashboardComponent,
    UserJobViewComponent,
    JobRequestViewComponent,
    EditUserComponent,
    FooterComponent,
    HeaderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
