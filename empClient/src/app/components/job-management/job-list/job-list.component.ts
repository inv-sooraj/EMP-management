import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from 'src/app/core/service/auth.service';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.css'],
})
export class JobListComponent implements OnInit {
  role: number;

  constructor(private jobService: JobService, private modalService: NgbModal,private service:AuthService) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  jobList: any;

  page: number = 1;

  sortBy: string = 'jobId';
  sortDesc: boolean = false;

  limit: number = 5;

  search: string = '';

  qualifications = this.jobService.qualifications;

  status = this.jobService.status;

  statusCount: { [key: string]: number } = {};

  ngOnInit(): void {
    this.listJobs();
    this.service.checkExp();
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
      .append('page', 1)
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

  checkedJobIds: Set<number> = new Set();

  checkAllButton(): boolean {
    let temp = true;

    if (this.jobList) {
      this.jobList.result.forEach((val: any) => {
        if (!this.checkedJobIds.has(val.jobId)) {
          temp = false;
        }
      });
    }

    return temp;
  }

  checkedUser(event: any) {
    if (event.target.checked) {
      this.checkedJobIds.add(parseInt(event.target.attributes.value.value));
    } else {
      this.checkedJobIds.delete(parseInt(event.target.attributes.value.value));
    }

    console.log(this.checkedJobIds);
  }

  checkAll(event: any) {
    this.jobList.result.forEach((element: any) => {
      if (event.target.checked) {
        if (!this.checkedJobIds.has(element.jobId)) {
          this.checkedJobIds.add(element.jobId);
        }
      } else {
        this.checkedJobIds.delete(element.jobId);
      }
    });

    console.log(this.checkedJobIds);
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
    if (this.checkedJobIds.size <= 0) {
      return;
    }

    this.jobService
      .changeJobsStatus(Array.from(this.checkedJobIds), status)
      .subscribe({
        next: (response: any) => {
          console.log('Updated', this.checkedJobIds, ' : ', status, response);
          this.listJobs();
        },
        error(err) {
          console.log(err);
        },
      });
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
