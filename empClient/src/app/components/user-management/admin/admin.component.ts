import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnDestroy, OnInit, Output } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JobService } from 'src/app/services/job-services/job.service';
import { JobRequestService } from 'src/app/services/jobRequest-services/job-request.service';
import { UserService } from 'src/app/services/user-services/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {
  // percentageValue: (value: number) => string;
  userCount: any=0
  jobCount: any=0
  jobRequestCount: any=0
  admin:any

  title = 'appBootstrap';

  closeResult: string = '';

  gaugeValues: any = {
    1: 0,
    2: 0,
    3: 0
  };

  interval: any;

  constructor(private userService: UserService, private jobService: JobService,private jobRequestService: JobRequestService, private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.userCounts();
    this.jobCounts();
    this.jobRequestCounts();
    this.getDetails();
  }

  userCounts() {
    this.userService.getUsersCount().subscribe({
      next: (response: any) => {
        console.log(response);
        this.userCount = response;
        this.gaugeValues[1]=this.userCount
      },
      error: (error: any) => { console.log(error) }
    })
  }

  jobCounts() {
    this.jobService.getJobCount().subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobCount = response;
        this.gaugeValues[2]=this.jobCount
      },
      error: (error: any) => { console.log(error) }
    })
  }
  jobRequestCounts() {
    this.jobRequestService.getJobRequestsCount().subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobRequestCount = response;
        this.gaugeValues[3]=this.jobRequestCount
      },
      error: (error: any) => { console.log(error) }
    })
  }

  getDetails(){
    this.userService.getUserDetails().subscribe({
      next: (response: any) => {
        console.log(response);
        if (response){
          this.admin=response;
        }
      },
      error: (error: any) => { console.log(error) }
    })
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




  ngOnDestroy(): void {
    clearInterval(this.interval);
  }
}
