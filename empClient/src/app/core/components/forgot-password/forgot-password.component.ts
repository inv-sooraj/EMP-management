import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent implements OnInit {
  token: string;
  expiry: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private service: AuthService,
    private router: Router
  ) {
    this.token = this.activatedRoute.snapshot.queryParams['token'];
    this.expiry = this.activatedRoute.snapshot.queryParams['expiry'];

    let timeDiff = Date.now() - this.expiry;

    if (timeDiff >= 0) {
      alert('Session Expired');
      this.token = '';
      router.navigate(['/forgot-password']);
    }
  }

  ngOnInit(): void {}

  userEmailForm: FormGroup = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}'),
    ]),
  });

  resetPasswordForm: FormGroup = new FormGroup({
    newPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=)(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}'
      ),
    ]),
    confirmNewPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=)(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}'
      ),
    ]),
  });

  sendEmail(): void {
    if (!this.userEmailForm.valid) {
      console.log('Validation Failed');
      this.userEmailForm.markAllAsTouched();

      return;
    }
    let email: string = this.userEmailForm.controls['email'].value;

    this.service.forgotPassword(email).subscribe({
      next: (response: any) => {
        console.log(response);
        alert('Email has been sent');
      },
      error(err) {
        console.log(err);
      },
    });
  }

  changePassword(): void {
    if (!this.resetPasswordForm.valid) {
      console.log('Validation Failed');
      this.resetPasswordForm.markAllAsTouched();
      return;
    }

    if (
      this.resetPasswordForm.controls['newPassword'].value !=
      this.resetPasswordForm.controls['confirmNewPassword'].value
    ) {
      alert('Password Should Match');
      return;
    }

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
