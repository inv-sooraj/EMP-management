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
  ) { }
  jobList: any;

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 100;

  search: string = '';

  sortDesc: boolean = false;
  qualifications = this.jobService.qualifications;

  ngOnInit(): void {
    this.listJobs();
    this.getAppliedJobs()
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
}
