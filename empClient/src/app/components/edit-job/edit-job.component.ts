import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { JobManagementService } from 'src/app/services/job-management.service';
import { UserManagementService } from 'src/app/services/user-management.service';

@Component({
  selector: 'app-edit-job',
  templateUrl: './edit-job.component.html',
  styleUrls: ['./edit-job.component.css'],
})
export class EditJobComponent implements OnInit {
  @Input() jobId: number = 0;

  @Output() completed = new EventEmitter();

  constructor(
    private service: JobManagementService,
    private route: Router,
    private userService: UserManagementService
  ) {}

  ngOnInit(): void {
    this.getItemId();
  }

  editJobForm = new FormGroup({
    jobTitle: new FormControl('', Validators.required),
    jobDescription: new FormControl('', Validators.required),
    openings: new FormControl('', Validators.required),
    qualification: new FormControl('', Validators.required),
  });

  getItemId() {
    if (!this.jobId) {
      return;
    }

    this.service.getJobById(this.jobId).subscribe({
      next: (response: any) => {
        console.log(response),
          this.editJobForm.patchValue({
            jobTitle: response.jobTitle,
            jobDescription: response.jobDescription,
            openings: response.openings,
            qualification: response.qualification,
          });
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  editJob() {
    if (this.editJobForm.valid) {
      this.service
        .updateJobDetails(this.editJobForm.value, this.jobId)
        .subscribe({
          next: (response: any) => {
            console.log(response);
            this.completed.emit();
            let role = Number(localStorage.getItem('key'));
            if (role == 0) {
              this.route.navigate(['jobView']);
            } else {
              this.route.navigate(['userDashboard']);
            }
          },
          error: (error: any) => {
            console.log(error);
          },
        });
    }
  }

  jobAdd() {
    if (this.editJobForm.valid) {
      this.service.addJob(this.editJobForm.value).subscribe({
        next: (response: any) => {
          console.log(response);
          let role = Number(localStorage.getItem('key'));
          if (role == 0) {
            this.route.navigate(['jobView']);
          } else {
            this.route.navigate(['userDashboard']);
          }
        },
        error: (error: any) => {
          console.log(error);
        },
      });
    }
  }

  submit() {
    if (this.jobId) {
      this.editJob();
    } else {
      this.jobAdd();
    }
  }
}
