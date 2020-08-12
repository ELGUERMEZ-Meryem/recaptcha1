import {Component, OnInit} from '@angular/core';
import {ReCaptchaV3Service} from "ngx-captcha";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-recaptcha-v3',
  templateUrl: './recaptcha-v3.component.html'
})
export class RecaptchaV3Component implements OnInit {
  private recaptchaKey: string;

  constructor(private reCaptchaV3Service: ReCaptchaV3Service) {
  }

  ngOnInit(): void {
    this.recaptchaKey = environment.recaptchav3Key;
    // for recaptcha v3 localhost
    // site key 6Le88r0ZAAAAAED8Ewl6TdLtAvjOOI4ipkPnU5-P
    // secret key 6Le88r0ZAAAAAFaha_ygpnM0fa-3YrgwE0GJgF1v
    this.reCaptchaV3Service.execute(this.recaptchaKey, 'homepage', (token) => {
      console.log('This is your token: ', token);
    }, {
      useGlobalDomain: false
    });
  }

  submit() {
    // call bachEnd to get a score
  }
}
