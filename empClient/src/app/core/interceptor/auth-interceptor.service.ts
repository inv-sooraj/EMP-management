import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs';

import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor{

  constructor(private injector:Injector) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authService=this.injector.get(AuthService)
    if (localStorage.getItem('accessToken')) {
      let tokenizedReq = req.clone({
        setHeaders: {
          Authorization: `Employee ${authService.getToken()}`,
        },
      });

      return next.handle(tokenizedReq);
    }
    return next.handle(req);

      
      
  }

}
