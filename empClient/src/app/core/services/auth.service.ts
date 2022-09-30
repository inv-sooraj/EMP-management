import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  header = new HttpHeaders();


  constructor(private http:HttpClient) { }
  
  IsLoggedIn(){
    return !!localStorage.getItem('accessToken');
  }

  getToken(){
    return localStorage.getItem("accessToken")
  }
  logUser(info: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/login', info);
  }

  regUser(info: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/users', info);
  }
}
