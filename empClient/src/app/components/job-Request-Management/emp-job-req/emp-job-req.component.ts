import { Component, OnInit } from '@angular/core';
import { JobRequestService } from 'src/app/services/jobRequest-services/job-request.service';

@Component({
  selector: 'app-emp-job-req',
  templateUrl: './emp-job-req.component.html',
  styleUrls: ['./emp-job-req.component.css']
})
export class EmpJobReqComponent implements OnInit {
  requests:any
  constructor(private service:JobRequestService) { }

  ngOnInit(): void {
    this.reqList();
  }
  reqList() {
    this.service.getEmpRequests().subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.requests = response;
        }

      },
      error: (error: any) => { console.log(error) }
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

}
