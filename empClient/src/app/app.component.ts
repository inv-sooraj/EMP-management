import { Component, DoCheck, OnInit } from '@angular/core';
import { AuthService } from './core/service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit   {
 constructor(private service:AuthService){}
 
  ngOnInit(): void {
    this.service.checkExpired()
  }
}

  



