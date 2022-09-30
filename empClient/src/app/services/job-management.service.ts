import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class JobManagementService {
  constructor(private http: HttpClient) {}

  // To display all jobs
  listJob() {
    return this.http.get(environment.apiUrl + '/jobs');
  }
  // To add new job
  addJob(info: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/jobs', info);
  }
  //To edit job details
  getJobById(id: any) {
    return this.http.get(environment.apiUrl + '/jobs/' + id);
  }
  // To delete a user
  deleteJob(arg: any,flag:number): Observable<any> {
    return this.http.put(environment.apiUrl + '/jobs/delete/' + arg, flag);
  }

  //To update job details
  updateJobDetails(arg: any, jobId: number): Observable<any> {
    return this.http.put(environment.apiUrl + '/jobs/' + jobId, arg);
  }

  // To display job as pagination
  navPage(info: any): Observable<any> {
    return this.http.get(environment.apiUrl + '/jobs/getjob', { params: info });
  }

  downloadCsv() {
    return this.http.get(environment.apiUrl + '/jobs/download', {
      responseType: 'blob',
      observe: 'response',
    });
  }
}
