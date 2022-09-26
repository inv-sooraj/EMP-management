import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  public status: any = 0
  public jobId: any

  constructor(private http: HttpClient) { }

  getJobs(queryParam: HttpParams): Observable<any> {
    return this.http.get(`${environment.apiUrl}/job`, { params: queryParam });
  }
  downloadJobs(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/job/download`, { responseType: "blob", observe: "response" });
  }
  getJobCount(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/job/count`);
  }

  jobAdd(data: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/job`, data);
  }
  jobEdit(data: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}/job/${this.jobId}`, data);
  }
  getJob(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/job/${this.jobId}`);
  }
  deleteJob(id: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}/job/delete`, id);
  }
  deleteJobs(jobIds: Array<number>): Observable<any> {
    return this.http.put(environment.apiUrl + '/job/delete/selected', jobIds);
  }



}
