import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  API_URL = environment.apiUrl
  constructor(private http:HttpClient) { }

  loginuser(data: any): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, data);
  }

}
