import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CardsComponent } from './components/cards/cards.component';
import { JobApplyComponent } from './components/job-management/job-apply/job-apply.component';
import { JobListComponent } from './components/job-management/job-list/job-list.component';
import { JobRequestListComponent } from './components/job-request-management/job-request-list/job-request-list.component';
import { PageErrorComponent } from './components/page-error/page-error.component';
import { TestComponentComponent } from './components/test-component/test-component.component';
import { AboutComponent } from './components/user-management/about/about.component';
import { ContactComponent } from './components/user-management/contact/contact.component';
import { EmployerChartComponent } from './components/user-management/employer-chart/employer-chart.component';
import { HomepagejobviewComponent } from './components/user-management/homepagejobview/homepagejobview.component';

import { LandingPageComponent } from './components/user-management/landing-page/landing-page.component';

import { UserDetailComponent } from './components/user-management/user-detail/user-detail.component';

import { UserEditComponent } from './components/user-management/user-edit/user-edit.component';
import { UserListComponent } from './components/user-management/user-list/user-list.component';
import { UserProfileComponent } from './components/user-management/user-profile/user-profile.component';
import { UserchartComponent } from './components/user-management/userchart/userchart.component';
import { AuthGuard } from './core/auth/auth.guard';
import { LoginGuard } from './core/auth/login.guard';
import { ForgotPasswordComponent } from './core/components/forgot-password/forgot-password.component';
import { LoginGoogleComponent } from './core/components/login-google/login-google.component';
import { LoginComponent } from './core/components/login/login.component';
import { RegisterComponent } from './core/components/register/register.component';
import { UserVerifyComponent } from './core/components/user-verify/user-verify.component';

const routes: Routes = [
  {
    path: '',
    component: LandingPageComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [LoginGuard],
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [LoginGuard],
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
    path: 'test',
    component: TestComponentComponent,
    canActivate: [AuthGuard],
  },

  {
    path: 'usrdetail',
    component: UserDetailComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'job-request-list',
    component: JobRequestListComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'reset-password',
    component: ForgotPasswordComponent,
    canActivate: [LoginGuard],
  },
  {
    path: 'user-verify',
    component: UserVerifyComponent,
    canActivate: [LoginGuard],
  },
  {
    path: 'userchart',
    component: UserchartComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'rqstchart',
    component: EmployerChartComponent,
    canActivate: [AuthGuard],
  },
  { path: 'landingpage', component: LandingPageComponent },
  { path: '404-page', component: PageErrorComponent },

  {
    path: 'glogin',
    component: LoginGoogleComponent,
    canActivate: [LoginGuard],
  },
  { path: 'cards', component: CardsComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contacts', component: ContactComponent },
  {
    path: '**',
    redirectTo: '404-page',
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      useHash: false,
      anchorScrolling: 'enabled',
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
