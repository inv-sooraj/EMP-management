import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl: string = environment.apiUrl;

  public qualifications = new Map<number, string>([
    [0, 'NIL'],
    [1, 'SSLC'],
    [2, 'PLUS TWO'],
    [3, 'UG'],
    [4, 'PG'],
  ]);

  public roles = new Map<number, string>([
    [0, 'EMPLOYEE'],
    [1, 'EMPLOYER'],
    [2, 'ADMIN'],
  ]);

  public status = new Map<number, string>([
    [0, 'INACTIVE'],
    [1, 'ACTIVE'],
    [2, 'DELETED'],
  ]);

  constructor(private http: HttpClient) {}

  registerUser(body: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/users/admin-register', body);
  }

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

  changePassword(body: any): Observable<any> {
    return this.http.put(this.apiUrl + '/users/change-password', body);
  }
  deleteUser(userId: number): Observable<any> {
    return this.http.put(this.apiUrl + '/users/delete/' + userId, {});
  }

  deleteUsers(userIds: Array<number>): Observable<any> {
    return this.http.put(this.apiUrl + '/users/delete/selected', userIds);
  }

  downloadCsv(quearyParam: HttpParams): Observable<any> {
    return this.http.get(this.apiUrl + '/users/download', {
      params: quearyParam,
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

  deleteProfilePic(userId:number): Observable<any> {
    return this.http.delete(this.apiUrl + '/users/profile/deleted' + userId);
  }

  getRoleStat(): Observable<any> {
    return this.http.get(this.apiUrl + '/users/role-stat');
  }

  deactivateUser() {
    return this.http.put(this.apiUrl + '/users/delete', '');
  }
}
