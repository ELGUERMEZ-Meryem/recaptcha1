package com.example.api.service;

import com.example.api.constants.RecaptchaV3Constants;
import com.example.api.exception.InvalidReCaptchaException;
import com.example.api.exception.ReCaptchaInvalidException;
import com.example.api.model.GoogleResponseV3;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.regex.Pattern;

@Service
public class RecaptchaV3Service implements IRecaptchaV3 {
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    private final RestOperations restTemplate;
    private final RecaptchaV3Constants recaptchaV3Constants;
    private final HttpServletRequest request;
    private final ReCaptchaAttemptService reCaptchaAttemptService;

    public RecaptchaV3Service(RecaptchaV3Constants recaptchaV3Constants, HttpServletRequest request, ReCaptchaAttemptService reCaptchaAttemptService) {
        this.recaptchaV3Constants = recaptchaV3Constants;
        this.request = request;
        this.reCaptchaAttemptService = reCaptchaAttemptService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void processResponse(String response, String action) throws ReCaptchaInvalidException, InvalidReCaptchaException {

        //check if the client has exceeded the attempt limit
        if (reCaptchaAttemptService.isBlocked(getClientIP())) {
            throw new InvalidReCaptchaException("Client exceeded maximum number of failed attempts");
        }

        //The captcha response obtained should be sanitized first. A simple regular expression is used.
        if (!responseSanityCheck(response)) {
            throw new InvalidReCaptchaException("Response contains invalid characters");
        }

        //Then we make a request to the web-service with the secret-key, the captcha response, and the client's IP address.
        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                recaptchaV3Constants.getSecretKey(), response, getClientIP()));

        //Call Recaptcha REST service, The response is of Type GoogleResponseV3
        //getForObject(url, classType): retrieve a representation by doing a GET on the URL. The response (if any) is unmarshalled to given class type and returned.
        GoogleResponseV3 googleResponse = restTemplate.getForObject(verifyUri, GoogleResponseV3.class);

        if (!googleResponse.isSuccess() || !googleResponse.getAction().equals(action)
                || googleResponse.getScore() < recaptchaV3Constants.getThreshold()) {
            reCaptchaAttemptService.reCaptchaFailed(getClientIP());
            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
        }
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
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
