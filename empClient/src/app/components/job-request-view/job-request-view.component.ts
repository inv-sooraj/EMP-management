import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobRequestManagementService } from 'src/app/services/job-request-management.service';

@Component({
  selector: 'app-job-request-view',
  templateUrl: './job-request-view.component.html',
  styleUrls: ['./job-request-view.component.css'],
})
export class JobRequestViewComponent implements OnInit {
  page: number = 1;
  search: string = '';
  details: any;
  jobRequestId: any;
  role = Number(localStorage.getItem('key'));

  status: { [key: number]: string } = {
    0: 'APPROVED ',
    1: 'PENDING',
    2: 'REJECTED ',
  };
  constructor(
    private service: JobRequestManagementService,
    private route: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.pagination();
  }
  gotoPage(page: number) {
    this.page = page;
    this.pagination();
  }

  navPrevious() {
    this.page -= 1;

    this.pagination();
  }
  numSeq(n: number) {
    return Array(n);
  }

  navNext() {
    this.page += 1;

    this.pagination();
  }

  // jobRequestView() {
  //   this.service.getJobRequest().subscribe({
  //     next: (response: any) => {
  //       console.log(response);
  //       this.details = response;
  //     },
  //     error: (error: any) => {
  //       console.log(error);
  //     },
  //   });
  // }

  logOut() {
    localStorage.clear();
    this.route.navigate(['login']);
  }

  getParam(): HttpParams {
    return new HttpParams()
      .append('page', this.page)
      .append('limit', 4)
      .append('sort', 'job_req_id')
      .append('search', this.search)
      .append('filter', 2);
  }
  pagination() {
    this.service.navPage(this.getParam()).subscribe({
      next: (response: any) => {
        console.log(response);
        this.details = response;
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  remark: string = '';

  action: number = 1;

  open(content: any, action: number) {
    this.action = action;

    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  sendConfim() {
    let data = {
      remarks: this.remark,
      status: this.action,
    };
    this.service.changeStatus(this.jobRequestId, data).subscribe({
      next: (response: any) => {
        console.log(response);
        this.pagination();
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }
}
