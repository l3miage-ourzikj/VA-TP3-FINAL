package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.exceptions.SessionStatusNotUpadatedResponse;

import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionUpdatingStatusRestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SessionUpdatingStatusExceptionHandler {

    @ExceptionHandler(SessionUpdatingStatusRestException.class)
    public ResponseEntity<SessionStatusNotUpadatedResponse> handle(HttpServletRequest httpServletRequest, Exception exception){
        SessionUpdatingStatusRestException sessionUpdatingStatusRestException = (SessionUpdatingStatusRestException) exception;
        SessionStatusNotUpadatedResponse response = SessionStatusNotUpadatedResponse
                .builder()
                .errorMessage(sessionUpdatingStatusRestException.getMessage())
                .status(sessionUpdatingStatusRestException.getStatus())
                .uri(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(409).body(response);
    }
}
