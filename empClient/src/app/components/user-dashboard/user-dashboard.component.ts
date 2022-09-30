import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserManagementService } from 'src/app/services/user-management.service';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css'],
})
export class UserDashboardComponent implements OnInit {
  details: any;
  flag: number = 0;
  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };

  constructor(private service: UserManagementService, private route: Router) {}

  ngOnInit(): void {
    this.currentUserView();
    this.isUser();
  }

  currentUserView() {
    this.service.getCurrentUserDetails().subscribe((data) => {
      this.details = data;
      console.log(this.details);
    });
  }

  isUser() {
    let role = Number(localStorage.getItem('key'));

    if (role == 2) {
      this.flag = 1;
    } else {
      this.flag = 0;
    }
  }

  logout() {
    localStorage.clear();
    this.route.navigate(['login']);
  }
  edit(info: any) {
    this.service.userId = info;
    this.route.navigate(['editUser']);
  }
}
