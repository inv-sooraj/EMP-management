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
  userData:any
  count: any
  total: any

  page:number = 1
  limit:number=5;
  sortBy:string='user_Id'
  search:string='';

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
    .append('search', this.search);
    this.userService.getUsers(queryParams).subscribe({
      next: (response: any) => {
        if (response) {
          console.log(response);
          this.users = response;
          this.userData=response.result;
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
    this.sortBy = sortBy;
    this.page = 1;
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
    console.log(event.target.checked);
    console.log(event.target.attributes.value.value);

    if (event.target.checked) {
      this.checked.push(event.target.attributes.value.value);
    } else {
      this.checked.splice(
        this.checked.indexOf(event.target.attributes.value.value),
        1
      );
    }

    console.log(this.checked.indexOf(5));

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

  abcd(event: any) {
    console.log(event);

    this.users.result.forEach((element: any) => {
      console.log(element.jobId);

      document.getElementById('checkbox' + element.jobId)?.click();
    });
  }

}
