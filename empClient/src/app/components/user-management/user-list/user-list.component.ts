import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  role: number;
  constructor(
    private userService: UserService,
    private modalService: NgbModal
  ) {
    this.role = parseInt(localStorage.getItem('role') as string);
  }

  ngOnInit(): void {
    this.listUsers();
  }

  page: number = 1;

  sortBy: string = 'job_id';

  limit: number = 5;

  search: string = '';

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
    console.log('sort by : ', sortBy);

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
    this.userService.deleteUser(userId).subscribe({
      next: (response: any) => {
        console.log('deleted', userId, response);
        this.listUsers();
      },
      error(err) {
        console.log(err);
      },
    });
  }

  checked: Array<number> = [];

  checkedUser(event: any) {
    if (event.target.checked) {
      this.checked.push(Number(event.target.attributes.value.value));

      if (this.userList.result.length == this.checked.length) {
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
    this.userList.result.forEach((element: any) => {
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
    this.userService.deleteUsers(this.checked).subscribe({
      next: (response: any) => {
        console.log('deleted', this.checked, response);
        this.listUsers();
      },
      error(err) {
        console.log(err);
      },
    });

    (document.getElementById('selectAll') as HTMLInputElement).checked = false;
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
}
