import { ToolbarService } from './toolbar.service';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { SocialAuthService, SocialUser } from 'angularx-social-login';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class AppComponent implements OnInit {
  socialUser!: SocialUser;
  title = 'NUS-ISS Broker';

  constructor(
    private router: Router,
    public toolbarService: ToolbarService,
    public socialAuthService: SocialAuthService
  ) {}

  ngOnInit(): void {
    this.socialAuthService.authState.forEach(
      (value) => (this.socialUser = value)
    );
  }
  logout() {
    this.socialAuthService.signOut().then(() => {
      this.toolbarService.logout();
      this.router.navigate(['login']);
    });
  }
}
