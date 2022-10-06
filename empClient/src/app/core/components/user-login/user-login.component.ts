import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {
  showSpinner: boolean = false

  constructor(private service: AuthService,
     private router: Router,
     private modalService: NgbModal) { }
  loginForm: FormGroup = new FormGroup({
    userName: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'), Validators.minLength(8)])
  })

  forgotPswdForm: FormGroup = new FormGroup({
    email: new FormControl('',[ Validators.required,Validators.email]),
  })

  ngOnInit(): void {
    localStorage.clear();
  }
  register() {
    this.router.navigate(['register'])
  }

  login() {
    if (this.loginForm.valid) {
      console.log("in func")
      let userData = {
        userName: this.loginForm.controls['userName'].value,
        password: this.loginForm.controls['password'].value
      }
      this.service.loginUser(userData).subscribe({
        next: (response: any) => {
          console.log(response);
          localStorage.setItem("role",response.role);
          localStorage.setItem("accesstoken", response.accessToken.value);
          localStorage.setItem("refreshtoken", response.refreshToken.value);
          if (response.role == 0) {
            this.router.navigate(["admindashboard"]);
          } else if (response.role == 1) {
            this.router.navigate(["employerdashboard"]);
          } else if (response.role == 2) {
            this.router.navigate(["employeedashboard"]);
          }
          alert("Login Successfull");
        },
        error: (error: any) => {
          console.log(error);
          if (error.status == 404) {
            alert("User Not Found");
          } else if (error.status == 400) { alert("Invalid Password"); }
        }

      });
    } else this.loginForm.markAllAsTouched();
  }

  forgotPswd(){
    if(this.forgotPswdForm.valid){
      this.showSpinner=true
      let data = {
        email: this.forgotPswdForm.controls['email'].value
      }
      this.service.forgotPswd(data).subscribe({
        next: (response: any) => {
          this.showSpinner=false;
          console.log(response);
          document.getElementById('forgotPswdModal')?.click();
          alert("Password reset link has been sent to "+this.forgotPswdForm.controls['email'].value);
        },
        error: (error: any) => { 
          this.showSpinner=false;
          console.log(error);
          document.getElementById('forgotPswdModal')?.click(); }
      })
    }
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' })
  }

}
