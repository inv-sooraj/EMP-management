import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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

  constructor(
    private route: Router,
    private service: UserManagementService,
    private modalService: NgbModal
  ) {}
  sortDesc: boolean = false;
  sortBy:string='user_id'
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

  // editUser(info: any) {
  //   this.service.userId = info;
  //   this.route.navigate(['editUser']);
  // }

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
      .append('sort', this.sortBy)
      .append('desc', this.sortDesc)
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

  limit: number = 4;
  setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.pagination();
  }

  userId: number = 0;
  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  checked: Array<number> = [];
  

  checkedUser(event: any) {
    if (event.target.checked) {
      this.checked.push(Number(event.target.attributes.value.value));

      if (this.details.result.length == this.checked.length) {
        (document.getElementById('selectAll') as HTMLInputElement).checked =
          true;
      }
    } else {
      this.checked.splice(
        this.checked.indexOf(Number(event.target.attributes.value.value)),
        1
      );
      (document.getElementById('selectAll') as HTMLInputElement).checked =
        false;
    }

    console.log(this.checked);
  }

  checkAll(event: any) {
    this.details.result.forEach((element: any) => {
      (
        document.getElementById('checkbox' + element.userId) as HTMLInputElement
      ).checked = event.target.checked;

      if (event.target.checked) {
        if (!this.checked.includes(element.userId)) {
          this.checked.push(element.userId);
        }
      } else {
        this.checked.splice(this.checked.indexOf(element.userId), 1);
      }
    });
    console.log(this.checked);
  }

  deleteUsers(): void {
    if (this.checked.length <= 0) {
      return;
    }
    this.service.deleteUsers(this.checked).subscribe({
      next: (response: any) => {
        console.log('deleted', this.checked, response);
        this.pagination();
      },
      error(error: any) {
        console.log(error);
      },
    });

    (document.getElementById('selectAll') as HTMLInputElement).checked = false;
  }
  setSort(sortBy: string) {
    if (this.details.result.length <= 1) {
      return;
    }
    
    if (this.sortBy == sortBy) {
      this.sortDesc = this.sortDesc ? false : true;
    } else {
      this.sortDesc = false;
    }
    this.sortBy = sortBy;
    this.page = 1;
    this.pagination();
  }
}
