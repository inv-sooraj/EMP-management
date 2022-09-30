import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserManagementService } from 'src/app/services/user-management.service';

@Component({
  selector: 'app-add-edit-user',
  templateUrl: './add-edit-user.component.html',
  styleUrls: ['./add-edit-user.component.css'],
})
export class AddEditUserComponent implements OnInit {
  
  flag: boolean = true;

  @Output() completed = new EventEmitter();

  constructor(private service: UserManagementService) {}

  ngOnInit(): void {
    // this.getUserDetails();
  }

  addEditUserForm = new FormGroup({
    userName: new FormControl('', [Validators.required]),
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required]),
    password: new FormControl('', Validators.required),
    // address: new FormControl(''),
    // phone: new FormControl(''),
    // qualification: new FormControl('', [Validators.required]),
    role: new FormControl('', [Validators.required]),
  });

  // To add a new user
  addUser() {
    if (this.addEditUserForm.valid) {
      let formData = {
        userName: this.addEditUserForm.controls['userName'].value,
        name: this.addEditUserForm.controls['name'].value,
        email: this.addEditUserForm.controls['email'].value,
        password: this.addEditUserForm.controls['password'].value,
        // phone: this.addEditUserForm.controls['phone'].value,
        // address: this.addEditUserForm.controls['address'].value,
        // qualification: this.addEditUserForm.controls['qualification'].value,
        role: this.addEditUserForm.controls['role'].value,
      };
      this.service.userAdd(formData).subscribe({
        next: (response: any) => {
          console.log(response), this.completed.emit();
        },
        error: (error: any) => {
          console.log(error);
        },
      });
    } else {
      this.addEditUserForm.markAllAsTouched();
    }
  }
}
