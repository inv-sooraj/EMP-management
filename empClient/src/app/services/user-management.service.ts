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
  userDetails(userId: any): Observable<any> {
    return this.http.get(environment.apiUrl + '/users/' + userId);
  }
  //To update user details

  updateUser(userId: number, arg: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/users/' + userId, arg);
  }

  // pagination
  navPage(info: any): Observable<any> {
    return this.http.get(environment.apiUrl + '/users/list', { params: info });
  }

  //To get user deatils
  getCurrentUserDetails(): Observable<any> {
    return this.http.get(environment.apiUrl + '/login');
  }

  // download as csv format
  downloadCsv(): Observable<any> {
    return this.http.get(environment.apiUrl + '/users/download', {
      responseType: 'blob',
      observe: 'response',
    });
  }
  // change user password
  changePassword(info: any): Observable<any> {
    return this.http.put(environment.apiUrl + '/users/changepassword', info);
  }

  // Delete multiple users using checkbox
  deleteUsers(checked: number[]) {
    return this.http.put(environment.apiUrl + '/users/deleteall ', checked);
  }
  // get image
  getProfileImage(): Observable<any> {
    return this.http.get(environment.apiUrl + '/users/viewProfile',{responseType:'blob'});
  }

  // upload image
  updateUserDetails(body: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/users/uploadimage', body);
  }
}
