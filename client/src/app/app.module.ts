import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgxCaptchaModule} from "ngx-captcha";
import {ReactiveFormsModule} from "@angular/forms";
import {AppService} from "./app.service";
import {HttpClientModule} from "@angular/common/http";
import {RecaptchaV3Component} from './recaptcha-v3/recaptcha-v3.component';
import {RecaptchaV3Service} from "./recaptcha-v3/recaptcha-v3.service";

@NgModule({
  declarations: [
    AppComponent,
    RecaptchaV3Component
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    NgxCaptchaModule,
    HttpClientModule
  ],
  providers: [AppService, RecaptchaV3Service],
  bootstrap: [AppComponent]
})
export class AppModule {
}
