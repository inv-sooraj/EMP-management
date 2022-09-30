import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobManagementService } from 'src/app/services/job-management.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-job-view',
  templateUrl: './job-view.component.html',
  styleUrls: ['./job-view.component.css'],
})
export class JobViewComponent implements OnInit {
  details: any;
  page: number = 1;


  flag:number=0;
  search: string = '';
  view:number=3;
  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };

  status: { [key: number]: string } = {
    0: 'Deleted ',
    1: 'Active',
    2: 'Pending ',
    3: 'Rejected ',
  };
  constructor(
    private service: JobManagementService,
    private route: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    // this.jobView();
    this.pagination();
  }

  // logOut() {
  //   localStorage.clear();
  //   this.route.navigate(['login']);
  // }

  // jobView() {
  //   this.service.listJob().subscribe((data) => {
  //     this.details = data;
  //     console.log(this.details);
  //   });
  // }

  jobDelete(arg: any) {
    this.service.deleteJob(arg,this.flag).subscribe({
      next: (response: any) => {
        console.log(response);
        // this.jobView();
        this.pagination();
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  jobEdit(arg: any) {
    this.route.navigate(['editJob' + arg]);
  }

  // addJob() {
  //   // this.service.status=0;
  //   this.route.navigate(['editJob']);
  // }

  getParam(): HttpParams {
    return new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sort', 'job_id')
      .append('search', this.search)
      .append('filter', this.view);
  }

  setSearch() {
    this.page = 1;
    this.pagination();
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

  numSeq(n: number) {
    return Array(n);
  }

  navNext() {
    this.page += 1;

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

  // ##################################################

  csvDownload() {
    this.service.downloadCsv().subscribe({
      next: (response: any) => {
        console.log(response);

        let anchor = document.createElement('a');
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(
          new Blob([response.body], { type: response.body.type })
        );
        anchor.click();
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  limit: number = 5;
  setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.pagination();
    // this.tableHeight = 73 * (this.limit + 1);
  }

  setView() {
    console.log(this.view);
    this.page = 1;
    this.pagination();
    // this.tableHeight = 73 * (this.limit + 1);
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  jobId: number = 0;
}
