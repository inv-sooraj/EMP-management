import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class JobRequestManagementService {
  constructor(private http: HttpClient) {}

  // User can apply job requests
  jobRequest(id: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/jobrequest/', id);
  }

  // employer can get job requests
  getJobRequest(): Observable<any> {
    return this.http.get(environment.apiUrl + '/jobrequest');
  }

  // Pagination
  navPage(info: any): Observable<any> {
    return this.http.get(environment.apiUrl + '/jobrequest/getjobrequest', {
      params: info,
    });
  }

  changeStatus(id:any,data:any): Observable<any> {
    return this.http.put(environment.apiUrl + '/jobrequest/confirm/' + id ,data);
  }
}
