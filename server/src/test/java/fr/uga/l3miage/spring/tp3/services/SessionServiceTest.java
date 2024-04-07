package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.EcosSessionProgrammationResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.Mockito.*;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {
    @Autowired
    private  SessionService sessionService;
    @MockBean
    private SessionComponent sessionComponent;
    @MockBean
    private ExamComponent examComponent;
    @SpyBean
    private SessionMapper sessionMapper;

    @Test
    void createSession() throws CreationSessionRestException, ExamNotFoundException {
        ExamEntity examEntity = ExamEntity
                .builder()
                .id((long)1)
                .name("test")
                .build();
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
        SessionCreationRequest sessionCreationRequest=SessionCreationRequest
                .builder()
                .name("test")
                .examsId(Set.of((long)1))
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();
        EcosSessionEntity sessionEntity=sessionMapper.toEntity(sessionCreationRequest);
        when(sessionComponent.createSession(any())).thenReturn(sessionEntity);
        Set<ExamEntity> examEntities=new HashSet<>();
        examEntities.add(examEntity);
        when(examComponent.getAllById(any())).thenReturn(examEntities);
        SessionResponse responseExpected= sessionMapper.toResponse(sessionEntity);
        //when
        SessionResponse response=sessionService.createSession(sessionCreationRequest);
        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
        verify(sessionMapper,times(2)).toEntity(sessionCreationRequest);
        verify(sessionMapper,times(2)).toResponse(same(sessionEntity));
        verify(sessionComponent,times(1)).createSession(any(EcosSessionEntity.class));

    }

}
