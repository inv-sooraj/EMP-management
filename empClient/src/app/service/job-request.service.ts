import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class JobRequestService {
  apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addJobRequests(jobId: number): Observable<any> {
    return this.http.post(this.apiUrl + '/job-request/' + jobId, '');
  }

  getJobRequests(quearyParam: HttpParams): Observable<any> {
    return this.http.get(this.apiUrl + '/job-request/page/', {
      params: quearyParam,
    });
  }

  getAppliedJob(): Observable<any> {
    return this.http.get(this.apiUrl + '/job-request/applied');
  }

  updateStatus(jobRequestId: number, requestBody: any): Observable<any> {
    return this.http.put(
      this.apiUrl + '/job-request/' + jobRequestId,
      requestBody
    );
  }
}
