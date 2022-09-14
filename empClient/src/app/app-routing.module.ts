import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './components/user-management/admin/admin.component';
import { AuthGuard } from './core/auth-guard/auth.guard';
import { UserLoginComponent } from './core/components/user-login/user-login.component';

const routes: Routes = [
  {path:"",redirectTo:"login",pathMatch:"full"},
  {path:"login",component:UserLoginComponent,pathMatch:"full"},
  {path:"admindashboard",component:AdminComponent,canActivate:[AuthGuard],pathMatch:"full"},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
