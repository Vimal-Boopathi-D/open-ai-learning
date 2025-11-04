package com.example.OpenAiLearning.ExceptionHandler;

import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<String> handleNonTransientAiException(NonTransientAiException e) {

        if(e.getMessage().contains("Incorrect API key provided") || e.getMessage().contains("401")){
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Upgrade your plan");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
