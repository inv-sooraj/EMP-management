import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobManagementService } from 'src/app/services/job-management.service';
import { JobRequestManagementService } from 'src/app/services/job-request-management.service';

@Component({
  selector: 'app-user-job-view',
  templateUrl: './user-job-view.component.html',
  styleUrls: ['./user-job-view.component.css'],
})
export class UserJobViewComponent implements OnInit {
  details: any;
  // text: string = 'apply';
  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };
  constructor(
    private route: Router,
    private service: JobManagementService,
    private request: JobRequestManagementService
  ) {}
  page: number = 1;
  search: string = '';
  ngOnInit(): void {
    // this.userJobView();
    this.pagination();
  }

  logOut() {
    localStorage.clear();
    this.route.navigate(['login']);
  }

  // userJobView() {
  //   this.service.listJob().subscribe((data) => {
  //     this.details = data;
  //     console.log(this.details);
  //   });
  // }
  requestJob(id: any) {
    this.request.jobRequest(id).subscribe({
      next: (response: any) => {
        console.log(response);
        // this.text="applied";
        // this.jobView();
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  getParams() {
    return new HttpParams()
      .append('page', this.page)
      .append('limit', 4)
      .append('sort', 'user_id')
      .append('search', this.search);
  }
  pagination() {
    this.service.navPage(this.getParams()).subscribe({
      next: (response: any) => {
        console.log('View', response);

        this.details = response;
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  previous() {
    this.page -= 1;

    this.pagination();
  }
  next() {
    this.page += 1;

    this.pagination();
  }
  numSeq(n: number) {
    return Array(n);
  }
  gotoPage(page: number) {
    this.page = page;
    this.pagination();
  }
}
