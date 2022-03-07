import { ApiService } from './../api.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SocialAuthService, SocialUser } from 'angularx-social-login';
import { Order, User } from '../models';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css'],
})
export class HistoryComponent implements OnInit {
  socialUser!: SocialUser;
  userObj!: User;
  orderHistory!: Order[];

  constructor(
    private router: Router,
    public socialAuthService: SocialAuthService,
    private apiSvc: ApiService
  ) {}

  ngOnInit(): void {
    this.socialAuthService.authState.forEach(
      (value) => (this.socialUser = value)
    );
    console.log(this.socialUser);
    this.userObj = {
      email: this.socialUser.email,
    };
    this.getHistory();
  }

  async getHistory() {
    await this.apiSvc.getHistory(this.userObj).then((result) => {
      this.orderHistory = result;
      this.orderHistory.forEach(
        (item) =>
          (item.timeStamp = item.timeStamp?.replace('T', ' ').replace('Z', ''))
      );
    });
  }
}
