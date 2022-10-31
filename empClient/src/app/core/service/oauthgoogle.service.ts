import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserInfo {
  info: {
    sub: string;
    email: string;
    name: string;
    picture: string;
  };
}

const oAuthConfig: AuthConfig = {
  issuer: 'https://accounts.google.com',
  strictDiscoveryDocumentValidation: false,
  redirectUri: window.location.origin,
  clientId:
    '97587627218-qgaitnpfp5q9nu8br0k4li9762b57scc.apps.googleusercontent.com',
  scope: 'profile email',
};

@Injectable({
  providedIn: 'root',
})
export class OAuthGoogleService {
  userProfileSubject = new Subject<UserInfo>();

  constructor(
    private readonly oAuthService: OAuthService,
    private http: HttpClient
  ) {}

  signIn() {
    this.oAuthService.configure(oAuthConfig);

    // this.oAuthService.logoutUrl = 'https://www.google.com/accounts/Logout';

    this.oAuthService.redirectUri = 'http://localhost:4200/glogin';

    // this.oAuthService.postLogoutRedirectUri = 'http://localhost:8080';

    this.oAuthService.loadDiscoveryDocument().then(() => {
      this.oAuthService.tryLoginImplicitFlow().then(() => {
        if (!this.oAuthService.hasValidAccessToken()) {
          this.oAuthService.initLoginFlow();
        } else {
          this.oAuthService.loadUserProfile().then((userProfile) => {
            console.log(JSON.stringify(userProfile, undefined, 4));
            this.userProfileSubject.next(userProfile as UserInfo);
          });
        }
      });
    });
  }

  isGoogleLoggedIn(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  googleLogout() {
    this.oAuthService.revokeTokenAndLogout();
  }

  verifyEmail(param: HttpParams): Observable<any> {
    return this.http.get(environment.apiUrl + '/oauth2/verify-email', {
      params: param,
      responseType: 'text',
    });
  }

  register(body: any, role: number): Observable<any> {
    return this.http.post(
      environment.apiUrl + '/oauth2/register/' + role,
      body
    );
  }

  login(body: any): Observable<any> {
    return this.http.post(environment.apiUrl + '/oauth2/login', body);
  }
}
