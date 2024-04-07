package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.WrongCurrentStatusForUpdateException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;
    @MockBean
    private EcosSessionRepository ecosSessionRepository;
    @MockBean
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;
    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;
    @SpyBean
    private SessionMapper sessionMapper;

    @MockBean
    private  EcosSessionEntity sessionEntity;

    @Test
    void createSession(){
        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .name("test")
                .build();
    }

    @Test
    void getSessionByIdFound(){
        //given
        EcosSessionEntity session =EcosSessionEntity.builder()
                .name("Bora bora")
                .build();

        //when
        when(ecosSessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        //then
        assertDoesNotThrow(()->sessionComponent.getSessionById(1L));


    }

    @Test
    void getSessionByIdNotFound() {

        //when
        when(ecosSessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(SessionNotFoundException.class,()-> sessionComponent.getSessionById(1L));


    }

    @Test
    void isStatusEqualTo_EVAL_STARTED_True(){

        //given
        EcosSessionEntity session = EcosSessionEntity.builder()
                .name("Hola")
                .status(SessionStatus.EVAL_STARTED)
                .build();

        //when
        when(sessionEntity.getStatus()).thenReturn(SessionStatus.EVAL_STARTED);

        //then

        assertDoesNotThrow(()-> sessionComponent.isStatusEvalStarted(session));


    }


    @Test
    void isStatusEqualTo_EVAL_STARTED_false(){

        //given
        EcosSessionEntity session = EcosSessionEntity.builder()
                .name("Hola")
                .status(SessionStatus.CORRECTED)
                .build();

        //when
        when(sessionEntity.getStatus()).thenReturn(SessionStatus.CORRECTED);

        //then

        assertThrows(WrongCurrentStatusForUpdateException.class,()-> sessionComponent.isStatusEvalStarted(session));


    }
}


