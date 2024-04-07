package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
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
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springfox.documentation.builders.RequestHandlerSelectors.any;

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
    @Test
    void createSession(){
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest=SessionProgrammationStepCreationRequest
                .builder()
                .description("description de test")
                .code("test")
                .build();
        Set<SessionProgrammationStepCreationRequest> setSteps=new HashSet<>();
        setSteps.add(sessionProgrammationStepCreationRequest);
        SessionProgrammationCreationRequest sessionProgrammationCreationRequest=SessionProgrammationCreationRequest
                .builder()
                .label("test")
                .steps(setSteps)
                .build();
        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .name("test")
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();
        EcosSessionEntity ecosSessionEntity= sessionMapper.toEntity(sessionCreationRequest);
        when(ecosSessionRepository.save(ecosSessionEntity)).thenReturn(ecosSessionEntity);
        when(ecosSessionProgrammationRepository.save(ecosSessionEntity.getEcosSessionProgrammationEntity())).thenReturn(ecosSessionEntity.getEcosSessionProgrammationEntity());
        when(ecosSessionProgrammationStepRepository.saveAll(anySet())).thenReturn(List.of());


        //when
        EcosSessionEntity response= sessionComponent.createSession(ecosSessionEntity);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(ecosSessionEntity);
        verify(ecosSessionRepository).save(ecosSessionEntity);
        verify(ecosSessionProgrammationRepository).save(ecosSessionEntity.getEcosSessionProgrammationEntity());
        verify(ecosSessionProgrammationStepRepository).saveAll(anySet());
    }
}
