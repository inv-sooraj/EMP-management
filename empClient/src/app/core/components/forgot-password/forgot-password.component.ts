import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  resetToken:any

  constructor(private service: AuthService,
    private router: Router,
    private activatedRouter:ActivatedRoute) { }


  resetPasswordForm: FormGroup = new FormGroup({
    password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$'), Validators.minLength(8)]),
    confirmPassword: new FormControl('', [Validators.required])
  })

  ngOnInit(): void {
    this.resetToken=this.activatedRouter.snapshot.params['url'];
  }
  resetPassword() {
    console.log(this.resetToken);
    
    if (this.resetPasswordForm.valid) {
      let newPswd = {
        password: this.resetPasswordForm.controls['password'].value
      };
      this.service.resetPswd(newPswd,this.resetToken).subscribe({
        next: (response: any) => {
          console.log(response);
          alert("Password changed successfully");
          this.router.navigate(['login']);
        },
        error: (error: any) => {
          console.log(error);
          alert(error.error.message );
        }

      });

    }
  }
}
