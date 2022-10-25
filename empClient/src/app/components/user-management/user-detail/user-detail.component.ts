import { Input, Component, OnInit } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css'],
})
export class UserDetailComponent implements OnInit {
  @Input() userId: number = 0;
  constructor(private userService: UserService) {}
  userDetail: any;
  ngOnInit(): void {
    this.getDetail();
  }

  role = this.userService.roles;
  qualifications = this.userService.qualifications;

  getDetail() {
    if (!this.userId) {
      return;
    }

    this.userService.getUser(this.userId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.userDetail = response
        if(response.hasProfilePic){
          this.getProfilePic(this.userId);
        }
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  getProfilePic(id:number): void {
    let queryParams = new HttpParams()
    .append('userId',id)
    this.userService.getProfile(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);

        (document.getElementById('profilePicture') as HTMLImageElement).src =
          URL.createObjectURL(new Blob([response], { type: response.type }));
      },
      error(err) {
        console.log(err);
      },
    });
  }
}
