package fr.uga.l3miage.spring.tp3.exceptions;

import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionStatusNotUpadatedResponse {
    private Long sessionId;
    private SessionStatus status;
    private String errorMessage;
    private String uri;
}
