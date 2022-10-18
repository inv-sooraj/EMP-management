import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-form',
  templateUrl: './job-form.component.html',
  styleUrls: ['./job-form.component.css'],
})
export class JobFormComponent implements OnInit {
  @Input() jobId: number = 0;

  @Output() public completedEvent = new EventEmitter();

  button: string = '';

  title: string = '';

  qualifications = this.jobService.qualifications

  constructor(private jobService: JobService, private router: Router) {}

  ngOnInit(): void {
    this.patchValue(this.jobId);

    if (this.jobId) {
      this.button = 'Edit';
      this.title = 'Edit Job';
    } else {
      this.button = 'Add';
      this.title = 'Add Job';
    }
  }

  jobForm: FormGroup = new FormGroup({
    title: new FormControl('', [Validators.required,Validators.maxLength(50)]),
    description: new FormControl('', [Validators.required,Validators.maxLength(255)]),
    qualification: new FormControl('', [Validators.required]),
    openings: new FormControl(0, [Validators.required,Validators.max(2147483647),Validators.min(0)]),
  });

  formAction() {

    console.log("Errors : ",this.jobForm);
    
    if (!this.jobForm.valid) {
      this.jobForm.markAllAsTouched();
      return;
    }

    let body = {
      title: this.jobForm.controls['title'].value,
      description: this.jobForm.controls['description'].value,
      qualification: this.jobForm.controls['qualification'].value,
      openings: this.jobForm.controls['openings'].value,
    };

    if (this.jobId) {
      this.editJob(this.jobId, body);
    } else {
      this.addJob(body);
    }
  }

  addJob(body: any): void {
    this.jobService.addJob(body).subscribe({
      next: (response: any) => {
        console.log('Added ', response);
        this.completedEvent.emit();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  editJob(jobId: number, body: any): void {
    this.jobService.editJob(jobId, body).subscribe({
      next: (response: any) => {
        console.log('Edited ', response);
        this.completedEvent.emit();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  patchValue(jobId: number): void {
    if (!jobId) {
      return;
    }

    console.log('patch');

    this.jobService.getJob(jobId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.jobForm.patchValue({
          title: response.title,
          description: response.description,
          qualification: response.qualification,
          openings: response.openings,
        });
      },
      error(err) {
        console.log(err);
      },
    });
  }
}
