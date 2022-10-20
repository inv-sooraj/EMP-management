import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { HeaderComponent } from '../../header/header.component';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css'],
})
export class UserEditComponent implements OnInit {
  @Input() patchValues = {};

  @Output() public completedEvent = new EventEmitter();

  constructor(private userService: UserService) {}

  qualifications =this.userService.qualifications

  userEditForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required,Validators.maxLength(50)]),
    dob: new FormControl('',Validators.maxLength(13)),
    phone: new FormControl('',Validators.maxLength(13)),
    address: new FormControl('',Validators.maxLength(255)),
    city: new FormControl('',Validators.maxLength(255)),
    zipCode: new FormControl('',Validators.maxLength(255)),
    qualification: new FormControl('', [Validators.required,Validators.maxLength(1)]),
  });

  ngOnInit(): void {
    this.userEditForm.patchValue(this.patchValues);
  }

  editUser() {
    if (!this.userEditForm.valid) {
      console.log('Validation Failed');
      this.userEditForm.markAllAsTouched();
      return;
    }

    let param = {
      name: this.userEditForm.controls['name'].value,
      dob: this.userEditForm.controls['dob'].value,
      phone: this.userEditForm.controls['phone'].value,
      address: this.userEditForm.controls['address'].value,
      city: this.userEditForm.controls['city'].value,
      zipCode: this.userEditForm.controls['zipCode'].value,
      qualification: this.userEditForm.get('qualification')?.value,
    };

    this.userService.editUser(param).subscribe({
      next: (response: any) => {
        console.log(response);

        localStorage.setItem('name', response.name);

        this.completedEvent.emit();
      },
      error(err) {
        console.log(err);
      },
    });
  }
}
