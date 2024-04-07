package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.LastStepDateTimeNotYetPassedException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.StepNotFoundException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import nonapi.io.github.classgraph.utils.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ProgrammationStepComponentTest {

    @Autowired
    private EcosSessionProgrammationStepComponent ecosSessionProgrammationStepComponent;

    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;
    @MockBean
    private  EcosSessionProgrammationStepEntity ecosSessionProgrammationStepEntity;


    @Test
    void getLastStepPresent(){
        EcosSessionProgrammationStepEntity step =EcosSessionProgrammationStepEntity.builder().build();
        //when
        when(ecosSessionProgrammationStepRepository.findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(anyLong())).thenReturn(Optional.of(step));

        Optional<EcosSessionProgrammationStepEntity> expectedResponse =ecosSessionProgrammationStepRepository.findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(anyLong());
        //then

        assertThat(expectedResponse).isNotEmpty();
    }
    @Test
    void getLastStepNotPresent(){
        //when
        when(ecosSessionProgrammationStepRepository.findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(anyLong())).thenReturn(Optional.empty());
        //then

        assertThrows(StepNotFoundException.class,()->ecosSessionProgrammationStepComponent.getLastStep(anyLong()));
    }

    @Test
    void isYetPassedTrue(){

        //given
        EcosSessionProgrammationStepEntity step =EcosSessionProgrammationStepEntity.builder()
                .description("Swim & co ")
                .dateTime(LocalDateTime.now())
                .build();

        EcosSessionEntity session =EcosSessionEntity.builder().build();

        //when
        when(ecosSessionProgrammationStepEntity.getDateTime()).thenReturn(step.getDateTime().minusMinutes(3));

        //then

        assertDoesNotThrow(()-> ecosSessionProgrammationStepComponent.IsYetPassed(step,session));




    }
    @Test
    void isYetPassedFalse(){

        //given
        EcosSessionProgrammationStepEntity step =EcosSessionProgrammationStepEntity.builder()
                .description("Swim & co ")
                .dateTime(LocalDateTime.now().plusMinutes(3))
                .build();

        EcosSessionEntity session =EcosSessionEntity.builder().build();

        //when
        when(ecosSessionProgrammationStepEntity.getDateTime()).thenReturn(step.getDateTime());

        //then

        assertThrows(LastStepDateTimeNotYetPassedException.class,() -> ecosSessionProgrammationStepComponent.IsYetPassed(step,session));




    }
}

