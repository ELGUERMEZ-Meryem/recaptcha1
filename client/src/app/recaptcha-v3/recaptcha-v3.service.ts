import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable()
export class RecaptchaV3Service {
  private API_RECAPTCHA_V3_URL = '/recaptcha/recaptchaV3';

  constructor(private http: HttpClient) {
  }

  checkRecaptchaV3(recaptchaResponse: string) {
    return this.http.post<any>(environment.apiURL + this.API_RECAPTCHA_V3_URL, recaptchaResponse);
  }

}
