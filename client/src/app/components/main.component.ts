import { ToolbarService } from './../toolbar.service';
import { SocialAuthService, SocialUser } from 'angularx-social-login';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Shares, User } from '../models';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {
  socialUser!: SocialUser;
  userObj!: User;
  portfolio: Shares[] = [];
  totalValue: number = 0;

  constructor(
    private router: Router,
    public socialAuthService: SocialAuthService,
    private toolbarService: ToolbarService,
    private apiSvc: ApiService
  ) {}

  ngOnInit(): void {
    this.socialAuthService.authState.forEach(
      (value) => (this.socialUser = value)
    );
    this.userObj = {
      email: this.socialUser.email,
    };
    this.getShares();
    this.getCashBalance();
    // this.populateLivePrices();
  }

  logout(): void {
    this.socialAuthService.signOut().then(() => {
      this.toolbarService.logout;
      this.router.navigate(['login']);
    });
  }

  async getShares() {
    await this.apiSvc.getPortfolio(this.userObj).then((result) => {
      result.forEach((v) => {
        this.apiSvc.getQuote(v.symbol).then((quote) => {
          v.quotes = quote;
          this.totalValue += v.quotes.price * v.quantity;
        });
      });
      this.portfolio = result;
    });
  }

  async getCashBalance() {
    await this.apiSvc.getCashBalance(this.userObj).then((result) => {
      this.userObj = result;
      if (this.userObj.cash) this.totalValue += this.userObj.cash;
    });
  }
}
