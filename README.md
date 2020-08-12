# recaptcha v2 ("I'm not a robot" Checkbox):

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

google.recaptcha.key: 6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI
google.recaptcha.secret-key: 6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe
    
    
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

4. We should add dependancy google guava cache:

Cache is a component that is used to speed up data retrieval in general. Google’s guava library provides cache mechanism. guava cache can be used when fast access needed and when values retrieved multiple times. Guava Library cache interface allows standard caching operations like get, put and invalidate. Get operation returns the value associated by the key, put operation stores value associated by the key and invalidate operation discards the value associated with the key.

            <dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>26.0-jre</version>
		</dependency>

The cache is incorporated first by aborting if the client has exceeded the attempt limit. Otherwise when processing an unsuccessful GoogleResponse we record the attempts containing an error with the client's response. Successful validation clears the attempts cache.

# recaptcha v3:

No more clicking on a box or identifying cars and other traffic lights on a series of images, with version 3 of reCAPTCHA you won't have to do anything to authenticate yourself. The system will now analyze the activity on the website and provide a score between 0.0 and 1.0, which will assess how suspicious the interactions between the site and the user are. If the score is close to 1.0, the interactions will be considered normal, while if they are close to 0.0, our API will consider that the traffic was probably generated by a robot. Our site administrators can then use the score obtained in three different ways:
- By setting a threshold that determines when a user is let through, or when additional verification should be performed (for example, using two-factor -authentication).
- By combining the score obtained with signals that reCAPTCHA cannot access to enhance security (such as user profiles or transaction histories).
- By using the reCAPTCHA score as one of the signals to train a machine learning model to combat abuse.

The same, to integrate Google's reCAPTCHA 3, we first need to register our site with the service, add their library to our page, and then verify the token response with the web service.

So, let's register our site at https://www.google.com/recaptcha/admin/create and, after selecting reCAPTCHA v3, we'll obtain the new secret and site keys.

I. FrontEnd integration:

Inside our registration form, we add a hidden field that will store the response token received from the call to the reCaptchaV3Service.execute function:
First you need to inject ReCaptchaV3Service from ngx-captcha and then use Execute method. 

	   this.reCaptchaV3Service.execute(this.recaptchaKey, 'submit', (recaptchaResponse) => {
	      //recaptcha v3 response
	      console.log('recaptcha v3 response', recaptchaResponse);
	    }, {
	      useGlobalDomain: false
	    });
    
    
 The seconde argument is the action. Action is a new concept that Google introduced so that we can execute many reCAPTCHA requests on the same web page.

II. Validating token in our backend:

We need to validate recaptchaResponse in backend using recaptcha web Service API.

1. store the keys in the application.yml:

We store the keys in the application.yml:

	google.recaptchaV3.key: 6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI
	google.recaptchaV3.secret-key: 6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe
	google.recaptchaV3.threshold: 0.5

And expose them to Spring using the RecaptchaV3Constants bean annotated with @ConfigurationProperties.
the threshold set to 0.5 is a default value and can be tuned over time by analyzing the real threshold values in the Google admin console.

2. validate recaptcha response that we got from our frontEnd using recaptcha web Service:

The endpoint accepts an HTTP request on the URL https://www.google.com/recaptcha/api/siteverify, with the query parameters secret, response, and remoteip. It returns a json response having the schema:

	{
	    "success": true|false,
	    "challenge_ts": timestamp,
	    "hostname": string,
	    "error-codes": [ ... ],
	    "score": number,
            "action": string
	}

The score is based on the user's interactions and is a value between 0 (very likely a bot) and 1.0 (very likely a human).

An action must be specified every time we execute the reCAPTCHA v3. And, we have to verify that the value of the action property in the response corresponds to the expected name.

3. We can use Attempts Cache:

It is important to understand that by integrating reCAPTCHA, every request made will cause the server to create a socket to validate the request.We can implement an elementary cache that restricts a client to a number of failed captcha responses that we fix in our application.yml.

# Resources:

https://www.baeldung.com/spring-security-registration-captcha

https://medium.com/@samuelhenshaw2020/recaptcha-v2-in-angular-8-with-back-end-verification-with-nodejs-9574f297fdef

https://mkyong.com/java/how-to-get-client-ip-address-in-java/

https://www.yusufaytas.com/caching-guava/



 
 

