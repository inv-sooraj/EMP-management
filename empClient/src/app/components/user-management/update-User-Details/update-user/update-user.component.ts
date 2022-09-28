import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user-services/user.service';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {

  user: any

  constructor(private userService: UserService) { }


  updateForm: FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    phoneNo: new FormControl('', Validators.required),
    qualification: new FormControl('', Validators.required)
  })

  ngOnInit(): void {
    this.getDetails()
  }

  getDetails() {
    this.userService.getUserDetails().subscribe({
      next: (response: any) => {
        console.log(response);
        if (response) {
          this.user = response;
          if (response.name) {
            this.updateForm.patchValue({
              name: response.name,
              address: response.address,
              phoneNo: response.phoneNo,
              qualification: response.qualification,
            })
          }
        }
      },
      error: (error: any) => { console.log(error) }
    })
  }

  updateUser() {
    if (this.updateForm.valid) {
      let userData = {
        name: this.updateForm.controls['name'].value,
        address: this.updateForm.controls['address'].value,
        phoneNo: this.updateForm.controls['phoneNo'].value,
        qualification: this.updateForm.controls['qualification'].value,
      }
      this.userService.userEdit(userData).subscribe({
        next: (response: any) => {
          console.log(response);
          document.getElementById('closeUserUpdateModal')?.click();

        },
        error: (error: any) => { console.log(error) }
      })
    }
  }
  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };
}



