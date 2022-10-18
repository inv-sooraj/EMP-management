import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from 'src/app/core/service/auth.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  role: number;

  showSpinner: boolean = false

  status = this.userService.status;

  roles = this.userService.roles;

  roleStatusCount: { [key: string]: number } = {};

  constructor(
    private userService: UserService,
    private modalService: NgbModal,
    private service: AuthService
  ) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  ngOnInit(): void {
    this.listUsers();
    this.getRoleStat();
  }

  page: number = 1;

  sortBy: string = 'userId';

  limit: number = 5;

  search: string = '';

  sortDesc: boolean = false;

  userList: any = {};

  numSeq(n: number): Array<number> {
    return Array(n);
  }

  prevPage() {
    this.page -= 1;
    this.listUsers();
  }

  gotoPage(page: number) {
    this.page = page;
    this.listUsers();
  }

  nextPage() {
    this.page += 1;
    this.listUsers();
  }

  setSort(sortBy: string) {
    if (this.userList.result.length <= 1) {
      return;
    }

    if (this.sortBy == sortBy) {
      this.sortDesc = this.sortDesc ? false : true;
    } else {
      this.sortDesc = false;
    }

    console.log('sort by : ', sortBy, ', desc : ', this.sortDesc);

    this.sortBy = sortBy;
    this.page = 1;
    this.listUsers();
  }

  setLimit() {
    console.log(this.limit);
    this.page = 1;
    this.listUsers();
  }

  setSearch() {
    console.log(this.search);
    this.listUsers();
  }

  listUsers(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sortBy', this.sortBy)
      .append('desc', this.sortDesc)
      .append('search', this.search);

    this.userService.getUsers(queryParams).subscribe({
      next: (response: any) => {
        this.userList = response;
        console.log(response);
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  deleteUser(userId: number) {
    this.showSpinner = true

    this.userService.deleteUser(userId).subscribe({
      next: (response: any) => {
        this.showSpinner = false

        console.log('deleted', userId, response);
        this.listUsers();
      },
      error:(err) =>{
        this.showSpinner = false
        console.log(err);
      },
    });
  }

  checkedUserIds: Set<number> = new Set();

  checkAllButton(): boolean {
    let temp = true;

    if (this.userList.result) {
      this.userList.result.forEach((val: any) => {
        if (!this.checkedUserIds.has(val.userId)) {
          temp = false;
        }
      });
    }

    return temp;
  }

  checkedUser(event: any) {
    if (event.target.checked) {
      this.checkedUserIds.add(parseInt(event.target.attributes.value.value));
    } else {
      this.checkedUserIds.delete(parseInt(event.target.attributes.value.value));
    }

    console.log(this.checkedUserIds);
  }

  checkAll(event: any) {
    this.userList.result.forEach((element: any) => {
      if (event.target.checked) {
        if (!this.checkedUserIds.has(element.userId)) {
          this.checkedUserIds.add(element.userId);
        }
      } else {
        this.checkedUserIds.delete(element.userId);
      }
    });

    console.log(this.checkedUserIds);
  }

  deleteUsers(): void {
    if (this.checkedUserIds.size <= 0) {
      return;
    }
    this.userService.deleteUsers(Array.from(this.checkedUserIds)).subscribe({
      next: (response: any) => {
        console.log('deleted', this.checkedUserIds, response);
        this.listUsers();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  downloadCsv(): void {
    this.userService.downloadCsv().subscribe({
      next: (response: any) => {
        let anchor = document.createElement('a');
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(
          new Blob([response.body], { type: response.body.type })
        );
        anchor.click();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  userId: number = 0;

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  getRoleStat(): void {
    this.roleStatusCount = {
      ADMIN: 0,
      EMPLOYER: 0,
      EMPLOYEE: 0,
    };
    this.userService.getRoleStat().subscribe({
      next: (response: any) => {
        console.log('Stat', response);

        response.forEach((element: any) => {
          console.log(element);

          this.roleStatusCount[this.roles[element.status]] = element.count;
        });
      },
      error(err) {
        console.log(err);
      },
    });
  }
}
