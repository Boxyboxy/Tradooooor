import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ToolbarService {
  loggedIn: boolean = false;
  constructor() {}

  login() {
    this.loggedIn = true;
  }

  logout() {
    this.loggedIn = false;
  }
}
