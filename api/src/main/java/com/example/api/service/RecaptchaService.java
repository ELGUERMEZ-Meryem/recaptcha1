package com.example.api.service;

import com.example.api.constants.RecaptchaConstants;
import com.example.api.exception.InvalidReCaptchaException;
import com.example.api.exception.ReCaptchaInvalidException;
import com.example.api.model.GoogleResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.regex.Pattern;

@Service
public class RecaptchaService implements IRecaptcha {

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    private final RecaptchaConstants recaptchaConstants;
    private final ReCaptchaAttemptService reCaptchaAttemptService;
    private final RestOperations restTemplate;
    private final HttpServletRequest request;

    public RecaptchaService(RecaptchaConstants recaptchaConstants, ReCaptchaAttemptService reCaptchaAttemptService, HttpServletRequest request) {
        this.recaptchaConstants = recaptchaConstants;
        this.reCaptchaAttemptService = reCaptchaAttemptService;
        this.request = request;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void processResponse(String response) throws ReCaptchaInvalidException, InvalidReCaptchaException {

        //check if the client has exceeded the attempt limit
        if(reCaptchaAttemptService.isBlocked(getClientIP())) {
            throw new InvalidReCaptchaException("Client exceeded maximum number of failed attempts");
        }

        //The captcha response obtained should be sanitized first. A simple regular expression is used.
        if (!responseSanityCheck(response)) {
            throw new InvalidReCaptchaException("Response contains invalid characters");
        }

        //Then we make a request to the web-service with the secret-key, the captcha response, and the client's IP address.
        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                recaptchaConstants.getSecretKey(), response, getClientIP()));

        //Call Recaptcha REST service, The response is of Type GoogleResponse
        //getForObject(url, classType): retrieve a representation by doing a GET on the URL. The response (if any) is unmarshalled to given class type and returned.
        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

        if (!googleResponse.isSuccess()) {
            //When processing an unsuccessful GoogleResponse we record the attempts containing an error with the client's response
            if(googleResponse.hasClientError()) {
                reCaptchaAttemptService.reCaptchaFailed(getClientIP());
            }
            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
        }

        //Successful validation clears the attempts cache
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
        //A truth value in the success property means the user has been validated. Otherwise the errorCodes property will populate with the reason.
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    private String getClientIP() {
        //We can get the client IP address via the HTTP request header X-Forwarded-For (XFF).
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (StringUtils.isEmpty(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
