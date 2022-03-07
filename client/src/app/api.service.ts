import { IEXquote, Message, Order, Shares, User } from './models';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private httpClient: HttpClient) {}

  getQuote(ticker: string): Promise<IEXquote> {
    return lastValueFrom(this.httpClient.get<IEXquote>('/api/stock/' + ticker));
  }

  createUser(user: User): Promise<Message> {
    return lastValueFrom(this.httpClient.post<any>('/api/user', user));
  }

  buy(order: Order): Promise<Message> {
    return lastValueFrom(this.httpClient.post<any>('/api/trade/buy', order));
  }

  sell(order: Order): Promise<Message> {
    return lastValueFrom(this.httpClient.post<any>('/api/trade/sell', order));
  }

  getHistory(user: User): Promise<Order[]> {
    return lastValueFrom(this.httpClient.post<any>('/api/trade/history', user));
  }

  getPortfolio(user: User): Promise<Shares[]> {
    return lastValueFrom(
      this.httpClient.post<any>('/api/user/portfolio', user)
    );
  }

  getCashBalance(user: User): Promise<User> {
    return lastValueFrom(this.httpClient.post<any>('/api/user/cash', user));
  }
}
