import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  API_URL = environment.apiUrl
  role: any
  constructor(private http: HttpClient) { }

  loginUser(data: any): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, data);
  }

  registerUser(data: any): Observable<any> {
    return this.http.post(`${this.API_URL}/users`, data);
  }

  regUser(data: any): Observable<any> {
    return this.http.post(`${this.API_URL}/users/admin`, data);
  }

  forgotPswd(data: any): Observable<any> {
    return this.http.post(`${this.API_URL}/users/resetPswdEmail`, data);
  }
  resetPswd(data :any,token:any){
    return this.http.post(`${this.API_URL}/users/forgotPswd/`+token, data);
  }

}


