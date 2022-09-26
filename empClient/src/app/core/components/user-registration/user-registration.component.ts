import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  constructor(private service: AuthService, private router: Router) { }

  registerForm: FormGroup = new FormGroup({
    userName: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required,Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'), Validators.email]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'), Validators.minLength(8)])

  })
  ngOnInit(): void {
    // TODO document why this method 'ngOnInit' is empty
  
  }





  registerUser() {
    if (this.registerForm.valid) {
      console.log("in func")
      let userData = {
        userName: this.registerForm.controls['userName'].value,
        email: this.registerForm.controls['email'].value,
        password: this.registerForm.controls['password'].value,
        role:2
      }
      this.service.registerUser(userData).subscribe({
        next: (response: any) => {
          console.log(response);

          alert("Registration Successfull")
        },
        error: (error: any) => {
          console.log(error);
          if (error.status == 404) {
            alert("User Not Found");
          } else if (error.status == 400) { alert("Password Mismatch"); }
        }

      });
    }
  }

  login() {
    this.router.navigate(['login'])
  }

}
