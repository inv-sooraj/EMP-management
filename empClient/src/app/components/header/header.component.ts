import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  getName(): string {
    return localStorage.getItem('name') as string;
  }

  getRole(): number {
    return parseInt(localStorage.getItem('role') as string);
  }

  getStat(): string {
    if (localStorage.getItem('accessToken')) return 'Logout';
    else return 'Login';
  }
}
