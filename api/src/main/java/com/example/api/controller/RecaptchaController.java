package com.example.api.controller;

import com.example.api.exception.InvalidReCaptchaException;
import com.example.api.exception.ReCaptchaInvalidException;
import com.example.api.service.RecaptchaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/recaptcha")
public class RecaptchaController {
    private final RecaptchaService recaptchaService;

    public RecaptchaController(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @PostMapping
    public ResponseEntity<?> verifyRecaptcha(@RequestBody String recaptcha) {
        try {
            recaptchaService.processResponse(recaptcha);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (ReCaptchaInvalidException | InvalidReCaptchaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
