import { QuoteComponent } from './components/quote.component';
import { MaterialModule } from './material.module';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {
  GoogleLoginProvider,
  SocialAuthServiceConfig,
  SocialLoginModule,
} from 'angularx-social-login';
import { LoginComponent } from './components/login.component';
import { AuthGuardService } from './auth-guard.service';
import { AboutComponent } from './components/about.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { BuyComponent } from './trade/buy.component';
import { SellComponent } from './trade/sell.component';
import { HistoryComponent } from './trade/history.component';

const appRoutes: Routes = [
  { path: '', component: MainComponent },

  { path: 'quote', component: QuoteComponent },
  { path: 'login', component: LoginComponent },
  { path: 'about', component: AboutComponent },
  { path: 'buy', component: BuyComponent, canActivate: [AuthGuardService] },
  { path: 'sell', component: SellComponent, canActivate: [AuthGuardService] },
  {
    path: 'history',
    component: HistoryComponent,
    canActivate: [AuthGuardService],
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  declarations: [
    AppComponent,
    QuoteComponent,
    MainComponent,
    LoginComponent,
    AboutComponent,
    BuyComponent,
    SellComponent,
    HistoryComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    FlexLayoutModule,
    MaterialModule,
    BrowserAnimationsModule,
    SocialLoginModule,
    GoogleMapsModule,
  ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: true,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '977280661891-17t7e8ks4dlkjftgmejpb5btuh1p06au.apps.googleusercontent.com'
            ),
          },
        ],
        onError: (err) => {
          console.error(err);
        },
      } as SocialAuthServiceConfig,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
