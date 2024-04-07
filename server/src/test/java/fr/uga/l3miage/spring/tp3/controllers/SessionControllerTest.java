package fr.uga.l3miage.spring.tp3.controllers;
import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.services.SessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EcosSessionRepository ecosSessionRepository;

    @SpyBean
    private SessionComponent sessionComponent;

    @SpyBean
    private ExamComponent examComponent;

    @Autowired
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;


    @SpyBean
    private SessionMapper sessionMapper;

    //méthode pour supprimer les données en base qui ne sont pas supprimées entre deux tests. [car ici on ne s'isole pas de la BD comme on fait un test d'intégration ]

    @AfterEach
    public void clear() {
        ecosSessionRepository.deleteAll();
        ecosSessionProgrammationStepRepository.deleteAll();

    }
    @Test
    // Détection d'une faille on peut créer une session avec un set d'examen vide
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
    @Test
    void cantCreateSessionWithoutGoodExamID(){
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
                .examsId(Set.of((long)1))
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();
        // when
        ResponseEntity<String> response = testRestTemplate
                .exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessionCreationRequest, headers), String.class);


        //then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);


    }

    @Test
    void endSession(){
        //given.

        EcosSessionEntity requestedSession = EcosSessionEntity.builder()
                .examEntities(Set.of())
                .status(SessionStatus.EVAL_STARTED)
                .build();

        EcosSessionProgrammationStepEntity lastStep= EcosSessionProgrammationStepEntity.builder()
                .code("A2E")
                .dateTime(LocalDateTime.now().minusMinutes(2))
                .build();

        ExamEntity exam1= ExamEntity.builder().candidateEvaluationGridEntities(Set.of()).build();


        CandidateEvaluationGridEntity sheet1=CandidateEvaluationGridEntity.builder().grade(15.5).build();
        CandidateEvaluationGridEntity sheet2=CandidateEvaluationGridEntity.builder().grade(13.6).build();

        exam1.getCandidateEvaluationGridEntities().add(sheet1);
        exam1.getCandidateEvaluationGridEntities().add(sheet2);

        requestedSession.getExamEntities().add(exam1);
        sessionComponent.createSession(requestedSession);

        final HttpHeaders headers = new HttpHeaders();

        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("sessionId", 1);
        ;
        //when
        Set<CandidateEvaluationGridResponse> expectedResponse= sessionService.endSession(requestedSession.getId());
        ResponseEntity<   Set<CandidateEvaluationGridResponse> > response= testRestTemplate.exchange("/api/sessions/{sessionId}/end", HttpMethod.PATCH, new HttpEntity<>(null, headers), Set.of(CandidateEvaluationGridResponse.class), urlParams);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(sessionService, times(1)).endSession(ArgumentMatchers.anyLong());
    }

}
