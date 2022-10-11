import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobService } from 'src/app/services/job-services/job.service';
import { JobRequestService } from 'src/app/services/jobRequest-services/job-request.service';
import { EmpJobReqComponent } from '../../job-Request-Management/emp-job-req/emp-job-req.component';

@Component({
  selector: 'app-joblist-employee',
  templateUrl: './joblist-employee.component.html',
  styleUrls: ['./joblist-employee.component.css']
})
export class JoblistEmployeeComponent implements OnInit {
  jobs: any
  jobsData: any
  count: any
  total: any
  requests:Array<number>=[]


  page: number = 1
  limit: number = 5;
  sortBy: string = 'job_Id'
  search: string = '';
button:boolean=false;
sortDesc: boolean = false;
  filter: number = 5;

  title = 'appBootstrap';

  closeResult: string = '';

  remarkForm: FormGroup = new FormGroup({
    remark: new FormControl('', Validators.required),
  })

  jobId: any

  constructor(private jobService: JobService,
    private modalService: NgbModal,
    private reqService: JobRequestService) { }

  ngOnInit(): void {
    this.jobList();
   
  }

  jobList() {
    console.log("hhhh");
    
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sortBy', this.sortBy)
      .append('desc', this.sortDesc)
      .append('filter', this.filter)
      .append('search', this.search);

    this.jobService.getJobs(queryParams).subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.jobs = response;
          this.jobsData = response.result;
          this.count = response.result.length;
          this.total = response.numItems;
          this.reqList();
        }

      },
      error: (error: any) => { console.log(error) }
    })
  }
  reqList() {
    this.reqService.getEmpReqIds().subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.requests = response;

        }

      },
      error: (error: any) => { console.log(error) }
    })
  }
  

  numSeq(n: number): Array<number> {
    return Array(n);
  }

  prevPage() {
    this.page -= 1;
    this.jobList();
  }

  gotoPage(page: number) {
    this.page = page;
    this.jobList();
  }

  nextPage() {
    this.page += 1;
    this.jobList();
  }

  setLimit() {
    console.log(this.limit);
    this.jobList();
  }

  setSearch() {
    console.log(this.search);
    this.jobList();
  }
  setSort(sortBy: string) {
    if (this.jobs.result.length <= 1) {
      return;
    }

    if (this.sortBy == sortBy) {
      this.sortDesc = this.sortDesc ? false : true;
    } else {
      this.sortDesc = false;
    }
    this.sortBy = sortBy;
    this.page = 1;
    this.jobList();
  }

  setFilter() {
    console.log(this.limit);
    this.jobList();
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' })
  }

  checked: Array<number> = [];

  checkedUser(event: any) {
    if (event.target.checked) {
      this.checked.push(Number(event.target.attributes.value.value));

      if (this.jobs.result.length == this.checked.length) {
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
    this.jobs.result.forEach((element: any) => {
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

  apply(id: any) {
    this.jobId = id;

  }
  jobApply() {
    if (this.remarkForm.valid) {
      let data = {
        remark: this.remarkForm.controls['remark'].value
      }
      this.reqService.jobApply(this.jobId, data).subscribe({
        next: (response: any) => {
          console.log(response);
          document.getElementById('closeJobApplyModal')?.click();
        },
        error: (error: any) => { console.log(error); }
      })
    }
  }

}
