import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { JobService } from 'src/app/services/job-services/job.service';

@Component({
  selector: 'app-job-add',
  templateUrl: './job-add.component.html',
  styleUrls: ['./job-add.component.css']
})
export class JobAddComponent implements OnInit {

  status: any;
  qualification: any

  jobAddForm: FormGroup = new FormGroup({
    title: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required),
    noOfOpenings: new FormControl('', Validators.required),
  })

  jobEditForm: FormGroup = new FormGroup({
    title: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required),
    noOfOpenings: new FormControl('', Validators.required),
  })

  constructor(private jobService: JobService) { }

  ngOnInit(): void {
    this.formSelect()
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
            })
        },
        error: (error: any) => {
          console.log(error);
        }
      })
    }
  }

  onSelection(value: any) {
    if (value == 'Tenth') {
      this.qualification = '0';
    } else if (value == 'Twelfth') {
      this.qualification = '1';
    } else if (value == 'UG') {
      this.qualification = '2';
    } else if (value == 'PG') {
      this.qualification = '3';
    }
  }





  jobAdd() {
    if (this.jobAddForm.valid) {
      let jobData = {
        title: this.jobAddForm.controls['title'].value,
        description: this.jobAddForm.controls['description'].value,
        noOfOpenings: this.jobAddForm.controls['noOfOpenings'].value,
        qualification: this.qualification,
      }

      this.jobService.jobAdd(jobData).subscribe({
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

  jobEdit() {
    if (this.jobEditForm.valid) {
      let jobData = {
        title: this.jobEditForm.controls['title'].value,
        description: this.jobEditForm.controls['description'].value,
        noOfOpenings: this.jobEditForm.controls['noOfOpenings'].value,
        qualification: this.qualification,
      }

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
}