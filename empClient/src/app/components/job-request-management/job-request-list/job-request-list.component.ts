import { HttpParams } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/service/auth.service';
import { JobRequestService } from 'src/app/service/job-request.service';

@Component({
  selector: 'app-job-request-list',
  templateUrl: './job-request-list.component.html',
  styleUrls: ['./job-request-list.component.css'],
})
export class JobRequestListComponent implements OnInit {
  constructor(
    private jobRequestService: JobRequestService,
    private modalService: NgbModal,
    private service: AuthService,
    private toastService: ToastrService
  ) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  userId: number = 0;

  page: number = 1;

  sortBy: string = 'jobRequestId';

  @Input() limit: number = 0;

  search: string = '';

  sortDesc: boolean = false;

  showSpinner: boolean = false;

  status = this.jobRequestService.status;

  role: number;

  @Input() jobId: number = 0;
  @Input() scrollingDisabled: boolean = false;

  ngOnInit(): void {
    this.listJobRequests();
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
    return arr;
  }

  prevPage() {
    this.page -= 1;
    this.listJobRequests();
  }

  gotoPage(page: number) {
    if (this.page == page) return;
    this.page = page;
    this.listJobRequests();
  }

  nextPage() {
    this.page += 1;
    this.listJobRequests();
  }

  setSort(sortBy: string) {
    this.jobRequestDataList = [];

    if (this.sortBy == sortBy) {
      this.sortDesc = this.sortDesc ? false : true;
    } else {
      this.sortDesc = false;
    }

    console.log('sort by : ', sortBy, ', desc : ', this.sortDesc);

    this.sortBy = sortBy;
    this.page = 1;
    this.listJobRequests();
  }

  resetList() {
    this.jobRequestDataList = [];
    this.page = 1;
    this.listJobRequests();
  }

  pagerInfo: any;

  jobRequestDataList: Array<any> = [];

  listJobRequests(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append(
        'limit',
        this.limit ? this.limit : (window.innerHeight / 100).toFixed(0)
      )
      .append('sortBy', this.sortBy)
      .append('desc', this.sortDesc)
      .append('jobId', this.jobId)
      .append('search', this.search);

    this.jobRequestService.getJobRequests(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);
        this.pagerInfo = response.pagerInfo;

        this.pagerInfo['numPages'] = response.numPages;
        this.pagerInfo['currentPage'] = response.currentPage;

        if (this.limit == 0) {
          this.jobRequestDataList.push(...response.result);
        } else {
          this.jobRequestDataList = response.result;
        }
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  updateStatus(jobRequestId: number, status: number): void {
    console.log(this.remark);
    this.showSpinner = true;

    let body = {
      status: status,
      remark: this.remark.value,
    };

    this.jobRequestService.updateStatus(jobRequestId, body).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobRequestDataList.splice(
          this.jobRequestDataList.findIndex(
            (x) => x.jobRequestId == response.jobRequestId
          ),
          1,
          response
        );
        this.showSpinner = false;
      },
      error: (err: any) => {
        this.showSpinner = false;
        console.error(err);
        if (err.error.status == 400) {
          if (err.error.message == 'Invalid Operation') {
            this.toastService.error('Job is Completed!');
          } else this.toastService.error(err.error.message);
        }
      },
    });
  }

  jobRequestId: number = 0;

  action: number = 1;

  remark: FormControl = new FormControl('', [Validators.maxLength(50)]);

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
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
    this.listJobRequests();
  }
}
