import { ApiService } from './../api.service';
import { ToolbarService } from './../toolbar.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  GoogleLoginProvider,
  SocialAuthService,
  SocialUser,
} from 'angularx-social-login';
import { User } from '../models';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  user: User | undefined;
  socialUser!: SocialUser;

  constructor(
    private router: Router,
    private authService: SocialAuthService,
    private socialAuthService: SocialAuthService,
    private toolbarService: ToolbarService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {}

  async signInHandler(): Promise<void> {
    await this.authService
      .signIn(GoogleLoginProvider.PROVIDER_ID)
      .then((data) => {
        this.toolbarService.login();
        localStorage.setItem('google_auth', JSON.stringify(data));
        this.socialAuthService.authState.forEach(
          (value) => (this.socialUser = value)
        );

        this.user = {
          email: this.socialUser?.email,
        };

        this.apiService.createUser(this.user).then((v) => console.log(v));
        this.router.navigateByUrl('/').then();
      });
  }
}
