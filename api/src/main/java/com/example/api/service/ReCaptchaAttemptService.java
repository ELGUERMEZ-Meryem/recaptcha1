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
    private final LoadingCache<String, Integer> attemptsCache;

    //Guava Library cache interface allows standard caching operations like get, put and invalidate.
    //Get operation returns the value associated by the key.
    //Put operation stores value associated by the key.
    //Invalidate operation discards the value associated with the key.
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
        //When successful validation, we clear the attempts cache
        attemptsCache.invalidate(key);
    }

    public void reCaptchaFailed(String key) {
        //When unsuccessful GoogleResponse, we add 1 in our client's attemptsCache
        int attempts = attemptsCache.getUnchecked(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        //check if the client has exceeded the attempt limit
        return attemptsCache.getUnchecked(key) >= recaptchaConstants.getMaxAttempt();
    }
}
