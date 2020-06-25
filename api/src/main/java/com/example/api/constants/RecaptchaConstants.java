package com.example.api.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "recaptcha")
@Data
public class RecaptchaConstants {
    private String key;
    private String secretKey;
}
