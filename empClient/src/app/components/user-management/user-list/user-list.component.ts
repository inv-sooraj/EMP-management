import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/service/auth.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  role: number;

  showSpinner: boolean = false;

  status = this.userService.status;

  roles = this.userService.roles;

  roleStatusCount: { [key: string]: number } = {};

  constructor(
    private userService: UserService,

    private modalService: NgbModal,
    private service: AuthService,
    private toastService: ToastrService
  ) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }
  today: string = '';

  ngOnInit(): void {
    this.today = new Date().toISOString().split('T')[0];

    this.csvData.endDate.value = this.today;

    this.listUsers();
  }

  page: number = 1;

  sortBy: string = 'userId';

  limit: number = 0;

  search: string = '';

  sortDesc: boolean = false;

  pagerInfo: any = {};

  userDataList: Array<any> = [];

  selectedStatus: number = 3;
  selectedRole: number = 3;

  // numSeq(n: number): Array<number> {
  //   let arr = new Array<number>();

  //   if (this.pagerInfo.numPages <= 5) {
  //     for (let index = 1; index <= this.pagerInfo.numPages; index++) {
  //       arr.push(index);
  //     }
  //     return arr;
  //   }

  //   let start;
  //   if (this.pagerInfo.currentPage > this.pagerInfo.numPages - 2) {
  //     start = this.pagerInfo.numPages - 2;
  //   } else {
  //     start = this.pagerInfo.currentPage < 4 ? 3 : this.pagerInfo.currentPage;
  //   }

  //   for (let index = start - 2; index < start + 3; index++) {
  //     arr.push(index);
  //   }
  //   return arr;
  // }

  // prevPage() {
  //   this.page -= 1;
  //   this.listUsers();
  // }

  // gotoPage(page: number) {
  //   if (this.page == page) return;
  //   this.page = page;
  //   this.listUsers();
  // }

  // nextPage() {
  //   this.page += 1;
  //   this.listUsers();
  // }

  setPage(event: any): void {
    if (!event.target.value) return;

    let page: number = parseInt(event.target.value);

    if (
      this.page == page ||
      (this.page == this.pagerInfo.numPages &&
        page > this.pagerInfo.numPages) ||
      (this.page == 1 && page < 1)
    )
      return;

    if (page < 1) this.page = 1;
    else if (page > this.pagerInfo.numPages)
      this.page = this.pagerInfo.numPages;
    else this.page = page;

    this.listUsers();
  }

  setSort(sortBy: string) {
    this.userDataList = [];

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

  resetList() {
    this.userDataList = [];
    this.page = 1;
    this.listUsers();
  }

  listUsers(): void {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append(
        'limit',
        this.limit ? this.limit : (window.innerHeight / 100).toFixed(0)
      )
      .append('sortBy', this.sortBy)
      .append('desc', this.sortDesc)
      .append('status', this.selectedStatus)
      .append('role', this.selectedRole)
      .append('search', this.search);

    this.userService.getUsers(queryParams).subscribe({
      next: (response: any) => {
        this.pagerInfo = response.pagerInfo;
        this.pagerInfo['numPages'] = response.numPages;
        this.pagerInfo['currentPage'] = response.currentPage;

        if (this.limit == 0) {
          this.userDataList.push(...response.result);
        } else {
          this.userDataList = response.result;
        }

        console.log(this.userDataList);

        console.log(response);

        this.getRoleStat();
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  deleteUser(userId: number) {
    this.showSpinner = true;

    this.userService.deleteUser(userId).subscribe({
      next: (response: any) => {
        this.showSpinner = false;

        console.log('deleted', userId, response);

        this.userDataList.splice(
          this.userDataList.findIndex((x) => x.userId == userId),
          1,
          response
        );
      },
      error: (err) => {
        this.showSpinner = false;

        console.log(err);
      },
    });
  }

  userEditComplete(response: any): void {
    this.userDataList.splice(
      this.userDataList.findIndex((x) => x.userId == response.userId),
      1,
      response
    );
  }

  checkedUserIds: Set<number> = new Set();

  checkAllButton(): boolean {
    let temp = true;

    if (this.userDataList) {
      this.userDataList.forEach((val: any) => {
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
    this.userDataList.forEach((element: any) => {
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
    this.showSpinner = true;

    this.userService.deleteUsers(Array.from(this.checkedUserIds)).subscribe({
      next: (response: any) => {
        this.showSpinner = false;
        console.log('deleted', this.checkedUserIds, response);

        response.forEach((element: any) => {
          this.userDataList.splice(
            this.userDataList.findIndex((x) => x.userId == element.userId),
            1,
            element
          );
        });
      },
      error: (err) => {
        this.showSpinner = false;
        console.log(err);
      },
    });
  }

  downloadCsv(): void {
    if (!this.csvData.startDate.valid || !this.csvData.endDate.valid) {
      this.csvData.startDate.touched = true;
      return;
    }

    let queryParams = new HttpParams()
      .append('roles', Array.from(this.csvData.roles).join(',').toString())
      .append('status', Array.from(this.csvData.status).join(',').toString())
      .append('startDate', this.csvData.startDate.value.replaceAll('-', '/'))
      .append('endDate', this.csvData.endDate.value.replaceAll('-', '/'));

    this.userService.downloadCsv(queryParams).subscribe({
      next: (response: any) => {
        console.log(response);

        let anchor = document.createElement('a');
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(
          new Blob([response.body], { type: response.body.type })
        );
        anchor.click();

        this.modalService.dismissAll();
      },

      error: (err: any) => {
        console.log(err);
        if (err.status == 404) {
          this.toastService.warning('No Records Found!');
        } else if (err.status == 400) {
          err.error.text().then((text: any) => {
            this.toastService.error(JSON.parse(text).message);
          });
        }
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
        response.forEach((element: any) => {
          this.roleStatusCount[this.roles.get(element.status) as string] =
            element.count;
        });
      },
      error(err) {
        console.log(err);
      },
    });
  }

  csvData: any = {
    roles: new Set<number>(this.roles.keys()),
    status: new Set<number>(this.status.keys()),
    startDate: new FormControl('', [Validators.required]),
    endDate: new FormControl('', [Validators.required]),
  };

  changeCsvStat(event: any, stat: any, data: Set<number>): void {
    if (event.target.checked) {
      data.add(stat);
    } else {
      data.delete(stat);
    }

    console.log(this.csvData);
  }

  openLg(content: any) {
    this.modalService.open(content, { size: 'lg' });
  }

  throttle = 300;
  scrollDistance = 1;
  scrollUpDistance = 2;

  onScrollDown() {
    if (this.limit) {
      return;
    }
    this.page += 1;
    this.listUsers();
  }

  onUp() {
    console.log('Up');
  }
}
