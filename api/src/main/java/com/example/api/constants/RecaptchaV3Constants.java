package com.example.api.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.recaptcha3")
@Data
public class RecaptchaV3Constants {
    private String siteKey;
    private String secretKey;
    private float threshold;
}
