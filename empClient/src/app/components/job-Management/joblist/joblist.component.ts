import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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

  setSearch() {
    console.log(this.search);
    this.jobList();
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }


  checked: Array<number> = [];

  checkedUser(event: any) {
    console.log(event.target.checked);
    console.log(event.target.attributes.value.value);

    if (event.target.checked) {
      this.checked.push(event.target.attributes.value.value);
    } else {
      this.checked.splice(
        this.checked.indexOf(event.target.attributes.value.value),
        1
      );
    }

    console.log(this.checked.indexOf(5));

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

    document.getElementById('selectAll')?.click();
  }

  abcd(event: any) {
    console.log(event);

    this.jobs.result.forEach((element: any) => {
      console.log(element.jobId);

      document.getElementById('checkbox' + element.jobId)?.click();
    });
  }
}


