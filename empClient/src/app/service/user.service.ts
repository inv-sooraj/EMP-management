import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<any> {
    return this.http.get(this.apiUrl + '/users/');
  }

  getUser(userId: number): Observable<any> {
    return this.http.get(this.apiUrl + '/users/' + userId);
  }

  getUsers(quearyParam: HttpParams): Observable<any> {
    return this.http.get(this.apiUrl + '/users/page/', { params: quearyParam });
  }

  editUser(body: any): Observable<any> {
    return this.http.put(this.apiUrl + '/users/details', body);
  }

  updateUser(userId: number, body: any): Observable<any> {
    return this.http.put(this.apiUrl + '/users/user-update/' + userId, body);
  }
  deleteUser(userId: number): Observable<any> {
    return this.http.put(this.apiUrl + '/users/delete/' + userId, {});
  }

  deleteUsers(userIds: Array<number>): Observable<any> {
    return this.http.put(this.apiUrl + '/users/delete/selected', userIds);
  }

  downloadCsv(): Observable<any> {
    return this.http.get(this.apiUrl + '/users/download', {
      responseType: 'blob',
      observe: 'response',
    });
  }
  uploadProfile(requestBody: FormData): Observable<any> {
    return this.http.put(this.apiUrl + '/users/profile', requestBody);
  }

  getProfile(): Observable<any> {
    return this.http.get(this.apiUrl + '/users/profile', {
      responseType: 'blob',
    });
  }
}
