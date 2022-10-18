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

  jobList: any;

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 5;

  search: string = '';

  // tableHeight: number = 73 * (this.limit + 1);

  sortDesc: boolean = false;


  qualifications = this.jobService.qualifications;

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

  setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.listJobs();
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
      .append('desc', this.sortDesc)
      .append('apply', true)
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

  applyJob(jobId: number): void {
    console.log(jobId);

    this.jobRequestService.addJobRequests(jobId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.toastService.success('Job applied!')
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
