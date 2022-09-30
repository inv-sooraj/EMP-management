import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
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
    private jobRequestService: JobRequestService
  ) {}

  jobList: any;

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 5;

  search: string = '';

  // tableHeight: number = 73 * (this.limit + 1);

  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };

  ngOnInit(): void {
    this.listJobs();
    this.getAppliedJobs();
  }

  numSeq(n: number): Array<number> {
    return Array(n);
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
    if (this.jobList.result.length <= 1) {
      return;
    }
    console.log('sort by : ', sortBy);

    this.sortBy = sortBy;
    this.page = 1;
    this.listJobs();
  }

  setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.listJobs();
    // this.tableHeight = 73 * (this.limit + 1);
  }

  setSearch() {
    console.log(this.search);
    this.listJobs();
  }

  listJobs(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sortBy', this.sortBy)
      .append('view', '5')
      .append('search', this.search);

    this.jobService.getJobs(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobList = response;
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  deleteJob(jobId: number) {
    this.jobService.deleteJob(jobId).subscribe({
      next: (response: any) => {
        console.log('deleted', jobId, response);
        this.listJobs();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  applyJob(jobId: number): void {
    console.log(jobId);

    this.jobRequestService.addJobRequests(jobId).subscribe({
      next: (response: any) => {
        console.log(response);
        alert('Applied for Job');
        this.getAppliedJobs();
      },
      error(err) {
        console.log(err);
        alert(err.error.message);
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
}
