import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(private route: Router) {}
  canActivate() {
    let Role = parseInt(localStorage.getItem('key') as string);
    console.log(Role);

    if (Role == 0) {
      return true;
    } else {
      alert('illegal access');
      this.route.navigate(['userDashboard']);
      return false;
    }
  }
}
