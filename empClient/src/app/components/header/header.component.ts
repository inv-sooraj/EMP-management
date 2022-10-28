import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { OauthgoogleService } from 'src/app/core/service/oauthgoogle.service';
import { UserService } from 'src/app/service/user.service';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  showSpinner: boolean = false;

  constructor(
    private router: Router,
    private modalService: NgbModal,
    private userService: UserService,
    private toastService: ToastrService,
    private googleService: OauthgoogleService
  ) {}
  ngOnInit(): void {
    // No API call
  }

  logout() {
    Swal.fire({
      title: 'Are you sure you want to logout?',
      icon: 'warning',
      showCancelButton: true,
      width: '400px',
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes',
    }).then((result) => {
      if (result.isConfirmed) {
        localStorage.clear();
        this.googleService.googleLogout();
        sessionStorage.clear();
        this.router.navigate(['login']);
      }
    });
  }
  getName(): string {
    return localStorage.getItem('name') as string;
  }

  getRole(): number {
    return parseInt(localStorage.getItem('role') as string);
  }

  getStat(): string {
    if (localStorage.getItem('accessToken')) return 'Logout';
    else return 'Login';
  }

  open(content: any, jobId?: number) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }
  changePasswordForm: FormGroup = new FormGroup({
    currentPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=)(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}'
      ),
    ]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=)(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}'
      ),
    ]),
    confirmNewPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=)(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}'
      ),
    ]),
  });

  changePassword(): void {
    if (!this.changePasswordForm.valid) {
      console.log('Validation Failed');
      this.changePasswordForm.markAllAsTouched();
      return;
    }

    if (
      this.changePasswordForm.controls['newPassword'].value !=
      this.changePasswordForm.controls['confirmNewPassword'].value
    ) {
      this.toastService.error(' Password Missmatch!');
      return;
    }

    this.showSpinner = true;
    let param = {
      currentPassword:
        this.changePasswordForm.controls['currentPassword'].value,
      newPassword: this.changePasswordForm.controls['newPassword'].value,
    };
    this.modalService.dismissAll();
    this.userService.changePassword(param).subscribe({
      next: (response: any) => {
        this.showSpinner = false;
        console.log('Password Changed', response);
        this.toastService.success('Password Changed Successfully!');
        this.changePasswordForm.reset();
      },
      error: (error: any) => {
        this.showSpinner = false;
        console.log('error', error.error);
      },
    });
  }

  deleteUserAccount() {
    Swal.fire({
      title: 'Are you sure you want to Deactivate Your Account?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes',
    }).then((result) => {
      if (result.isConfirmed) {
        this.showSpinner = true;
        this.userService.deactivateUser().subscribe({
          next: (response: any) => {
            console.log(response);
            this.showSpinner = false;
            this.toastService.warning('Account Deactivated!');
            localStorage.clear();
            this.router.navigate(['login']);
          },
          error: (error: any) => {
            console.log(error);
            this.showSpinner = false;
          },
        });
      }
    });
  }
}
