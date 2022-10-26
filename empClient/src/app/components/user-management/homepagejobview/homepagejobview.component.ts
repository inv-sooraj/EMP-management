import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-homepagejobview',
  templateUrl: './homepagejobview.component.html',
  styleUrls: ['./homepagejobview.component.css'],
})
export class HomepagejobviewComponent implements OnInit {
  constructor(private route: Router) {}

  ngOnInit(): void {}

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
}
