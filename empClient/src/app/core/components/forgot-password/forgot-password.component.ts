import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent implements OnInit {
  token: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private service: AuthService
  ) {
    this.token = this.activatedRoute.snapshot.queryParams['token'];
    console.log(this.activatedRoute);
    console.log(this.token);
  }

  ngOnInit(): void {
    this.service.logout();
  }

  userEmailForm: FormGroup = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}'),
    ]),
  });

  resetPasswordForm: FormGroup = new FormGroup({
    newPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
      ),
    ]),
    confirmNewPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
      ),
    ]),
  });

  sendEmail(): void {
    let email: string = this.userEmailForm.controls['email'].value;

    this.service.forgotPassword(email).subscribe({
      next: (response: any) => {
        console.log(response);
      },
      error(err) {
        console.log(err);
      },
    });
  }

  changePassword(): void {
    let password = this.resetPasswordForm.controls['newPassword'].value;

    this.service.resetPassword(this.token, password).subscribe({
      next: (response: any) => {
        console.log(response);
      },
      error(err) {
        console.log(err);
      },
    });
  }
}
