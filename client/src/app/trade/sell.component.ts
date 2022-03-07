import { Message } from './../models';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SocialAuthService, SocialUser } from 'angularx-social-login';
import { ApiService } from '../api.service';
import { IEXquote, Order } from '../models';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css'],
})
export class SellComponent implements OnInit {
  user!: SocialUser;
  form!: FormGroup;
  stock!: IEXquote;
  order: Order | undefined;
  message!: Message;

  constructor(
    private router: Router,
    public socialAuthService: SocialAuthService,
    private apiSvc: ApiService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
    this.socialAuthService.authState.forEach((value) => (this.user = value));
  }

  createForm(): FormGroup {
    return this.fb.group({
      symbol: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
      shares: this.fb.control(null, [Validators.required, Validators.min(1)]),
    });
  }

  async sellStock() {
    console.log(this.form.get('symbol')?.value);
    await this.apiSvc.getQuote(this.form.get('symbol')?.value).then((v) => {
      this.stock = v;
    });
    console.log(this.stock);
    let date = new Date();
    let currentTimeStamp =
      date.getUTCFullYear() +
      '-' +
      ('00' + (date.getUTCMonth() + 1)).slice(-2) +
      '-' +
      ('00' + date.getUTCDate()).slice(-2) +
      ' ' +
      ('00' + date.getUTCHours()).slice(-2) +
      ':' +
      ('00' + date.getUTCMinutes()).slice(-2) +
      ':' +
      ('00' + date.getUTCSeconds()).slice(-2);
    console.log(currentTimeStamp);
    this.order = {
      email: this.user?.email,
      symbol: this.form.get('symbol')?.value,
      shares: -this.form.get('shares')?.value,
      price: this.stock.price,
      timeStamp: currentTimeStamp,
    };
    console.log(this.order);
    // todo, sell function on service
    await this.apiSvc
      .sell(this.order)
      .then((v) => {
        console.log(v);
        this.message = v;
        alert(this.message.message);
        this.router.navigate(['/']);
      })
      .catch((err) => {
        console.log(err.error);
        this.message = err.error;
        alert(this.message.error);
      });
  }
}
