import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobService } from 'src/app/services/job-services/job.service';

@Component({
  selector: 'app-joblist',
  templateUrl: './joblist.component.html',
  styleUrls: ['./joblist.component.css']
})
export class JoblistComponent implements OnInit {
  jobs: any
  jobsData:any
  count: any
  total: any
  
  page:number = 1
  limit:number=5;
  sortBy:string='job_Id'
  search:string='';
  filter:number=5;


  title = 'appBootstrap';

  closeResult: string = '';


  constructor(private jobService: JobService, private router: Router, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.jobList()
  }

  addJob() {
    this.jobService.status = 0;
  }
  edit(jobId: any) {
    this.jobService.status = 1;
    this.jobService.jobId = jobId;
  }

  delete(jobId: any) {
    this.jobService.deleteJob(jobId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobList();
      },
      error: (error: any) => { console.log(error) }
    })
  }

  jobList() {

    let queryParams = new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sortBy', this.sortBy)
      .append('filter',this.filter)
      .append('search', this.search);

    this.jobService.getJobs(queryParams).subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.jobs = response;
          this.jobsData=response.result;
          this.count = response.result.length;
          this.total = response.numItems;
        }

      },
      error: (error: any) => { console.log(error) }
    })
  }
  download() {
    console.log("in fun");

    this.jobService.downloadJobs().subscribe({
      next: (response: any) => {
        const anchor = document.createElement("a");
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(new Blob([response.body], { type: response.body.type }));
        anchor.click();
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
  setSort(sortBy: string) {
    this.sortBy = sortBy;
    this.page = 1;
    this.jobList();
  }

  setLimit() {
    console.log(this.limit);
    this.jobList();
  }

  setFilter() {
    console.log(this.limit);
    this.jobList();
  }

  setSearch() {
    console.log(this.search);
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

  deleteJobs(): void {
    if (this.checked.length <= 0) {
      return;
    }
    this.jobService.deleteJobs(this.checked).subscribe({
      next: (response: any) => {
        console.log('deleted', this.checked, response);
        this.jobList();
      },
      error(err) {
        console.log(err);
      },
    });

    (document.getElementById('selectAll') as HTMLInputElement).checked = false;
  }

  changeStatus(jobId:any,status:any){
    this.jobService.approveJob(jobId,status).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobList();
      },
      error: (error: any) => { console.log(error) }
    })
  }
}


