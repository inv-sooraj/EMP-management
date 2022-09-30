import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserManagementService } from 'src/app/services/user-management.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
})
export class AdminDashboardComponent implements OnInit {
  details: any;
  qualifications: { [key: number]: string } = {
    0: 'SSLC ',
    1: 'PLUS TWO',
    2: 'UG ',
    3: 'PG ',
  };

  role: { [key: number]: string } = {
    0: 'Admin ',
    1: 'Employer',
    2: 'Employee ',
  };

  constructor(private route: Router, private service: UserManagementService) {}

  page: number = 1;
  search: string = '';

  ngOnInit(): void {
    // this.userView();
    this.pagination();
  }

  // logOut() {
  //   localStorage.clear();
  //   this.route.navigate(['login']);
  // }

  // userView() {
  //   this.service.display().subscribe((data) => {
  //     this.details = data;
  //     console.log(this.details);
  //   });
  // }

  editUser(info: any) {
    this.service.userId=info;
    this.route.navigate(['editUser']);
  }

  userDelete(info: any) {
    this.service.deleteUser(info).subscribe({
      next: (response: any) => {
        console.log(response);
        // this.userView();
        this.pagination();
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  userEdit(arg: any) {
    // this.service.id = arg;
    this.route.navigate(['addEditUser']);
  }

  getParams() {
    return new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sort', 'user_id')
      .append('search', this.search);
  }

  pagination() {
    this.service.navPage(this.getParams()).subscribe({
      next: (response: any) => {
        console.log(response);
        this.details = response;
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  next() {
    this.page += 1;

    this.pagination();
  }

  previous() {
    this.page -= 1;

    this.pagination();
  }
  numSeq(n: number) {
    return Array(n);
  }
  gotoPage(page: number) {
    this.page = page;
    this.pagination();
  }

  userSearch() {
    this.page = 1;
    this.pagination();
  }

  csvDownload() {
    this.service.downloadCsv().subscribe({
      next: (response: any) => {
        console.log(response);

        let anchor = document.createElement('a');
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(
          new Blob([response.body], { type: response.body.type })
        );
        anchor.click();
      },
      error: (error: any) => {
        console.log(error);
      },
    });
  }

  limit: number = 5;
setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.pagination();
  }
}
