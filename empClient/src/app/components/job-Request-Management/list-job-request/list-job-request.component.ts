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
  requestData: any
  count: any
  total: any

  page: number = 1
  limit: number = 5;
  sortBy: string = 'req_id'
  search: string = '';
  filter: number = 5;
  sortDesc: boolean = false;

  role: any
  showSpinner: boolean = false

  title = 'appBootstrap';

  closeResult: string = '';
  constructor(private service: JobRequestService, private router: Router, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.reqList()
    this.role = localStorage.getItem('role');
  }
  addJob() {
    this.service.status = 0;
  }

  edit(reqId: any, id: any) {
    this.showSpinner = true
    this.service.updateRequest(reqId, id).subscribe({
      next: (response: any) => {
        console.log(response);
        this.showSpinner = false
        this.reqList();
      },
      error: (error: any) => {
        console.log(error);
        this.showSpinner = false
      }
    })
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
      .append('desc', this.sortDesc)
      .append('filter', this.filter)
      .append('search', this.search);
    this.service.getJobRequests(queryParams).subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.requests = response;
          this.requestData = response.result;
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
    if (this.requests.result.length <= 1) {
      return;
    }

    if (this.sortBy == sortBy) {
      this.sortDesc = this.sortDesc ? false : true;
    } else {
      this.sortDesc = false;
    }
    this.sortBy = sortBy;
    this.page = 1;
    this.reqList();
  }
  setFilter() {
    console.log(this.limit);
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

  
  checkedReq(event: any) {
    if (event.target.checked) {
      this.checked.push(Number(event.target.attributes.value.value));

      if (this.requests.result.length == this.checked.length) {
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
    this.requests.result.forEach((element: any) => {
      (
        document.getElementById('checkbox' + element.reqId) as HTMLInputElement
      ).checked = event.target.checked;

      if (event.target.checked) {
        if (!this.checked.includes(element.reqId)) {
          this.checked.push(element.reqId);
        }
      } else {
        this.checked.splice(this.checked.indexOf(element.reqId), 1);
      }
    });
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
}
