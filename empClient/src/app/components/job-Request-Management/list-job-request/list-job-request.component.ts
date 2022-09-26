import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobRequestService } from 'src/app/services/jobRequest-services/job-request.service';

@Component({
  selector: 'app-list-job-request',
  templateUrl: './list-job-request.component.html',
  styleUrls: ['./list-job-request.component.css']
})
export class ListJobRequestComponent implements OnInit {
  requests: any
  requestData:any
  count: any
  total: any
 
  page:number = 1
  limit:number=5;
  sortBy:string='job_Id'
  search:string='';

  title = 'appBootstrap';

  closeResult: string = '';
  constructor(private service: JobRequestService, private router: Router, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.reqList()
  }
  addJob() {
    this.service.status = 0;
  }

  edit(reqId: any) {
    this.service.status = 1;
    this.service.reqId = reqId;
  }

  delete(reqId: any) {
    this.service.deletereq(reqId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.reqList();
      },
      error: (error: any) => { console.log(error) }
    })
  }

  reqList() {
    let queryParams = new HttpParams()
    .append('page', this.page)
    .append('limit', this.limit)
    .append('sortBy', this.sortBy)
    .append('search', this.search);
    this.service.getJobRequests(queryParams).subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.requests = response;
          this.requestData=response.result;
          this.count = response.result.length;
          this.total = response.numItems;
        }

      },
      error: (error: any) => { console.log(error) }
    })
  }
  download() {
    console.log("in fun");

    this.service.downloadJobRequests().subscribe({
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
    this.reqList();
  }

  gotoPage(page: number) {
    this.page = page;
    this.reqList();
  }

  nextPage() {
    this.page += 1;
    this.reqList();
  }
  setSort(sortBy: string) {
    this.sortBy = sortBy;
    this.page = 1;
    this.reqList();
  }

  setLimit() {
    console.log(this.limit);
    this.reqList();
  }

  setSearch() {
    console.log(this.search);
    this.reqList();
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

  deleterequests(): void {
    if (this.checked.length <= 0) {
      return;
    }
    this.service.deleteRequests(this.checked).subscribe({
      next: (response: any) => {
        console.log('deleted', this.checked, response);
        this.reqList();
      },
      error(err) {
        console.log(err);
      },
    });

    document.getElementById('selectAll')?.click();
  }

  abcd(event: any) {
    console.log(event);

    this.requests.result.forEach((element: any) => {
      console.log(element.jobId);

      document.getElementById('checkbox' + element.jobId)?.click();
    });
  }

}
