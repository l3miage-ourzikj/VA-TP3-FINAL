package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.EcosSessionProgrammationStepComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.StepNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.LastStepDateTimeNotYetPassedException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.StepNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.WrongCurrentStatusForUpdateException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionComponent sessionComponent;

    @MockBean
    private EcosSessionProgrammationStepComponent ecosSessionProgrammationStepComponent;

    @SpyBean
    private SessionMapper sessionMapper;

    @SpyBean
    private EcosSessionEntity sessionEntity;

    @Test
    void endSession_404_SessionNotFound() throws SessionNotFoundException {

        //when
        when(sessionComponent.getSessionById(anyLong())).thenThrow(new SessionNotFoundException("Session not found!",1L));

        //then
        assertThrows(SessionNotFoundRestException.class,()-> sessionService.endSession(1L));

    }

    @Test
    void endSession_404_StepNotFound() throws SessionNotFoundException, StepNotFoundException {

        EcosSessionEntity requestedSession = EcosSessionEntity.builder()
                .build();

        //when
        when(sessionComponent.getSessionById(anyLong())).thenReturn(requestedSession);
        when(ecosSessionProgrammationStepComponent.getLastStep(requestedSession.getId())).thenThrow(new StepNotFoundException("Last Step not found"));

        //then
        assertThrows(StepNotFoundRestException.class,()-> sessionService.endSession(requestedSession.getId()));

    }

    @Test
    void endSession() throws SessionNotFoundException, StepNotFoundException, LastStepDateTimeNotYetPassedException, WrongCurrentStatusForUpdateException {

        EcosSessionEntity requestedSession = EcosSessionEntity.builder()
                .examEntities(Set.of())
                .build();

        EcosSessionProgrammationStepEntity lastStep= EcosSessionProgrammationStepEntity.builder()
                .code("A2E")
                .dateTime(LocalDateTime.now().minusMinutes(2))
                .build();

        ExamEntity exam1= ExamEntity.builder().candidateEvaluationGridEntities(Set.of()).build();
        requestedSession.getExamEntities().add(exam1);

        CandidateEvaluationGridEntity sheet1=CandidateEvaluationGridEntity.builder().grade(15.5).build();
        CandidateEvaluationGridEntity sheet2=CandidateEvaluationGridEntity.builder().grade(13.6).build();

        exam1.getCandidateEvaluationGridEntities().add(sheet1);
        exam1.getCandidateEvaluationGridEntities().add(sheet2);

        sessionComponent.createSession(requestedSession);



        //when
        when(sessionComponent.getSessionById(anyLong())).thenReturn(requestedSession);
        when(ecosSessionProgrammationStepComponent.getLastStep(requestedSession.getId() ) ).thenReturn(lastStep);
        when(ecosSessionProgrammationStepComponent.IsYetPassed(lastStep,requestedSession)).thenReturn();
        when(sessionComponent.isStatusEvalStarted(requestedSession)).thenReturn();


        Set<CandidateEvaluationGridResponse> expectedResponse= new HashSet<>(sessionMapper.toResponse(sheet1),sessionMapper.toResponse(sheet2));
        Set<CandidateEvaluationGridResponse> response = sessionService.endSession(requestedSession.getId());
        //then
        assertDoesNotThrow(()-> sessionService.endSession(requestedSession.getId()));
        assertThat(response).isEqualTo(expectedResponse);
        verify(sessionEntity,times(1)).setStatus(any(SessionStatus.class));
        verify(sessionMapper,times(4)).toResponse(CandidateEvaluationGridEntity);




    }


}
