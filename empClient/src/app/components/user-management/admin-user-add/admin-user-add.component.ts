import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-admin-user-add',
  templateUrl: './admin-user-add.component.html',
  styleUrls: ['./admin-user-add.component.css'],
})
export class AdminUserAddComponent implements OnInit {
  @Output() public completedEvent = new EventEmitter();

  constructor(private userService: UserService) {}

  userAddForm: FormGroup = new FormGroup({
    role: new FormControl('0', Validators.required),
    name: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    userName: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      Validators.maxLength(50),
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}'),
    ]),
  });

  ngOnInit(): void {}

  register(): void {
    if (!this.userAddForm.valid) {
      console.log('Validation Failed');
      this.userAddForm.markAllAsTouched();
      return;
    }

    let param = {
      role: this.userAddForm.controls['role'].value,
      name: this.userAddForm.controls['name'].value,
      userName: this.userAddForm.controls['userName'].value,
      email: this.userAddForm.get('email')?.value,
    };

    console.log(param);
    

    this.userService.registerUser(param).subscribe({
      next: (response: any) => {
        console.log('User Registered', response);
        this.completedEvent.emit();
        alert('User Registered');
      },
      error: (error: any) => {
        console.log('error', error.error);
        if (error.error.status == 400) {
          if (error.error.message == 'Username Already Exists') {
            console.log('Username Already Exists');
            alert('Username Already Exists');
          } else if (error.error.message == 'Email Already Exists') {
            console.log('Email Already Exists');
            alert('Email Already Exists');
          }
        }
        alert("Error")
      },
    });
  }
}
