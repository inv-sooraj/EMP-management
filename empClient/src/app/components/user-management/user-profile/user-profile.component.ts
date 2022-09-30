import { Component, OnInit } from '@angular/core';
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

  role: { [key: number]: string } = {
    0: 'EMPLOYEE ',
    1: 'EMPLOYER',
    2: 'ADMIN ',
  };

  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };

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



}
