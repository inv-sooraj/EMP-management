import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/service/auth.service';
import { JobRequestService } from 'src/app/service/job-request.service';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.css'],
})
export class JobListComponent implements OnInit {
  role: number;
  today: string = '';

  constructor(
    private jobService: JobService,
    private jobRequestService: JobRequestService,
    private modalService: NgbModal,
    private service: AuthService,
    private toastService: ToastrService
  ) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  pagerInfo: any;

  jobDataList: Array<any> = [];

  page: number = 1;

  sortBy: string = 'jobId';

  sortDesc: boolean = false;

  limit: number = 0;

  search: string = '';

  qualifications = this.jobService.qualifications;

  status = this.jobService.status;

  showSpinner: boolean = false;

  statusCount: { [key: string]: number } = {};

  ngOnInit(): void {
    this.listJobs();
    this.service.checkExpired();

    this.today = new Date().toISOString().split('T')[0];

    this.csvData.endDate.value = this.today;
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
    if (this.page == page) return;
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
    this.page = 1;
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
      .append('filter', this.selectedStatus)
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
        this.getStat();
      },
      error: (err: any) => {
        console.error(err);
      },
      complete: () => {
        this.jobRequestCount();
      },
    });
  }

  checkedJobIds: Set<number> = new Set();

  checkAllButton(): boolean {
    let temp = true;

    if (this.pagerInfo) {
      this.jobDataList.forEach((val: any) => {
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
    this.jobDataList.forEach((element: any) => {
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

  csvData: any = {
    status: new Set<number>(this.status.keys()),
    startDate: new FormControl('', [Validators.required]),
    endDate: new FormControl('', [Validators.required]),
  };

  changeCsvStat(event: any, stat: any, data: Set<number>): void {
    if (event.target.checked) {
      data.add(stat);
    } else {
      data.delete(stat);
    }

    console.log(this.csvData);
  }

  downloadCsv(): void {
    if (!this.csvData.startDate.valid || !this.csvData.endDate.valid) {
      this.csvData.startDate.touched = true;
      return;
    }

    let queryParams = new HttpParams()
      .append('status', Array.from(this.csvData.status).join(',').toString())
      .append('startDate', this.csvData.startDate.value.replaceAll('-', '/'))
      .append('endDate', this.csvData.endDate.value.replaceAll('-', '/'));

    console.log(queryParams);

    this.jobService.downloadCsv(queryParams).subscribe({
      next: (response: any) => {
        console.log('re', response);

        let anchor = document.createElement('a');
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(
          new Blob([response.body], { type: response.body.type })
        );
        anchor.click();

        this.modalService.dismissAll();
      },
      error: (err) => {
        console.log(err);

        if (err.status == 404) {
          this.toastService.error('No Record Found');
        } else if (err.status == 400) {
          err.error.text().then((text: any) => {
            this.toastService.error(JSON.parse(text).message);
          });
        }
      },
    });
  }

  jobId: number = 0;

  open(content: any, jobId?: number) {
    this.jobId = jobId ? jobId : 0;
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }
  openXl(content: any, jobId?: number) {
    this.jobId = jobId ? jobId : 0;
    // this.modalService.open(content, { size: 'xl' });
    this.modalService.open(content, { scrollable: true, size: 'xl' });
  }

  changeJobStatus(jobId: number, status: number): void {
    this.showSpinner = true;
    this.jobService.changeJobStatus(jobId, status).subscribe({
      next: (response: any) => {
        console.log('Status Changed', response);
        this.showSpinner = false;

        this.jobDataList.splice(
          this.jobDataList.findIndex((x) => x.jobId == response.jobId),
          1,
          response
        );
      },
      error: (error: any) => {
        console.log(error);
        this.showSpinner = false;
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

          response.forEach((element: any) => {
            this.jobDataList.splice(
              this.jobDataList.findIndex((x) => x.jobId == element.jobId),
              1,
              element
            );
          });
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

          this.statusCount[this.status.get(element.status) as string] =
            element.count;
        });
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

  saveCompleted(event: any) {
    let index = this.jobDataList.findIndex((x) => x.jobId == event.jobId);
    console.log(index);

    if (index < 0) {
      this.resetList();
      return;
    }

    this.jobDataList.splice(index, 1, event);

    this.jobRequestCount();
  }

  jobRequestCounts: any = {};

  jobRequestCount() {
    this.jobRequestService.getRequestStatus().subscribe({
      next: (res: any) => {
        console.log(res);
        this.jobRequestCounts = res;
      },
      error: (err: any) => {
        console.log(err);
      },
    });
  }
}
