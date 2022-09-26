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

  getUsers(quearyParam: HttpParams): Observable<any> {
    return this.http.get(this.apiUrl + '/users/page/', { params: quearyParam });
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
}
