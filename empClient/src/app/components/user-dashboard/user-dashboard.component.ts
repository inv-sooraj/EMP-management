import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserManagementService } from 'src/app/services/user-management.service';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css'],
})
export class UserDashboardComponent implements OnInit {
  details: any;
  flag: number = 0;
  userId: number = 0;
  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };
  Roles: { [key: number]: string } = {
    0: 'Admin ',
    1: 'Employer',
    2: 'Employee ',
  };

  constructor(
    private service: UserManagementService,
    private route: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.currentUserView();
    this.isUser();
    this.getprofilePicture();
  }

  currentUserView() {
    this.service.getCurrentUserDetails().subscribe((data) => {
      this.details = data;
      console.log(this.details);
    });
  }

  isUser() {
    let role = Number(localStorage.getItem('key'));

    if (role == 2) {
      this.flag = 1;
    } else {
      this.flag = 0;
    }
  }

  logout() {
    localStorage.clear();
    this.route.navigate(['login']);
  }
  edit(info: any) {
    this.service.userId = info;
    // this.route.navigate(['editUser']);
  }

  changePasswordForm: FormGroup = new FormGroup({
    password: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'
      ),
      Validators.minLength(8),
    ]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'
      ),
      Validators.minLength(8),
    ]),
    confirmPswd: new FormControl('', Validators.required),
  });

  img = new FormGroup({
    image: new FormControl(''),
  });

  changePassword() {
    if (this.changePasswordForm.valid) {
      let formData = {
        password: this.changePasswordForm.controls['password'].value,
        newPassword: this.changePasswordForm.controls['newPassword'].value,
      };
      this.service.changePassword(formData).subscribe({
        next: (response: any) => {
          console.log(response);
        },
        error: (error: any) => {
          console.log(error);
          alert('Incorrect password');
        },
      });
    }
  }
  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }
  image: any;
  getprofilePicture() {
    this.service.getProfileImage().subscribe({
      next: (response: any) => {
        console.log('Imgage', response);

        document
          .getElementById('asd')
          ?.setAttribute(
            'src',
            URL.createObjectURL(new Blob([response], { type: response.type }))
          );
      },
      error: (error: any) => {
        console.log(error);
      },
    });
    // console.log(this.imgurl);
  }

  file: any;
  onChange(event: any) {
    this.file = event.target.files[0];

    console.log(this.file);

    this.update();
  }
  update() {
    let body = new FormData();

    body.append('image', this.file);

    this.service.updateUserDetails(body).subscribe({
      next: (res) => {
        console.log(res);
        //  this.route.navigate(['userDashboard']).then().
        this.getprofilePicture();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
