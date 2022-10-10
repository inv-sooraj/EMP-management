import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JobApplyComponent } from './components/job-management/job-apply/job-apply.component';
import { JobFormComponent } from './components/job-management/job-form/job-form.component';
import { JobListComponent } from './components/job-management/job-list/job-list.component';
import { JobRequestListComponent } from './components/job-request-management/job-request-list/job-request-list.component';
import { UserEditComponent } from './components/user-management/user-edit/user-edit.component';
import { UserListComponent } from './components/user-management/user-list/user-list.component';
import { UserProfileComponent } from './components/user-management/user-profile/user-profile.component';
import { AuthGuard } from './core/auth/auth.guard';
import { ForgotPasswordComponent } from './core/components/forgot-password/forgot-password.component';
import { LoginComponent } from './core/components/login/login.component';
import { RegisterComponent } from './core/components/register/register.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'user-list',
    component: UserListComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user-profile',
    component: UserProfileComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user-edit',
    component: UserEditComponent,
    canActivate: [AuthGuard],

  },
  {
    path: 'job-list',
    component: JobListComponent,
    canActivate: [AuthGuard],

  },
  {
    path: 'job-apply',
    component: JobApplyComponent,
    canActivate: [AuthGuard],

  },
  {
    path: 'job-request-list',
    component: JobRequestListComponent,
    canActivate: [AuthGuard],

  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent,
  },
  {
    path: '**',
    component: LoginComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
