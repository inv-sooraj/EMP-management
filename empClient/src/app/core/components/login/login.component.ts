import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  constructor(private route: Router, private service: AuthService) {}

  ngOnInit(): void {}

  loginForm = new FormGroup({
    userName: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  signIn() {
    if (this.loginForm.valid) {
      console.log(this.loginForm.value);

      this.service.logUser(this.loginForm.value).subscribe({
        next: (response: any) => {
          console.log(response);
          // Logged in user role saved to localstorage
          localStorage.setItem('key', response.role);
          // this.userService.userRole = response.role;
          // let role = Number(localStorage.getItem('key'));
          // if (role != 0) {
          this.route.navigate(['userDashboard']);
          // } else {
          //   this.route.navigate(['adminDashboard']);
          // }
          localStorage.setItem('accessToken', response.accessToken.value);
          localStorage.setItem('refreshToken', response.refreshToken.value);
        },
        error: (error: any) => {
          console.log(error);
          alert('Invalid Credentials');
        },
      });
    }
  }
}
