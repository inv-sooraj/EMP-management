import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  role: string = '2';

  status: boolean = false


  Role: any

  flag: any = 0

  showSpinner: boolean = false


  constructor(private service: AuthService,
    private router: Router) { }

  registerForm: FormGroup = new FormGroup({
    userName: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'), Validators.minLength(8)]),
    confirmPassword: new FormControl('', [Validators.required])
  })
  ngOnInit(): void {
    this.status = false;
    console.log("1:::", this.status);

    this.Role = localStorage.getItem('role');
    if (this.Role == 0) {
      this.status = true;
      console.log("2 in condition:::", this.Role);

    } else {
      this.status = false;
      console.log("3 else part:::", this.status);

    }
  }



  registerUser() {
    if (this.registerForm.valid) {
      this.showSpinner = true
      console.log(this.role);
      let userData = {
        userName: this.registerForm.controls['userName'].value,
        email: this.registerForm.controls['email'].value,
        password: this.registerForm.controls['password'].value,
        role: this.role
      };
      if (this.Role) {
        console.log("in role fun");

        this.service.regUser(userData).subscribe({
          next: (response: any) => {
            console.log(response);
            this.showSpinner = false;
            alert("Registration Successfull");
            this.router.navigate(['users']);
          },
          error: (error: any) => {
            console.log(error);
            this.showSpinner = false;
          }

        });
      } else {
        this.service.registerUser(userData).subscribe({
          next: (response: any) => {
            console.log(response);
            this.showSpinner = false;
            alert("Registration Successfull")
            this.router.navigate(['login']);
          },
          error: (error: any) => {
            console.log(error);
            this.showSpinner = false;
          }
        });
      }
    } else { this.registerForm.markAllAsTouched() }

  }


  login() {
    this.router.navigate(['login'])
  }
}
