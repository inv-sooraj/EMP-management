import { HttpParams } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { AuthService } from '../../service/auth.service';
import { OAuthGoogleService } from '../../service/oauthgoogle.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  // flag: boolean = false;

  constructor(
    private router: Router,
    private service: AuthService,
    private googleService: OAuthGoogleService,
    private toastService: ToastrService
  ) {}

  ngOnInit(): void {
    this.service.logout();
    // this.googleAuthSDK();
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
        // if (this.flag == true) {
        //   // sessionStorage.setItem('accessToken', response.accessToken.value);
        //   // sessionStorage.setItem('refreshToken', response.refreshToken.value);
        //   sessionStorage.setItem('userName', this.loginForm.controls['username'].value);
        //   sessionStorage.setItem('password', this.loginForm.get('password')?.value);
        // }
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
  // ------------------------------------------------------------------------------------------------------//

  // auth2: any;
  // @ViewChild('googleLogin', { static: true }) loginElement!: ElementRef;

  // callLogin() {
  //   this.auth2.attachClickHandler(
  //     this.loginElement.nativeElement,
  //     {},
  //     (googleAuthUser: any) => {
  //       console.log('Token => ' + googleAuthUser.getAuthResponse().id_token);

  //       console.log(JSON.stringify(googleAuthUser, undefined, 4));
  //       console.log(googleAuthUser.getBasicProfile().getEmail());

  //       let queryParam = new HttpParams().append(
  //         'email',
  //         googleAuthUser.getBasicProfile().getEmail()
  //       );

  //       this.googleService.verifyEmail(queryParam).subscribe({
  //         next: (response: any) => {
  //           console.log(response);

  //           if (response == 'NATIVE_USER') {
  //             alert('Native User');
  //             this.router.navigateByUrl('login');
  //           } else if (response == 'GOOGLE_USER') {
  //             let idToken = googleAuthUser.getAuthResponse().id_token;

  //             this.googleService.login(idToken).subscribe({
  //               next: (response: any) => {
  //                 console.log(JSON.stringify(response, undefined, 4));

  //                 localStorage.setItem(
  //                   'accessTokenExpiry',
  //                   response.accessToken.expiry
  //                 );
  //                 localStorage.setItem(
  //                   'accessToken',
  //                   response.accessToken.value
  //                 );
  //                 localStorage.setItem(
  //                   'refreshToken',
  //                   response.refreshToken.value
  //                 );
  //                 localStorage.setItem('name', response.name);
  //                 localStorage.setItem('role', response.role);

  //                 if (response.role == 1) this.router.navigate(['job-list']);
  //                 else this.router.navigate(['job-apply']);

  //                 window.location.reload();
  //               },
  //               error(err) {
  //                 console.log(err);
  //               },
  //             });
  //           } else if (response == 'NOT_PRESENT') {
  //             let idToken = googleAuthUser.getAuthResponse().id_token;

  //             let role = 1;

  //             this.googleService.register(idToken, role).subscribe({
  //               next: (response: any) => {
  //                 console.log(JSON.stringify(response, undefined, 4));

  //                 localStorage.setItem(
  //                   'accessTokenExpiry',
  //                   response.accessToken.expiry
  //                 );
  //                 localStorage.setItem(
  //                   'accessToken',
  //                   response.accessToken.value
  //                 );
  //                 localStorage.setItem(
  //                   'refreshToken',
  //                   response.refreshToken.value
  //                 );
  //                 localStorage.setItem('name', response.name);
  //                 localStorage.setItem('role', response.role);

  //                 if (response.role == 1) this.router.navigate(['job-list']);
  //                 else this.router.navigate(['job-apply']);

  //                 window.location.reload();
  //               },
  //               error(err) {
  //                 console.log(err);
  //               },
  //             });
  //           }
  //         },
  //         error(err) {
  //           console.log(err);
  //         },
  //       });
  //     },
  //     (error: any) => {
  //       alert(JSON.stringify(error, undefined, 2));
  //     }
  //   );
  // }

  // googleAuthSDK() {
  //   (<any>window)['googleSDKLoaded'] = () => {
  //     (<any>window)['gapi'].load('auth2', () => {
  //       this.auth2 = (<any>window)['gapi'].auth2.init({
  //         client_id:
  //           '97587627218-qgaitnpfp5q9nu8br0k4li9762b57scc.apps.googleusercontent.com',
  //         plugin_name: 'login',
  //         cookiepolicy: 'single_host_origin',
  //         scope: 'profile email',
  //       });
  //       this.callLogin();
  //     });
  //   };

  //   (function (d, s, id) {
  //     let js,
  //       fjs = d.getElementsByTagName(s)[0];
  //     if (d.getElementById(id)) {
  //       return;
  //     }
  //     js = d.createElement('script');
  //     js.id = id;
  //     js.src = 'https://apis.google.com/js/platform.js?onload=googleSDKLoaded';
  //     fjs?.parentNode?.insertBefore(js, fjs);
  //   })(document, 'script', 'google-jssdk');
  // }
  // rememberMe() {
  //   this.flag = true;
  // }
}
