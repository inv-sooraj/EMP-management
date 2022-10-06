import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  userCount: any = 0
  jobCount: any = 0
  jobRequestCount: any = 0
  admin: any
  blob: any

  src: any

  selectedImage: any


  title = 'appBootstrap';

  closeResult: string = '';

  gaugeValues: any = {
    1: 0,
    2: 0,
    3: 0
  };

  interval: any;
  changePasswordForm:FormGroup=new FormGroup({
    currentPswd:new FormControl('',[Validators.required,Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'),Validators.minLength(8)]),
    newPswd:new FormControl('',[Validators.required,Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'),Validators.minLength(8)]),
    confirmPswd:new FormControl('',Validators.required)
  })

  constructor(private userService: UserService,
    private jobService: JobService,
    private jobRequestService: JobRequestService,
    private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.userCounts();
    this.jobCounts();
    this.jobRequestCounts();
    this.getDetails();
    this.getProfilePic();
  }

  userCounts() {
    this.userService.getUsersCount().subscribe({
      next: (response: any) => {
        console.log(response);
        this.userCount = response;
        this.gaugeValues[1] = this.userCount
      },
      error: (error: any) => { console.log(error) }
    })
  }
  changePassword(){
    if(this.changePasswordForm.valid)
    {
      let data={
        currentPassword:this.changePasswordForm.controls['currentPswd'].value,
        newPassword: this.changePasswordForm.controls['newPswd'].value
      }
      this.userService.changePswd(data).subscribe({
        next: (response: any) => {
          console.log(response);
          if(response){
            alert("Password Changed Successfully");
            document.getElementById('closeChangePswdModal')?.click();
            this.changePasswordForm.reset();
          }
        },
        error: (error: any) => { console.log(error);
          if(error){
            alert("Password Mismatch");
            document.getElementById('closeChangePswdModal')?.click();
          }
        }
      })
    }
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
          this.admin = response;
        }
      },
      error: (error: any) => { console.log(error) }
    })
  }

  uploadPic() {
    const fd = new FormData();
    fd.append('profilePhoto', this.selectedImage)
    this.userService.uploadImageManager(fd).subscribe({
      next: (response: any) => {
        if (response) {
          alert("uploaded");
          this.getProfilePic();
        }
      },
      error: (error: any) => {
        console.log(error);
        alert('failed toupload');
      }
    })
  }

  getProfilePic() {

    this.userService.getProfilePic().subscribe({
      next: (response: any) => {
        if (response) {
        console.log("pic", response);
        
          (document.getElementById('profile') as HTMLImageElement).src = URL.createObjectURL(
            new Blob([response], { type: response.type })
          )
        }
      },
      error: (error: any) => {
        console.log(error);
      }
    })
  }



  onFileChanged(event: any) {
    console.log(event);
    this.selectedImage = <File>event.target.files[0]
    this.uploadPic();
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' })
  }
  ngOnDestroy(): void {
    clearInterval(this.interval);
  }
}
