import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { JobRequestService } from 'src/app/service/job-request.service';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-apply',
  templateUrl: './job-apply.component.html',
  styleUrls: ['./job-apply.component.css'],
})
export class JobApplyComponent implements OnInit {
  constructor(
    private jobService: JobService,
    private jobRequestService: JobRequestService,
    private toastService: ToastrService
  ) {}

  pagerInfo: any;

  jobDataList: Array<any> = [];

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 0;

  search: string = '';

  // tableHeight: number = 73 * (this.limit + 1);

  sortDesc: boolean = false;

  qualifications = this.jobService.qualifications;

  ngOnInit(): void {
    this.listJobs();
    this.getAppliedJobs();
  }

  numSeq(n: number): Array<number> {
    let arr = new Array<number>();

    if (this.pagerInfo.numPages <= 5) {
      for (let index = 1; index <= this.pagerInfo.numPages; index++) {
        arr.push(index);
      }
      return arr;
    }

    let start;
    if (this.pagerInfo.currentPage > this.pagerInfo.numPages - 2) {
      start = this.pagerInfo.numPages - 2;
    } else {
      start = this.pagerInfo.currentPage < 4 ? 3 : this.pagerInfo.currentPage;
    }

    for (let index = start - 2; index < start + 3; index++) {
      arr.push(index);
    }

    // return Array(n);
    return arr;
  }

  prevPage() {
    this.page -= 1;
    this.listJobs();
  }

  gotoPage(page: number) {
    this.page = page;
    this.listJobs();
  }

  nextPage() {
    this.page += 1;
    this.listJobs();
  }

  setSort(sortBy: string) {
    this.jobDataList = [];

    if (this.sortBy == sortBy) {
      this.sortDesc = this.sortDesc ? false : true;
    } else {
      this.sortDesc = false;
    }

    console.log('sort by : ', sortBy, ', desc : ', this.sortDesc);

    this.sortBy = sortBy;
    this.page = 1;
    this.listJobs();
  }

  resetList() {
    this.jobDataList = [];

    console.log(this.limit);
    this.page = 1;
    this.listJobs();
  }

  setSearch() {
    this.page = 1;

    this.jobDataList = [];
    console.log(this.search);
    this.listJobs();
  }

  listJobs(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append(
        'limit',
        this.limit ? this.limit : (window.innerHeight / 100).toFixed(0)
      )
      .append('sortBy', this.sortBy)
      .append('desc', this.sortDesc)
      .append('apply', true)
      .append('search', this.search);

    this.jobService.getJobs(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);
        this.pagerInfo = response.pagerInfo;

        this.pagerInfo['numPages'] = response.numPages;
        this.pagerInfo['currentPage'] = response.currentPage;

        if (this.limit == 0) {
          this.jobDataList.push(...response.result);
        } else {
          this.jobDataList = response.result;
        }
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  applyJob(jobId: number): void {
    console.log(jobId);

    this.jobRequestService.addJobRequests(jobId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.toastService.success('Job applied!');
        this.getAppliedJobs();
      },
      error: (err) => {
        console.log(err);
        this.toastService.error(err.error.message);
      },
    });
  }

  appliedJobs: Array<number> = [];
  getAppliedJobs() {
    this.jobRequestService.getAppliedJob().subscribe({
      next: (response: any) => {
        console.log(response);
        this.appliedJobs = response;
      },
      error(err) {
        console.log(err);
      },
    });
  }

  selectedStatus: number = 4;

  throttle = 300;
  scrollDistance = 1;
  scrollUpDistance = 2;
  onScrollDown() {
    if (this.limit) {
      return;
    }
    this.page += 1;
    this.listJobs();
  }
}
