package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.exceptions.SessionNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.StepNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.StepNotFoundRestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class StepNotFoundHandler {

    @ExceptionHandler(SessionNotFoundRestException.class)
    public ResponseEntity<StepNotFoundResponse> handle(HttpServletRequest httpServletRequest, Exception exception){
        StepNotFoundRestException stepNotFoundRestException = (StepNotFoundRestException) exception;
        StepNotFoundResponse response = StepNotFoundResponse
                .builder()
                .errorMessage(stepNotFoundRestException.getMessage())
                .uri(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(404).body(response);
    }
}
