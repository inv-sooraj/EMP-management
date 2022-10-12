import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class LoginAuthGuard implements CanActivate {
  constructor(private auth: AuthService, private route: Router) {}

  canActivate() {
    if (!this.auth.IsLoggedIn()) {
      return true;
    }
    this.route.navigate(['userDashboard']);
    return false;
  }
}
