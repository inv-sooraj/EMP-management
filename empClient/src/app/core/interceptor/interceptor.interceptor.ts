import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError, EMPTY, Observable, switchMap } from 'rxjs';
import { AuthService } from '../service/auth.service';

@Injectable()
export class InterceptorInterceptor implements HttpInterceptor {
  constructor(private service: AuthService) {}

  getAccessToken(): any {
    return localStorage.getItem('accessToken');
  }

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    let accessToken = this.getAccessToken();

    if (accessToken && !request.url.endsWith('login')) {
      request = request.clone({
        setHeaders: {
          Authorization: 'Emp ' + accessToken,
        },
      });
      return next.handle(request).pipe(
        catchError((error: any) => {
          if (
            (error instanceof HttpErrorResponse && error.status == 403) ||
            error.status == 401
          ) {
            return this.refreshAccess(request, next);
          }
          return EMPTY;
        })
      );
    } else {
      return next.handle(request);
    }
  }

  private refreshAccess(request: HttpRequest<any>, next: HttpHandler) {
    return this.service.newToken().pipe(
      switchMap((res: any) => {
        console.log(res);
        localStorage.setItem('accessToken', res.accessToken.value);
        localStorage.setItem('accessTokenExpiry', res.accessToken.expiry);

        request = request.clone({
          setHeaders: {
            'Content-Type': 'application/json',
            Authorization: 'Emp ' + localStorage.getItem('accessToken'),
          },
        });
        return next.handle(request);
      })
    );
  }
}
