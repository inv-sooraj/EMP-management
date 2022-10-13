import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../service/auth.service';

@Injectable()
export class InterceptorInterceptor implements HttpInterceptor {
  constructor(private service: AuthService) { }

  getAccessToken(): any {
    return localStorage.getItem('accessToken');
  }


  

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    let accessToken = this.getAccessToken();
    

    if (accessToken) {
      request = request.clone({
        setHeaders: {
          Authorization: 'Emp ' + accessToken,
        },
      });
    }
    

    return next.handle(request);
  }
}
