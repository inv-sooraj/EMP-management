import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobService } from 'src/app/service/job.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-homepagejobview',
  templateUrl: './homepagejobview.component.html',
  styleUrls: ['./homepagejobview.component.css'],
})
export class HomepagejobviewComponent implements OnInit {
  constructor(private route: Router, private jobService: JobService) {}
  details: any;
  ngOnInit(): void {
    this.getLastFourJobs();
  }

  alert() {
    if (localStorage.getItem('accessToken')) {
      this.route.navigate(['/job-apply']);
    } else {
      Swal.fire({
        title: 'Please login first..!',
        icon: 'warning',
        showCancelButton: true,
        width: '400px',
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Ok',
      }).then((result) => {
        if (result.isConfirmed) {
          localStorage.clear();
          this.route.navigate(['login']);
        }
      });
    }
  }

  getLastFourJobs() {
    this.jobService.getLastJobs().subscribe({
      next: (response: any) => {
        console.log(response);
        this.details = response;
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }
}
