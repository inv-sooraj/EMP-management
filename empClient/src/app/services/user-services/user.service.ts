import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public status: any = 0
  public userId: any
  constructor(private http: HttpClient) { }

  getUserDetails(){
    return this.http.get(`${environment.apiUrl}/users/detail`);
  }

  getUsers(queryParam:HttpParams): Observable<any> {
    return this.http.get(`${environment.apiUrl}/users`,{ params: queryParam });
  }
  downloadUsers(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/users/download`, { responseType: "blob", observe: "response" });
  }
  getUsersCount(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/users/count`);
  }

  userAdd(data: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/users`, data);
  }
  userEdit(data: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}/users`, data);
  }
  getUser(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/users/${this.userId}`);
  }
  deleteUser(id: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}/users/delete`, id);
  }
  deleteUsers(userIds: Array<number>): Observable<any> {
    return this.http.put(environment.apiUrl + '/users/delete/selected', userIds);
  }

  uploadImageManager(selectedFile:any):Observable<any>{
    return this.http.put(environment.apiUrl+"/users/profilePic",selectedFile);
  }

  getProfilePic():Observable<any>{
    return this.http.get(environment.apiUrl+"/users/getImg",{ responseType: "blob" })

  }
  changePswd(data:any):Observable<any>{
    return this.http.put(environment.apiUrl+"/users/changepswd",data)
  }
}
