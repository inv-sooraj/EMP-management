import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { JobService } from 'src/app/services/job-services/job.service';
import { JoblistComponent } from '../joblist-admin/joblist.component';

@Component({
  selector: 'app-job-add',
  templateUrl: './job-add.component.html',
  styleUrls: ['./job-add.component.css']
})
export class JobAddComponent implements OnInit {

  status: any;
  qualification: any
  role:any

  jobAddForm: FormGroup = new FormGroup({
    title: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required),
    noOfOpenings: new FormControl('', Validators.required),
    qualification: new FormControl('', Validators.required),
  })

  jobEditForm: FormGroup = new FormGroup({
    title: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required),
    qualification: new FormControl('', Validators.required),
    noOfOpenings: new FormControl('', Validators.required),
  })

  constructor(private jobService: JobService,private job:JoblistComponent) { }

  ngOnInit(): void {
    this.formSelect()
    this.role=localStorage.getItem('role');
  }

  formSelect() {
    this.status = this.jobService.status;
    if (this.status == 1) {
      this.jobService.getJob().subscribe({
        next: (response: any) => {
          this.jobEditForm.patchValue({
            title: response.title,
            description: response.description,
            noOfOpenings: response.noOfOpenings,
            qualification: response.qualification,
          })
        },
        error: (error: any) => {
          console.log(error);
        }
      })
    }
  }
  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };







  jobAdd() {
    if (this.jobAddForm.valid) {
      let jobData = {
        title: this.jobAddForm.controls['title'].value,
        description: this.jobAddForm.controls['description'].value,
        noOfOpenings: this.jobAddForm.controls['noOfOpenings'].value,
        qualification: this.jobAddForm.controls['qualification'].value,
      }
      
      this.jobService.jobAdd(jobData).subscribe({
        next: (response: any) => {
          console.log(response);
          if(this.role==0){
            this.changeStatus(response.jobId)
            // .subscribe({
            //   next:(response:any)=>{
            //     document.getElementById('closeJobEditModal')?.click();
            //   }
            // });
          }
          document.getElementById('closeJobEditModal')?.click();
        },
        error: (error: any) => {
          console.log(error);
        }
      })

    }
  }

  jobEdit() {
    if (this.jobEditForm.valid) {
      let jobData = {
        title: this.jobEditForm.controls['title'].value,
        description: this.jobEditForm.controls['description'].value,
        noOfOpenings: this.jobEditForm.controls['noOfOpenings'].value,
        qualification: this.jobEditForm.controls['qualification'].value,
      }
      console.log(jobData);

      this.jobService.jobEdit(jobData).subscribe({
        next: (response: any) => {
          console.log(response);
          document.getElementById('closeJobEditModal')?.click();
        },
        error: (error: any) => {
          console.log(error);
        }
      })

    }
  }
  changeStatus(jobId:any){
    this.jobService.approveJob(jobId,1).subscribe({
      next: (response: any) => {
        console.log(response);
        this.job.jobList();
      },
      error:(error:any)=>console.log(error)
      
    })
  }
}