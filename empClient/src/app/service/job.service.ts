import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class JobService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addJob(requestBody: any): Observable<any> {
    return this.http.post(this.apiUrl + '/job', requestBody);
  }

  getJob(jobId: number): Observable<any> {
    return this.http.get(this.apiUrl + '/job/' + jobId);
  }

  getJobs(quearyParam: HttpParams): Observable<any> {
    return this.http.get(this.apiUrl + '/job/page/', { params: quearyParam });
  }

  editJob(jobId: number, requestBody: any): Observable<any> {
    return this.http.put(this.apiUrl + '/job/' + jobId, requestBody);
  }

  deleteJob(jobId: number): Observable<any> {
    return this.http.put(this.apiUrl + '/job/delete/' + jobId, {});
  }

  deleteJobs(jobIds: Array<number>): Observable<any> {
    return this.http.put(this.apiUrl + '/job/delete/selected', jobIds);
  }

  downloadCsv(): Observable<any> {
    return this.http.get(this.apiUrl + '/job/download',{responseType:'blob',observe:'response'});
  }
}
