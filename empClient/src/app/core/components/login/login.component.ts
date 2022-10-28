import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { AuthService } from '../../service/auth.service';
import { OauthgoogleService } from '../../service/oauthgoogle.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  constructor(
    private router: Router,
    private service: AuthService,
    private toastService: ToastrService,
  ) {}

  ngOnInit(): void {
    this.service.logout();

    // window.open('http://localhost:8080/')
  }

  loginForm: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  login() {
    if (!this.loginForm.valid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    let body = {
      userName: this.loginForm.controls['username'].value,
      password: this.loginForm.get('password')?.value,
    };

    this.service.login(body).subscribe({
      next: (response: any) => {
        console.log('Logged In');
        console.log(response);
        localStorage.setItem('accessTokenExpiry', response.accessToken.expiry);
        localStorage.setItem('accessToken', response.accessToken.value);
        localStorage.setItem('refreshToken', response.refreshToken.value);
        localStorage.setItem('name', response.name);
        localStorage.setItem('role', response.role);
        this.service.startTimer();

        if (response.role == 2) this.router.navigate(['userchart']);
        else if (response.role == 1) this.router.navigate(['job-list']);
        else this.router.navigate(['job-apply']);
      },
      error: (error: any) => {
        console.log('Error', error.error);
        if (error.error.status == 400) {
          if (error.error.message == 'Invalid Username') {
            this.toastService.error('Invalid UserName!');
          } else {
            this.toastService.error('', 'Invalid Password!');
          }
        } else {
          this.toastService.error('Error!');
        }
      },
    });
  }
}
