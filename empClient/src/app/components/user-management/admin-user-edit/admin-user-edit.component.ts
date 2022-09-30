import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-admin-user-edit',
  templateUrl: './admin-user-edit.component.html',
  styleUrls: ['./admin-user-edit.component.css'],
})
export class AdminUserEditComponent implements OnInit {
  constructor(private userService: UserService) {}

  @Input() userId: number = 0;

  @Output() public completedEvent = new EventEmitter();

  ngOnInit(): void {
    this.getUser();
  }

  userEditForm: FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    userName: new FormControl('', Validators.required),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}'),
    ]),
  });

  getUser() {
    this.userService.getUser(this.userId).subscribe({
      next: (response: any) => {
        console.log(response);

        this.userEditForm.patchValue(response);
      },
      error(err) {
        console.log(err);
      },
    });
  }

  update() {
    if (!this.userEditForm.valid) {
      console.log('Validation Failed');
      this.userEditForm.markAllAsTouched();
      return;
    }

    let body = {
      name: this.userEditForm.controls['name'].value,
      userName: this.userEditForm.controls['userName'].value,
      email: this.userEditForm.controls['email'].value,
    };

    this.userService.updateUser(this.userId, body).subscribe({
      next: (response: any) => {
        console.log(response);
        this.completedEvent.emit();
      },
      error(err) {
        console.log(err);
      },
    });
  }
}
