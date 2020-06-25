import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

@Injectable()
export class AppService {

  private API_RECAPTCHA_URL = '/recaptcha';

  constructor(private http: HttpClient) {
  }

  checkRecaptcha(recaptcha: string) {
    return this.http.post<any>(environment.apiURL + this.API_RECAPTCHA_URL, recaptcha);
  }
}
