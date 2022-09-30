import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { LoginComponent } from './core/components/login/login.component';
import { RegistrationComponent } from './core/components/registration/registration.component';
import { JobViewComponent } from './components/job-view/job-view.component';
import { EditJobComponent } from './components/edit-job/edit-job.component';
import { AuthGuard } from './core/auth-guard/auth.guard';
import { AddEditUserComponent } from './components/add-edit-user/add-edit-user.component';
import { UserDashboardComponent } from './components/user-dashboard/user-dashboard.component';
import { UserJobViewComponent } from './components/user-job-view/user-job-view.component';
import { JobRequestViewComponent } from './components/job-request-view/job-request-view.component';
import { RoleGuard } from './core/role-guard/role.guard';
import { EditUserComponent } from './components/edit-user/edit-user.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent },
  {
    path: 'adminDashboard',
    component: AdminDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
  },
  {
    path: 'jobView',
    component: JobViewComponent,
    canActivate: [AuthGuard],
  },

  {
    path: 'addEditUser',
    component: AddEditUserComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'addEditUser/:userId',
    component: AddEditUserComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'userDashboard',
    component: UserDashboardComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'userJobView',
    component: UserJobViewComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'jobRequestView',
    component: JobRequestViewComponent,
    canActivate: [AuthGuard],
  },
  { path: 'editUser', component: EditUserComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
