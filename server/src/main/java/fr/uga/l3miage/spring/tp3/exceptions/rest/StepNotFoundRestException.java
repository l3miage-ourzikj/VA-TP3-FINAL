package fr.uga.l3miage.spring.tp3.exceptions.rest;

import lombok.Getter;


public class StepNotFoundRestException extends RuntimeException{


    public StepNotFoundRestException(String message) {
        super(message);

    }
}
