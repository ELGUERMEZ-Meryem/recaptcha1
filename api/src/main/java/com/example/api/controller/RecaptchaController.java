package com.example.api.controller;

import com.example.api.exception.InvalidReCaptchaException;
import com.example.api.exception.ReCaptchaInvalidException;
import com.example.api.service.RecaptchaService;
import com.example.api.service.RecaptchaV3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/recaptcha")
public class RecaptchaController {
    private final RecaptchaService recaptchaService;
    private final RecaptchaV3Service recaptchaV3Service;

    public RecaptchaController(RecaptchaService recaptchaService, RecaptchaV3Service recaptchaV3Service) {
        this.recaptchaService = recaptchaService;
        this.recaptchaV3Service = recaptchaV3Service;
    }

    @PostMapping
    public ResponseEntity<?> verifyRecaptchaV2(@RequestBody String recaptcha) {
        try {
            // Validate captchaV2 response that we get from http request with our RecaptchaService.
            recaptchaService.processResponse(recaptcha);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (ReCaptchaInvalidException | InvalidReCaptchaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PostMapping(value = "/recaptchaV3")
    public ResponseEntity<?> verifyRecaptchaV3(@RequestBody String recaptchaResponse) {
        // Validate captchaV3 response that we get from http request with our RecaptchaService.
        try {
            recaptchaV3Service.processResponse(recaptchaResponse, "homepage");
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (ReCaptchaInvalidException | InvalidReCaptchaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
