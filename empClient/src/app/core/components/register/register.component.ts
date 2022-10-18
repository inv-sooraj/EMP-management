import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HotToastService } from '@ngneat/hot-toast';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../service/auth.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  constructor(private service: AuthService, private router: Router,private toaster:ToastrService,private toastService: HotToastService) {}
  role: number = 0;

  ngOnInit(): void {
    this.showToast();
  }

  showToast() {
    this.toastService.show('Hello World!')
  }

  registrationForm: FormGroup = new FormGroup({
    role: new FormControl('0', Validators.required),
    name: new FormControl('',[ Validators.required,Validators.maxLength(50)]),
    userName: new FormControl('', [ Validators.required,Validators.maxLength(50)]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}'),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=)(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}'
      ),
    ]),
    confirmPassword: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$'
      ),
    ]),
  });

  register() {
    if (!this.registrationForm.valid) {
      console.log('Validation Failed');
      this.registrationForm.markAllAsTouched();
      return;
    }

    if (
      this.registrationForm.controls['confirmPassword'].value !=
      this.registrationForm.controls['password'].value
    ) {
      this.toastService.warning("Password Should Match")
      return;
    }

    let param = {
      role: this.registrationForm.controls['role'].value,
      name: this.registrationForm.controls['name'].value,
      userName: this.registrationForm.controls['userName'].value,
      email: this.registrationForm.get('email')?.value,
      password: this.registrationForm.controls['password'].value,
    };

    this.service.register(param).subscribe({
      next: (response: any) => {
      console.log('User Registered', response);
      this.toastService.success('Registered Successfully!')
      this.router.navigate(['login']);
      },
      error: (error: any) => {
        console.log('error', error.error);
        if (error.error.status == 400) {
          if (error.error.message == 'Username Already Exists') {
            console.log('Username Already Exists');
            this.toastService.warning("Username Already Exists")
          } else if (error.error.message == 'Email Already Exists') {
            console.log('Email Already Exists');
            this.toastService.warning("Email Already Exists")
            
          }
        }
      },
    });
  }
}
