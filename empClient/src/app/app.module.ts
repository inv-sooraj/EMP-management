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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    UserListComponent,
    UserEditComponent,
    JobListComponent,
    JobFormComponent,
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
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
