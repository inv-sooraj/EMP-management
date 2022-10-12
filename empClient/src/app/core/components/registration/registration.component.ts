import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent implements OnInit {
  constructor(private route: Router, private service: AuthService) {}

  ngOnInit(): void {}

  registrationForm = new FormGroup({
    userName: new FormControl('', Validators.required),
    name: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9 \\!"#\\$%&\'\\(\\)\\*\\+,\\-\\.\\/\\:;\\<\\=\\>\\?@\\[\\\\\\]\\^_`\\{\\|\\}~]+$'
      ),
    ]),
    cpass: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9 \\!"#\\$%&\'\\(\\)\\*\\+,\\-\\.\\/\\:;\\<\\=\\>\\?@\\[\\\\\\]\\^_`\\{\\|\\}~]+$'
      ),
    ]),
    role: new FormControl('', Validators.required),
  });

  signUp() {
    if (this.registrationForm.valid) {
      console.warn('Form Submitted');
      console.log(this.registrationForm.value);
      this.service.regUser(this.registrationForm.value).subscribe({
        next: (response: any) => {
          console.log(response), this.route.navigate(['login']);
        },
        error: (error: any) => {
          console.log(error);
          if (error.error.message == 'UserName Already Exists') {
            alert('Username already exists');
          } else {
            alert('Email already exists');
          }
        },
      });
    }
  }
}
