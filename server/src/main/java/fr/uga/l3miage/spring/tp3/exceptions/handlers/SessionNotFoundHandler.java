package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.SessionNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SessionNotFoundHandler {

    @ExceptionHandler(SessionNotFoundRestException.class)
    public ResponseEntity<SessionNotFoundResponse> handle(HttpServletRequest httpServletRequest, Exception exception){
        SessionNotFoundRestException sessionNotFoundRestException = (SessionNotFoundRestException) exception;
        SessionNotFoundResponse response = SessionNotFoundResponse
                .builder()
                .sessionId(sessionNotFoundRestException.getSessionId())
                .errorMessage(sessionNotFoundRestException.getMessage())
                .uri(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(404).body(response);
    }
}
