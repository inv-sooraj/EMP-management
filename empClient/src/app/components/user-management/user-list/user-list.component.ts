import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from 'src/app/services/user-services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: any
  userData: any
  count: any
  total: any

  page: number = 1
  limit: number = 5;
  sortBy: string = 'user_Id'
  search: string = '';
  filter: number = 5;
  sortDesc: boolean = false;

  title = 'appBootstrap';

  closeResult: string = '';

  constructor(private userService: UserService, private router: Router, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.usersList();
  }

  usersList() {
    let queryParams = new HttpParams()
      .append('page', this.page)
      .append('limit', this.limit)
      .append('sortBy', this.sortBy)
      .append('desc', this.sortDesc)
      .append('filter', this.filter)
      .append('search', this.search);
    this.userService.getUsers(queryParams).subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.users = response;
          this.userData = response.result;
          this.count = response.result.length;
          this.total = response.numItems;
        }
      },
      error: (error: any) => { console.log(error) }
    })
  }
  addUser() {
    this.userService.status = 0;
  }

  changePage(no: any) {
    this.page = no;
    this.usersList();
  }

  edit(userId: any) {
    this.userService.status = 1;
    this.userService.userId = userId;
  }

  delete(userId: any) {
    this.userService.deleteUser(userId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.usersList();
      },
      error: (error: any) => { console.log(error) }
    })
  }
  numSeq(n: number): Array<number> {
    return Array(n);
  }

  prevPage() {
    this.page -= 1;
    this.usersList();
  }

  gotoPage(page: number) {
    this.page = page;
    this.usersList();
  }

  nextPage() {
    this.page += 1;
    this.usersList();
  }
  setSort(sortBy: string) {

    if (this.users.result.length <= 1) {
      return;
    }

    if (this.sortBy == sortBy) {
      this.sortDesc = this.sortDesc ? false : true;
    } else {
      this.sortDesc = false;
    }
    this.sortBy = sortBy;
    this.page = 1;
    this.usersList();
  }
  setFilter() {
    console.log(this.limit);
    this.usersList();
  }

  setLimit() {
    console.log(this.limit);
    this.usersList();
  }

  setSearch() {
    console.log(this.search);
    this.usersList();
  }


  download() {
    console.log("in fun");

    this.userService.downloadUsers().subscribe({
      next: (response: any) => {
        const anchor = document.createElement("a");
        anchor.download = response.headers.get('Content-Disposition');
        anchor.href = URL.createObjectURL(new Blob([response.body], { type: response.body.type }));
        anchor.click();
      },
      error: (error: any) => { console.log(error) }
    })

  }


  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  checked: Array<number> = [];

  checkedUser(event: any) {
    if (event.target.checked) {
      this.checked.push(Number(event.target.attributes.value.value));

      if (this.users.result.length == this.checked.length) {
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
    this.users.result.forEach((element: any) => {
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

  deleteJobs(): void {
    if (this.checked.length <= 0) {
      return;
    }
    this.userService.deleteUsers(this.checked).subscribe({
      next: (response: any) => {
        console.log('deleted', this.checked, response);
        this.usersList();
      },
      error(err) {
        console.log(err);
      },
    });

    document.getElementById('selectAll')?.click();
  }
}
