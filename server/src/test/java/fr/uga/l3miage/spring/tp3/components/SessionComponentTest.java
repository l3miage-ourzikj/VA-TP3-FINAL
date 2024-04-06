package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

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
        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .name("test")
                .build();
    }
}
