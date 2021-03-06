package com.example.api.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * We get recaptcha keys that we stored in the application.yml.
 */
@Configuration
@ConfigurationProperties(prefix = "google.recaptcha2")
@Data
public class RecaptchaConstants {
    private String siteKey;
    private String secretKey;
    private int maxAttempt;
    private int duration;
}
