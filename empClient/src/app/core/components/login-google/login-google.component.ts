import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import {
  OAuthGoogleService,
  UserInfo,
} from '../../service/oauthgoogle.service';

@Component({
  selector: 'app-login-google',
  templateUrl: './login-google.component.html',
  styleUrls: ['./login-google.component.css'],
})
export class LoginGoogleComponent {
  userInfo?: UserInfo;

  constructor(private service: OAuthGoogleService, private router: Router,private toastService:ToastrService) {
    this.googleSignIn();
  }

  formVisible: boolean = false;

  registrationForm: FormGroup = new FormGroup({
    role: new FormControl('2', Validators.required),
    userName: new FormControl('', [
      Validators.required,
      Validators.maxLength(50),
    ]),
  });

  submit() {
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    let body = {
      userName: this.registrationForm.controls['userName'].value,
      role: this.registrationForm.controls['role'].value,
      idToken: sessionStorage.getItem('id_token') as string,
    };

    console.log(body);

    this.service.register(body).subscribe({
      next: (response: any) => {
        console.log(JSON.stringify(response, undefined, 4));

        Swal.fire('Registration Completed', '', 'success');

        localStorage.setItem('accessTokenExpiry', response.accessToken.expiry);
        localStorage.setItem('accessToken', response.accessToken.value);
        localStorage.setItem('refreshToken', response.refreshToken.value);
        localStorage.setItem('name', response.name);
        localStorage.setItem('role', response.role);

        if (response.role == 1) this.router.navigate(['job-list']);
        else this.router.navigate(['job-apply']);
      },
      error: (err: any) => {
        console.log(err);

        if (err.error.status == 400) {
          if (err.error.message == 'UserName Already Exists') {
            Swal.fire('UserName Already Exists', '', 'error').then(
              (result) => {}
            );
            this.registrationForm.controls['userName'].setErrors(
              Validators.required
            );
          }
        } else {
          Swal.fire(err.error.message, '', 'error').then((result) => {});
        }
      },
    });
  }

  googleSignIn() {
    this.service.signIn();
    this.service.userProfileSubject.subscribe((info) => {
      this.userInfo = info;

      let queryParam = new HttpParams().append(
        'email',
        this.userInfo.info.email
      );

      this.service.verifyEmail(queryParam).subscribe({
        next: (response: any) => {
          if (response == 'NATIVE_USER') {
            this.toastService.error('Native User');
            this.router.navigateByUrl('login');
          } else if (response == 'GOOGLE_USER') {
            this.loginWithIdToken(sessionStorage.getItem('id_token') as string);
          } else if (response == 'NOT_PRESENT') {
            this.formVisible = true;
          }
        },
        error:(err:any)=> {
          console.log(err);
          Swal.fire(err.error.message, '', 'error').then((result) => {this.router.navigateByUrl('login')});
        },
      });
    });
  }

  loginWithIdToken(idToken: string) {
    this.service.login(idToken).subscribe({
      next: (response: any) => {
        console.log(JSON.stringify(response, undefined, 4));

        localStorage.setItem('accessTokenExpiry', response.accessToken.expiry);
        localStorage.setItem('accessToken', response.accessToken.value);
        localStorage.setItem('refreshToken', response.refreshToken.value);
        localStorage.setItem('name', response.name);
        localStorage.setItem('role', response.role);
        localStorage.setItem('userType', response.userType);
        

        if (response.role == 1) this.router.navigate(['job-list']);
        else this.router.navigate(['job-apply']);
      },
      error:(err:any)=> {
        console.log(err);
        Swal.fire(err.error.message, '', 'error').then((result) => {this.router.navigateByUrl('login')});

      },
    });
  }

  // http://localhost:4200/
  // #state=SVdZdC1SUnhDRzcudn42UjFvWFhPUk9vRzZ2Yjl2anIwMEM3VG92MXRmd2dw
  // &access_token=ya29.a0Aa4xrXNmZSf7Li4dLu-krsd2P09WDZHxCyybWWHf_HS68gTjwNHylOH9o5W5bbmQB24i0BPzG0yQhRJgCUcj8a4ovSb7AQAD-bL1MLTiuKFt5i25faywDx19hLui6dwWamOrGCmkVvReqJoCzbNGK2htn8hqaCgYKATASARISFQEjDvL9brE0VzigibeXbpdUSom1vQ0163
  // &token_type=Bearer
  // &expires_in=3599
  // &scope=email%20profile%20openid%20https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile
  // &id_token=eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc3Y2MwZWY0YzcxODFjZjRjMGRjZWY3YjYwYWUyOGNjOTAyMmM3NmIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI5NzU4NzYyNzIxOC1xZ2FpdG5wZnA1cTludThicjBrNGxpOTc2MmI1N3NjYy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImF1ZCI6Ijk3NTg3NjI3MjE4LXFnYWl0bnBmcDVxOW51OGJyMGs0bGk5NzYyYjU3c2NjLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTA4NDgzMjk5OTExMjg1MzY3NDgxIiwiZW1haWwiOiJpbnZzbmVoaWxAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJtNFMzOVJvM2l6Sm53cXBrY09ldDB3Iiwibm9uY2UiOiJTVmRaZEMxU1VuaERSemN1ZG40MlVqRnZXRmhQVWs5dlJ6WjJZamwyYW5Jd01FTTNWRzkyTVhSbWQyZHciLCJuYW1lIjoiVGVzdCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BTG01d3UzUTVlWkhHRksxanVxam1GOGdrZ3dMbGx2UjBPMUVuRFdHQ1dRRT1zOTYtYyIsImdpdmVuX25hbWUiOiJUZXN0IiwibG9jYWxlIjoiZW4tR0IiLCJpYXQiOjE2NjY4NDkyNTQsImV4cCI6MTY2Njg1Mjg1NCwianRpIjoiMjY1MzFlYTQyYWM0ZDQ3NDRlYmUxYzkwYmY4OTJjYjY1MTQ3MGRjNiJ9.JyCWfIDDhGm86Lp9VPeh9kS1FeWWiCWTicPP4w3AjMb60J_EXvJxmypBnARGJnJjQxRfH0tHxAq3X-PYXzFYwYtkPbSmdtQwQkCBB92MgUfVzBW2SJYFEen_i7lWswNpof4-3gan6bJxLqUngg8sSaGEBgu9yaTKcPrBwDyBPYgPBi3LvVApMzuD0vBLMcf_SBAcOsBh43BQlWWgnw5mHT0A6ONRwBQAkMzQkfNywAto0RKFRAiuziOR3DX4MOISAAufyeMhFYCl9emteY4JI0I1B04c47--e33I8VKZR3FKm3CqgXxYP02GJIFHSxU8RIdMnDkmxCntORQdBHe6Ew
  // &authuser=0
  // &prompt=consent
}
