import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserManagementService } from 'src/app/services/user-management.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css'],
})
export class EditUserComponent implements OnInit {
  userId: any;
  constructor(private service: UserManagementService, private route: Router) {}

  ngOnInit(): void {
    this.getUserDetails();
  }

  editUserForm = new FormGroup({
    userName: new FormControl('', [Validators.required]),
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required]),

    address: new FormControl(''),
    phone: new FormControl(''),
    qualification: new FormControl('', [Validators.required]),
  });

  getUserDetails() {
    this.service.userDetails().subscribe({
      next: (response: any) => {
        console.log(response),
          this.editUserForm.patchValue({
            userName: response.userName,
            name: response.name,
            email: response.email,
            address: response.address,
            phone: response.phone,
            qualification: response.qualification,
          });
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  // To edit user details
  editUser() {
    if (this.editUserForm.valid) {
      let formData = {
        userName: this.editUserForm.controls['userName'].value,
        name: this.editUserForm.controls['name'].value,
        email: this.editUserForm.controls['email'].value,
        phone: this.editUserForm.controls['phone'].value,
        address: this.editUserForm.controls['address'].value,
        qualification: this.editUserForm.controls['qualification'].value
      };
      this.service.updateUser(formData).subscribe({
        next: (response: any) => {
          console.log(response);

          let role = Number(localStorage.getItem('key'));
          if (role == 0) {
            this.route.navigate(['adminDashboard']);
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
}
