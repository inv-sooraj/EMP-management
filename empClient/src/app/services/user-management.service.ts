import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserManagementService {
  // userRole: any;

  userId: any;
  // public id: any;

  constructor(private http: HttpClient) {}

  // To display all users
  display(): Observable<any> {
    return this.http.get(environment.apiUrl + '/users');
  }

  //To add new user
  userAdd(info: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/users/details', info);
  }

  // To delete a user
  deleteUser(arg: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/users/delete/' + arg, '');
  }

  // To get employee details
  userDetails(): Observable<any> {
    return this.http.get(environment.apiUrl + '/users/' + this.userId);
  }
  //To update user details

  updateUser(arg: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/users/' + this.userId, arg);
  }
  navPage(info: any): Observable<any> {
    return this.http.get(environment.apiUrl + '/users/list', { params: info });
  }

  getCurrentUserDetails(): Observable<any> {
    return this.http.get(environment.apiUrl + '/login');
  }

  downloadCsv(): Observable<any> {
    return this.http.get(environment.apiUrl + '/users/download', {
      responseType: 'blob',
      observe: 'response',
    });
  }
}
