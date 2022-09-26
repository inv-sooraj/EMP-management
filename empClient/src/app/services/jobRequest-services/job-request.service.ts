import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class JobRequestService {
  status: any
  reqId: any

  constructor(private http: HttpClient) { }

  getJobRequests(queryParam: HttpParams): Observable<any> {
    return this.http.get(`${environment.apiUrl}/jobrequest`, { params: queryParam });
  }
  downloadJobRequests(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/jobrequest/download`, { responseType: "blob", observe: "response" });
  }
  getJobRequestsCount(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/jobrequest/count`);
  }

  reqAdd(data: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/jobrequest`, data);
  }
  reqEdit(data: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}/jobrequest`, data);
  }
  getreq(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/jobrequest/${this.reqId}`);
  }
  deletereq(id: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}/jobrequest/delete`, id);
  }
  deleteRequests(reqIds: Array<number>): Observable<any> {
    return this.http.put(environment.apiUrl + '/jobrequest/delete/selected', reqIds);
  }
}
