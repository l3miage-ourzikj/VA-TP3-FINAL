package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
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
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionController {
@Autowired
private SessionService sessionService;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;
    @SpyBean
    private SessionComponent sessionComponent;

    @SpyBean
    private SessionMapper sessionMapper;

    //méthode pour supprimer les données en base qui ne sont pas supprimées entre deux tests. [car ici on ne s'isole pas de la BD comme on fait un test d'intégration ]
    @AfterEach
    public void clear() {
        ecosSessionProgrammationStepRepository.deleteAll();

    }

    @Test
    void endSession(){
        //given

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
