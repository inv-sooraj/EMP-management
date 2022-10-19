import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-user-verify',
  templateUrl: './user-verify.component.html',
  styleUrls: ['./user-verify.component.css'],
})
export class UserVerifyComponent implements OnInit {
  constructor(
    private service: AuthService,
    private acivatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  token: string = '';

  ngOnInit(): void {
    this.token = this.acivatedRoute.snapshot.queryParams['token'];

    if (!this.token) {
      this.router.navigateByUrl('login');
    }

    this.verify();
  }

  verify(): void {
    this.service.verifyUser(this.token).subscribe({
      next: (res: any) => {
        console.log(res);

        Swal.fire({
          title: 'Verified?',
          icon: 'success',
          confirmButtonColor: '#3085d6',
          confirmButtonText: 'Ok',
        }).then((result) => {
          this.router.navigate(['login']);
        });
      },
      error: (err: any) => {
        console.log(err);

        if (err.error.status == 400) {
          if (err.error.message == 'Already Verified') {
            Swal.fire({
              title: 'Alreadey Verified!',
              icon: 'warning',
              confirmButtonColor: '#3085d6',
              confirmButtonText: 'Login',
            }).then((result) => {
              this.router.navigate(['login']);
            });
          } else if (err.error.message == 'Invalid Token') {
            Swal.fire({
              title: 'Invalid Token!',
              icon: 'error',
              confirmButtonColor: '#3085d6',
              confirmButtonText: 'Ok',
            }).then((result) => {
              this.router.navigate(['login']);
            });
          } else if (err.error.message == 'Registration Token Expired') {
            Swal.fire({
              title: 'Expired!',
              icon: 'error',
              confirmButtonColor: '#3085d6',
              confirmButtonText: 'Register Again',
            }).then((result) => {
              this.router.navigate(['register']);
            });
          }
        } else if (err.error.status == 404) {
          Swal.fire({
            title: 'Oops Something went Wrong!',
            icon: 'error',
            confirmButtonColor: '#3085d6',
            confirmButtonText: 'Ok',
          }).then((result) => {
            this.router.navigate(['register']);
          });
        }
      },
    });
  }
}
