# recaptcha v2 ("I'm not a robot" Checkbox)

In this project we are going to learn how to integrate reCaptcha to angular (v8) and how to validate at backend with Spring boot.

I. What is Recaptcha:

ReCAPTCHA is a free service that protects our web app from spam and abuse, and it is always useful in registration forms and others.

Recaptcha v2 ("I'm not a robot" Checkbox): The "I'm not a robot" Checkbox requires the user to click a checkbox indicating the user is not a robot. This will either pass the user immediately (with No CAPTCHA) or challenge them to validate whether or not they are human.

II. Install the reCaptcha library for our frontend:

To install the reCaptcha library using npm in CMD. inside our angular project folder, we should run the command:

 npm install ngx-captcha 

III. Generate Recaptcha SITE KEY and SECRET KEY for our app:

We should go to the google recaptcha site https://www.google.com/recaptcha/intro/v3.html to create a SITE KEY (for front end Angular) and a SECRET KEY (for backend verification with Spring boot).

When our app is on developpement phase, We can use those keys:

    Site key: 6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI
    Secret key: 6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe

IV. Add recaptcha balise in our html page:

RecaptchaKey is our generated site key.

The recaptcha api actually checks for site abuse, and it does that with validating a user by retrieving a token which is then used for further validation at the backend. this is called resolving.

<ngx-recaptcha2 #captchaElem (expire)="handleExpire()"
                  (success)="handleSuccess($event)"
                  [siteKey]="recaptchaKey"
                  formControlName="recaptcha">
 </ngx-recaptcha2>
 
 When the reCaptcha is checkbox is clicked and checked, the success function gets called and the response is a token. the response will lok like this:
 
03AsdsdfsSxBIiZC-cYcch6y-BaWrmfB9iOvvU3UiT9J4zF-WxQ9p74dmIFUJOoZ6dg6U-7vW1H_Ds29hI5oRoY10_Yr_FaVe6mW52uaxIPgkIcZv13Mx3ssTX5Hg6leBC3ihfuKUSsg3lDfxbaTmONfshidbHs_yMRtiPYnv79ZWm75cpwXDcpY1RaI5SWZMf5yXnCkmGDwmV9Mo2yqnYuwA70g8Ouf8cdzfsdfsdfcWIVFJSYS6KN2GjL0TudbMxpOxdyEHEVb5KKkTjVe8rWYHwkg9755rgaRi5csTVRATD4zH36JmMAXTuXBNS_LaQHsfDjI7m-eeVRYUr72wXs6g9vvGgkBHmw0dGeK6kOreDwAug2baHZXPH8pD5Gu6-hIz7mESBO-qn0XihfYQhBMMdIwTzhvkyerXfvTBCctFGvcTb777qOW16ZWwVGzGXpMWjEm_DdHSnjwjROwZdog8jsdbfUghaihsgiuWn9VZAb2-NnVkV6c61NUlR-C6m12_3UGHZt4uVtxeTMjhqHdix-SQTh_lQ

So, we have to validate the token received at the backend with our recaptcha site secret key.

V. Validating token in our backend:

to validate the token angular needs to send the token to the spring boot backend. to do that use the Angular HttpClient API. 

1. store the keys in the application.yml:

We store the keys in the application.yml:

google:
  recaptcha:
    key: 6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI
    secret-key: 6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe
    
    
And expose them to Spring using the RecaptchaConstants bean annotated with @ConfigurationProperties.

2. Server-Side Validation:

A server-side request is made to validate the captcha response with the web-service API.

The endpoint accepts an HTTP request on the URL https://www.google.com/recaptcha/api/siteverify, with the query parameters secret, response, and remoteip. It returns a json response having the schema:

{
    "success": true|false,
    "challenge_ts": timestamp,
    "hostname": string,
    "error-codes": [ ... ]
}

3. Attempts Cache:

It is important to understand that by integrating reCAPTCHA, every request made will cause the server to create a socket to validate the request.We can implement an elementary cache that restricts a client to a number of failed captcha responses that we fix in our application.yml.

We should add dependancy guava:

             <dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>26.0-jre</version>
		</dependency>

The cache is incorporated first by aborting if the client has exceeded the attempt limit. Otherwise when processing an unsuccessful GoogleResponse we record the attempts containing an error with the client's response. Successful validation clears the attempts cache.


 
 

