import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  constructor(private route: Router, private modalService: NgbModal) {}

  ngOnInit(): void {
    this.role();
  }

  logOut() {
    localStorage.clear();
    this.route.navigate(['login']);
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }
  flag: number = 0;

  role() {
    let role = Number(localStorage.getItem('key'));

    if (role == 0) {
      this.flag = 1;
    } else if (role == 1) {
      this.flag = 0;
    } else {
      this.flag = 2;
    }
  }
}
