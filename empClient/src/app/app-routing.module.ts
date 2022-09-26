import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JobFormComponent } from './components/job-management/job-form/job-form.component';
import { JobListComponent } from './components/job-management/job-list/job-list.component';
import { UserEditComponent } from './components/user-management/user-edit/user-edit.component';
import { UserListComponent } from './components/user-management/user-list/user-list.component';
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
  },
  {
    path: 'user-edit',
    component: UserEditComponent,
  },
  {
    path: 'job-list',
    component: JobListComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
