import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobService } from 'src/app/services/job-services/job.service';
import { JobRequestService } from 'src/app/services/jobRequest-services/job-request.service';
import { UserService } from 'src/app/services/user-services/user.service';

@Component({
  selector: 'app-employer',
  templateUrl: './employer.component.html',
  styleUrls: ['./employer.component.css']
})
export class EmployerComponent implements OnInit {
  userCount: any = 0
  jobCount: any = 0
  jobRequestCount: any = 0
  employee: any

  title = 'appBootstrap';

  closeResult: string = '';

  gaugeValues: any = {
    1: 0,
    2: 0,
    3: 0
  };

  interval: any;
  constructor(private userService: UserService, private jobService: JobService, private jobRequestService: JobRequestService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.jobCounts();
    this.jobRequestCounts();
    this.getDetails();
  }

  jobCounts() {
    this.jobService.getJobCount().subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobCount = response;
        this.gaugeValues[2] = this.jobCount
      },
      error: (error: any) => { console.log(error) }
    })
  }
  jobRequestCounts() {
    this.jobRequestService.getJobRequestsCount().subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobRequestCount = response;
        this.gaugeValues[3] = this.jobRequestCount
      },
      error: (error: any) => { console.log(error) }
    })
  }

  getDetails() {
    this.userService.getUserDetails().subscribe({
      next: (response: any) => {
        console.log(response);
        if (response) {
          this.employee = response;
        }
      },
      error: (error: any) => { console.log(error) }
    })
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' })
  }
}
