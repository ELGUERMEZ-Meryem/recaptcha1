import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ReCaptcha2Component} from "ngx-captcha";
import {environment} from "../environments/environment";
import {AppService} from "./app.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  recaptchaForm: FormGroup;
  @ViewChild('captchaElem') captchaElem: ReCaptcha2Component;
  recaptchaKey: string;
  error: string;
  formSubmitted: boolean = false;

  constructor(private formBuilder: FormBuilder, private appService: AppService) {
  }

  ngOnInit() {
    this.recaptchaKey = environment.recaptchaKey;
    //I am using
    // Site key: 6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI
    // Secret key: 6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe
    this.recaptchaForm = this.formBuilder.group({
      recaptcha: ['', Validators.required]
    });
  }

  handleReset() {
    // To reset Google recaptcha
    console.log('reset');
  }

  handleExpire() {
    console.log('expire');
    // For example  if the user solves the Captcha and kept the page idle for 2 minutes. Now I'm getting error message on Captcha widget "Verification expired. Check the checkbox again" When we get this error message, the handleExpire() method is getting invoked.
  }

  handleSuccess($event: any) {
    console.log('success')
  }

  handleLoad() {
    console.log('load');
  }

  onSubmit() {
    this.formSubmitted = true;
    if (!this.recaptchaForm.controls.recaptcha.value) {
      return;
    }
    this.appService.checkRecaptcha(this.recaptchaForm.controls.recaptcha.value).subscribe(data => {
      this.captchaElem.resetCaptcha();
    }, err => {
      this.error = err.message;
    });
  }

  isControlHasError(controlName: string, validationType: string): boolean {
    const control = this.recaptchaForm.controls[controlName];
    if (!control) {
      return false;
    }

    const result = control.hasError(validationType) && (control.dirty || control.touched || this.formSubmitted);
    return result;
  }
}
