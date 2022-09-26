import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
active: any="dashboard";

  constructor(private router:Router) { }

  ngOnInit(): void {
  }
  logout(){
    document.getElementById('cancellogout')?.click();
    localStorage.clear();
    this.router.navigate(['login'])
  }
  dashboard(){
    this.router.navigate(['admin']);

  }
  userList(){
    this.router.navigate(['admin']);
  }
  jobList(){
    this.router.navigate(['admin']);
  }
  addJob(){
    this.router.navigate(['admin']);

  }

}
