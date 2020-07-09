package com.example.api.service;

import com.example.api.constants.RecaptchaConstants;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//By integrating reCAPTCHA, every request made will cause the server to create a socket to validate the request.
//While we'd need a more layered approach for a true DoS mitigation,
//we can implement an elementary cache that restricts a client to MAX_ATTEMPT(we specified it in application.yml) failed captcha responses:
@Service
public class ReCaptchaAttemptService {
    private final RecaptchaConstants recaptchaConstants;
    private LoadingCache<String, Integer> attemptsCache;

    public ReCaptchaAttemptService(RecaptchaConstants recaptchaConstants) {
        super();
        this.recaptchaConstants = recaptchaConstants;
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(recaptchaConstants.getDuration(), TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void reCaptchaSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public void reCaptchaFailed(String key) {
        int attempts = attemptsCache.getUnchecked(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        return attemptsCache.getUnchecked(key) >= recaptchaConstants.getMaxAttempt();
    }
}
