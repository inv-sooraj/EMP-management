import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from 'src/app/service/user.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  constructor(
    private router: Router,
    private modalService: NgbModal,
    private userService: UserService
  ) {}
  ngOnInit(): void {
    // No API call
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['login']);
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
      alert('Password Should Match');
      return;
    }

    let param = {
      currentPassword:
        this.changePasswordForm.controls['currentPassword'].value,
      newPassword: this.changePasswordForm.controls['newPassword'].value,
    };

    this.userService.changePassword(param).subscribe({
      next: (response: any) => {
        console.log('Password Changed', response);
        alert('Password Changed');
        this.modalService.dismissAll();
      },
      error: (error: any) => {
        console.log('error', error.error);
      },
    });
  }

  deleteUserAccount() {
    this.userService.deactivateUser().subscribe({
      next: (response: any) => {
        console.log(response);
        alert('Your account has been deactivated');
        localStorage.clear();
        this.router.navigate(['login']);
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }
}
