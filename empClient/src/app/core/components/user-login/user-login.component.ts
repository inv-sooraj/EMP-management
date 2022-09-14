import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  constructor(private service:AuthService,private router:Router) { }
  loginForm: FormGroup = new FormGroup({
    userName: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])

  })

  ngOnInit(): void {
  }

  login() {
  if (this.loginForm.valid) {
    console.log("in func")
    let userData = {
      userName: this.loginForm.controls['userName'].value,
      password: this.loginForm.controls['password'].value
    }
    this.service.loginuser(userData).subscribe({
      next: (response: any) => {
        console.log(response),
          localStorage.setItem("accesstoken", response.accessToken.value),
          localStorage.setItem("refreshtoken", response.refreshToken.value),
          this.router.navigate(["admindashboard"]);

        alert("Login Successfull")
        this.loginForm.reset;
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

}
