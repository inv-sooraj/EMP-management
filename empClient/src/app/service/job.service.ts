import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class JobService {
  private apiUrl: string = environment.apiUrl;

  public qualifications: { [key: number]: string } = {
    0: 'NIL',
    1: 'SSLC ',
    2: 'PLUS TWO',
    3: 'UG ',
    4: 'PG ',
  };

  public status: { [key: number]: string } = {
    0: 'PENDING',
    1: 'APPROVED',
    2: 'COMPLETED',
    3: 'DELETED',
  };

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

  changeJobStatus(jobId: number, status: number): Observable<any> {
    return this.http.put(this.apiUrl + '/job/status/' + jobId, status);
  }

  changeJobsStatus(requestBody: any, status: number): Observable<any> {
    return this.http.put(
      this.apiUrl + '/job/status/selected/' + status,
      requestBody
    );
  }

  downloadCsv(): Observable<any> {
    return this.http.get(this.apiUrl + '/job/download', {
      responseType: 'blob',
      observe: 'response',
    });
  }

  getStat(): Observable<any> {
    return this.http.get(this.apiUrl + '/job/stat');
  }
}
