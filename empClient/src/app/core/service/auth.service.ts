import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  login(body: any): Observable<any> {
    return this.http.post(this.apiUrl + '/login', body);
  }

  register(body: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/users', body);
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('name');
    localStorage.removeItem('role');
  }

  forgotPassword(body: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/login/forgot-password', body);
  }

  resetPassword(token: string, body: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/login/reset-password/'+token, body);
  }
}
