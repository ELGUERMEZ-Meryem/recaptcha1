# recaptcha v2 ("I'm not a robot" Checkbox)

In this project we are going to learn how to integrate reCaptcha to angular (v8) and how to validate at backend with Spring boot.

I. What is Recaptcha:

ReCAPTCHA is a free service that protects your website from spam and abuse, and it is always useful in registration forms and others.

recaptcha v2 ("I'm not a robot" Checkbox): The "I'm not a robot" Checkbox requires the user to click a checkbox indicating the user is not a robot. This will either pass the user immediately (with No CAPTCHA) or challenge them to validate whether or not they are human.

II. Install the reCaptcha library for our frontend:

To install the reCaptcha library using npm in CMD. inside our angular project folder, we should run the command:

npm install ngx-captcha 

III. Generate Recaptcha SITE KEY and SECRET KEY for our site:

We should go to the google recaptcha site https://www.google.com/recaptcha/intro/v3.html to create a SITE KEY (for front end Angular) and a SECRET KEY (for backend verification with Spring boot).
