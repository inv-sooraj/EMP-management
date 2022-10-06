import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobRequestService } from 'src/app/service/job-request.service';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-request-list',
  templateUrl: './job-request-list.component.html',
  styleUrls: ['./job-request-list.component.css'],
})
export class JobRequestListComponent implements OnInit {
  constructor(
    private jobRequestService: JobRequestService,
    private jobService: JobService,
    private modalService: NgbModal
  ) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  jobList: any;

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 5;

  search: string = '';

  // tableHeight: number = 73 * (this.limit + 1);

  status = this.jobService.status;

  role: number;

  ngOnInit(): void {
    this.listJobRequests();
  }

  numSeq(n: number): Array<number> {
    return Array(n);
  }

  prevPage() {
    this.page -= 1;
    this.listJobRequests();
  }

  gotoPage(page: number) {
    this.page = page;
    this.listJobRequests();
  }

  nextPage() {
    this.page += 1;
    this.listJobRequests();
  }

  setSort(sortBy: string) {
    if (this.jobList.result.length <= 1) {
      return;
    }
    console.log('sort by : ', sortBy);

    this.sortBy = sortBy;
    this.page = 1;
    this.listJobRequests();
  }

  setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.listJobRequests();
    // this.tableHeight = 73 * (this.limit + 1);
  }

  setSearch() {
    console.log(this.search);
    this.listJobRequests();
  }

  listJobRequests(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sortBy', this.sortBy)
      .append('search', this.search);

    this.jobRequestService.getJobRequests(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobList = response;
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  updateStatus(jobRequestId: number, status: number): void {
    let body = {
      status: status,
      remark: this.remark,
    };

    this.jobRequestService.updateStatus(jobRequestId, body).subscribe({
      next: (response: any) => {
        console.log(response);
        this.listJobRequests();
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  remark: string = '';

  jobRequestId: number = 0;

  action: number = 1;

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }
}
