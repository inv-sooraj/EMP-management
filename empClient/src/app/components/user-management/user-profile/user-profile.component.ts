import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  constructor(
    private userService: UserService,
    private modalService: NgbModal
  ) {}

  userDetails: any = {};

  role = this.userService.role;

  qualifications = this.userService.qualifications;

  changePasswordForm: FormGroup = new FormGroup({
    currentPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
      ),
    ]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
      ),
    ]),
    confirmNewPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
      ),
    ]),
  });

  ngOnInit(): void {
    this.getProfile();
  }

  getProfile() {
    this.userService.getCurrentUser().subscribe({
      next: (response: any) => {
        console.log(response);
        this.userDetails = response;
        this.getProfilePic();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  open(content: any, jobId?: number) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  file: any;

  onChange(event: any) {
    this.file = event.target.files[0];
    console.log('Changed');
    this.uploadProfilePic();
  }

  uploadProfilePic(): void {
    let formData = new FormData();

    formData.append('profilePic', this.file);

    this.userService.uploadProfile(formData).subscribe({
      next: (response: any) => {
        console.log(response);
        this.getProfilePic();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  getProfilePic(): void {
    this.userService.getProfile().subscribe({
      next: (response: any) => {
        console.log(response);

        (document.getElementById('profilePicture') as HTMLImageElement).src =
          URL.createObjectURL(new Blob([response], { type: response.type }));
      },
      error(err) {
        console.log(err);
      },
    });
  }

  changePassword(): void {
    if (!this.changePasswordForm.valid) {
      console.log('Validation Failed');
      this.changePasswordForm.markAllAsTouched();
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
}
