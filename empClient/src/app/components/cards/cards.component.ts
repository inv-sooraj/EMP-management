import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css'],
})
export class CardsComponent implements OnInit {
  constructor(private jobService: JobService) {}
  jobList: any;

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 5;

  search: string = '';

  sortDesc: boolean = false;
  qualifications = this.jobService.qualifications;

  ngOnInit(): void {
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
}
