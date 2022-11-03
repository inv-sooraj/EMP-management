import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiUrl: string = environment.apiUrl;
  timer: number = 20;

  tokenExpiry: any;

  constructor(private http: HttpClient, private router: Router) {}

  login(body: any): Observable<any> {
    return this.http.post(this.apiUrl + '/login', body);
  }

  loginWithGoogle(): Observable<any> {
    return this.http.get(this.apiUrl + '/');
  }

  register(body: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/users', body);
  }

  startTimer() {
    let t = setInterval(() => {
      if (this.timer <= 0) {
        console.log('expired');

        clearInterval(t);
      } else {
        this.timer--;
      }
    }, 1000);
    this.timer = 20;
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('name');
    localStorage.removeItem('role');
    sessionStorage.clear()
    this.router.navigate(['login']);
  }

  forgotPassword(body: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/login/forgot-password', body);
  }

  resetPassword(token: string, body: any): Observable<any> {
    return this.http.put(
      environment.apiUrl + '/login/reset-password/' + token,
      body
    );
  }

  newToken() {
    let token = localStorage.getItem('refreshToken');
    return this.http.put(environment.apiUrl + '/login', token);
  }

  checkExpired(): boolean {
    let expiry = localStorage.getItem('accessTokenExpiry') as string;
    if (expiry) {
      let diff =
        (Date.parse(expiry) -
          (Date.now() + new Date().getTimezoneOffset() * 60000)) /
        (1000 * 60);
      console.log(diff);

      if (diff < 11) {
        return true;
      }
    }
    return false;
  }

  verifyUser(body: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/users/verify-user', body);
  }
}
