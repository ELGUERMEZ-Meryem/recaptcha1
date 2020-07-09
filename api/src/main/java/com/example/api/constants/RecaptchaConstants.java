package com.example.api.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * We get recaptcha keys that we stored in the application.yml.
 */
@Configuration
@ConfigurationProperties(prefix = "google.recaptcha")
@Data
public class RecaptchaConstants {
    private String key;
    private String secretKey;
}
