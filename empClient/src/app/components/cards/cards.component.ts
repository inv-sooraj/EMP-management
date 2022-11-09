import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { JobRequestService } from 'src/app/service/job-request.service';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css'],
})
export class CardsComponent implements OnInit {
  constructor(
    private jobService: JobService,
    private jobRequestService: JobRequestService,
    private toastService: ToastrService
  ) {}

  jobDataList: Array<any> = [];

  pagerInfo: any = {};

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 0;

  search: string = '';

  sortDesc: boolean = false;
  qualifications = this.jobService.qualifications;

  ngOnInit(): void {
    this.listJobs();
    this.getAppliedJobs();
  }

  listJobs(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append(
        'limit',
        this.limit ? this.limit : (window.innerHeight / 90).toFixed(0)
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
