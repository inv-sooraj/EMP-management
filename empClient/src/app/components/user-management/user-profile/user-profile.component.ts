import { HttpParams } from '@angular/common/http';
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
  ) { }

  userDetails: any = {};

  role = this.userService.roles;

  qualifications = this.userService.qualifications;

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

  ngOnInit(): void {
    this.getProfile();
  }

  getProfile() {
    this.userService.getCurrentUser().subscribe({
      next: (response: any) => {
        console.log(response);
        this.userDetails = response;

        if (this.userDetails.hasProfilePic) {
          this.getProfilePic();
        }else{
          (document.getElementById('profilePicture') as HTMLImageElement).src = '../../../../assets/User-Profile.png'
        }
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
        console.log(response,"dd");
        this.getProfile();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  getProfilePic(): void {

    if (!this.userDetails.hasProfilePic) {
      (document.getElementById('profilePicture') as HTMLImageElement).src = '../../../../assets/User-Profile.png'
      return
    }

    let queryParams = new HttpParams()
      .append('userId', 0)
    this.userService.getProfile(queryParams).subscribe({
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

  // waiting code..................
  deletePicture(userId: number) {
    console.log("hhh");

    this.userService.deleteProfilePic(userId).subscribe({
      next: (reponse: any) => {
        console.log(reponse);
        this.getProfile()
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}
