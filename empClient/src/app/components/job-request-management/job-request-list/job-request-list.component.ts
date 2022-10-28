import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
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
    private service:AuthService,
    private toastService: ToastrService
  ) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  jobList: any;

  page: number = 1;

  sortBy: string = 'jobRequestId';

  limit: number = 5;

  search: string = '';

  sortDesc: boolean = false;

  showSpinner: boolean = false


  // tableHeight: number = 73 * (this.limit + 1);

  status = this.jobRequestService.status;

  role: number;

  ngOnInit(): void {
    this.listJobRequests();
  }

  numSeq(n: number): Array<number> {
    return Array(n);
  }

  prevPage() {
    this.page -= 1;
    this.listJobRequests();
  }

  gotoPage(page: number) {
    this.page = page;
    this.listJobRequests();
  }

  nextPage() {
    this.page += 1;
    this.listJobRequests();
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
    this.listJobRequests();
  }

  setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.listJobRequests();
  }

  setSearch() {
    console.log(this.search);
    this.listJobRequests();
  }

  listJobRequests(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sortBy', this.sortBy)
      .append('desc', this.sortDesc)
      .append('search', this.search);

    this.jobRequestService.getJobRequests(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobList = response;
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  updateStatus(jobRequestId: number, status: number): void {
    console.log(this.remark);
    this.showSpinner = true


    let body = {
      status: status,
      remark: this.remark.value,
    };

    this.jobRequestService.updateStatus(jobRequestId, body).subscribe({
      next: (response: any) => {
        console.log(response);
        this.listJobRequests();
        this.showSpinner = false

      },
      error: (err: any) => {
        this.showSpinner = false
        console.error(err);
        if (err.error.status == 400) {
          if (err.error.message == 'Invalid Operation') {
            this.toastService.error('Job is Completed!')
          } else alert(err.error.message);
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
}
