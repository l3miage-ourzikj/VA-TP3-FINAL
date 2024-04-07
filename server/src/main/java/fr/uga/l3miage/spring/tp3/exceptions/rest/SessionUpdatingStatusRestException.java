package fr.uga.l3miage.spring.tp3.exceptions.rest;

import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import lombok.Getter;

@Getter
public class SessionUpdatingStatusRestException  extends RuntimeException{
    private  final SessionStatus status;

    public SessionUpdatingStatusRestException(String message,SessionStatus status){
        super(message);
        this.status=status;

    }
}
