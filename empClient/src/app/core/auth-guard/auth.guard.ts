import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  getToken(){
    return !!localStorage.getItem('accesstoken')
  }
  constructor(private router: Router){}
  canActivate(): boolean {  
    if (this.getToken()){
      return true;
    }else {
      this.router.navigate(["login"]);
      return false;
    }
  }
  
}
