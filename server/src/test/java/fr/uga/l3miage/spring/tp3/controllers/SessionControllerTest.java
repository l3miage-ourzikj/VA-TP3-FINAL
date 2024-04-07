package fr.uga.l3miage.spring.tp3.controllers;
import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EcosSessionRepository ecosSessionRepository;

    @SpyBean
    private SessionComponent sessionComponent;

    @SpyBean
    private ExamComponent examComponent;

    @AfterEach
    public void clear() {
        ecosSessionRepository.deleteAll();
    }
    @Test
    void cantCreateSessionWithoutExam(){
        final HttpHeaders headers = new HttpHeaders();
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest=SessionProgrammationStepCreationRequest
                .builder()
                .code("test")
                .description("description test")
                .build();

        Set<SessionProgrammationStepCreationRequest> steps=new HashSet<>();
        steps.add(sessionProgrammationStepCreationRequest);
        SessionProgrammationCreationRequest sessionProgrammationCreationRequest=SessionProgrammationCreationRequest
                .builder()
                .label("test")
                .steps(steps)
                .build();
      final  SessionCreationRequest sessionCreationRequest=SessionCreationRequest
                .builder()
                .name("test")
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();
        // when
        ResponseEntity<SessionResponse> response = testRestTemplate
                .exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessionCreationRequest, headers), SessionResponse.class);


        //then
        assertThat(response.getStatusCode()).isEqualTo(400);

    }

}
