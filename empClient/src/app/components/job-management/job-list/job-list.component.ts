import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.css'],
})
export class JobListComponent implements OnInit {
  constructor(private jobService: JobService, private modalService: NgbModal) {}

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

  checked: Array<number> = [];

  checkedUser(event: any) {
    if (event.target.checked) {
      this.checked.push(Number(event.target.attributes.value.value));

      if (this.jobList.result.length == this.checked.length) {
        (document.getElementById('selectAll') as HTMLInputElement).checked =
          true;
      }
    } else {
      this.checked.splice(
        this.checked.indexOf(Number(event.target.attributes.value.value)),
        1
      );
      (document.getElementById('selectAll') as HTMLInputElement).checked =
        false;
    }

    console.log(this.checked);
  }

  checkAll(event: any) {
    this.jobList.result.forEach((element: any) => {
      (
        document.getElementById('checkbox' + element.jobId) as HTMLInputElement
      ).checked = event.target.checked;

      if (event.target.checked) {
        if (!this.checked.includes(element.jobId)) {
          this.checked.push(element.jobId);
        }
      } else {
        this.checked.splice(this.checked.indexOf(element.jobId), 1);
      }
    });
    console.log(this.checked);
  }

  deleteJobs(): void {
    if (this.checked.length <= 0) {
      return;
    }
    this.jobService.deleteJobs(this.checked).subscribe({
      next: (response: any) => {
        console.log('deleted', this.checked, response);
        this.listJobs();
      },
      error(err) {
        console.log(err);
      },
    });

    (document.getElementById('selectAll') as HTMLInputElement).checked = false;
  }

  downloadCsv(): void {
    this.jobService.downloadCsv().subscribe({
      next: (response: any) => {
        let anchor = document.createElement('a');
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(
          new Blob([response.body], { type: response.body.type })
        );
        anchor.click();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  jobId: number = 0;

  open(content: any, jobId?: number) {
    this.jobId = jobId ? jobId : 0;
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }
}
