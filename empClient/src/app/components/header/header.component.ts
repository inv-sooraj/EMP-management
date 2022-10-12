import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  constructor(private router:Router) {}

  ngOnInit(): void {}

  logout(){
    localStorage.clear()
    this.router.navigate(['login'])
  }

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
