package fr.uga.l3miage.spring.tp3.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionNotFoundResponse {
    private Long sessionId;
    private String errorMessage;
    private String uri;
}
