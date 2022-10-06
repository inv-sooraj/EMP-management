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
  role: number;

  constructor(private jobService: JobService, private modalService: NgbModal) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  jobList: any;

  page: number = 1;

  sortBy: string = 'job_id';
  sortDesc: boolean = false;

  limit: number = 5;

  search: string = '';

  // tableHeight: number = 73 * (this.limit + 1);
  qualifications = this.jobService.qualifications;

  status = this.jobService.status;

  statusCount: { [key: string]: number } = {};

  ngOnInit(): void {
    this.listJobs();
    this.getStat();
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
    
    console.log('sort by : ', sortBy,", desc : ",this.sortDesc);

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
      .append('desc', this.sortDesc)
      .append('filter', this.selectedStatus)
      .append('search', this.search);

    this.jobService.getJobs(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobList = response;
        this.getStat();
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

  changeJobStatus(jobId: number, status: number): void {
    this.jobService.changeJobStatus(jobId, status).subscribe({
      next: (response: any) => {
        console.log('Status Changed', response);
        this.listJobs();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  changeJobsStatus(status: number): void {
    if (this.checked.length <= 0) {
      return;
    }

    this.jobService.changeJobsStatus(this.checked, status).subscribe({
      next: (response: any) => {
        console.log('Updated', this.checked, ' : ', status, response);
        this.listJobs();
      },
      error(err) {
        console.log(err);
      },
    });

    (document.getElementById('selectAll') as HTMLInputElement).checked = false;
  }

  getStat(): void {
    this.statusCount = {
      PENDING: 0,
      APPROVED: 0,
      COMPLETED: 0,
      DELETED: 0,
    };
    this.jobService.getStat().subscribe({
      next: (response: any) => {
        console.log('Stat', response);

        response.forEach((element: any) => {
          console.log(element);

          this.statusCount[this.status[element.status]] = element.count;
        });
      },
      error(err) {
        console.log(err);
      },
    });
  }

  selectedStatus: number = 4;
}
