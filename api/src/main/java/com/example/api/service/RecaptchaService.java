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
    private final RestOperations restTemplate;
    private final HttpServletRequest request;

    public RecaptchaService(RecaptchaConstants recaptchaConstants, HttpServletRequest request) {
        this.recaptchaConstants = recaptchaConstants;
        this.request = request;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void processResponse(String response) throws ReCaptchaInvalidException, InvalidReCaptchaException {
        if (!responseSanityCheck(response)) {
            throw new InvalidReCaptchaException("Response contains invalid characters");
        }

        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                recaptchaConstants.getSecretKey(), response, getClientIP()));

        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

        if (!googleResponse.isSuccess()) {
            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
        }
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response);
    }

    private String getClientIP() {
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
