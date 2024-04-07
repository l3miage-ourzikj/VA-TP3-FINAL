package fr.uga.l3miage.spring.tp3.exceptions.technical;

import lombok.Getter;


public class StepNotFoundException extends Exception{
    public StepNotFoundException(String message){
        super(message);
    }
}
