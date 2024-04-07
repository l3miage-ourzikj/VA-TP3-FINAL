package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
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
    @Autowired
    private EcosSessionRepository ecosSessionRepository;
    @Autowired
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;
    @Autowired
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;
    @Test
    void createSession(){
        EcosSessionProgrammationStepEntity ecosSessionProgrammationStepEntity = EcosSessionProgrammationStepEntity
                .builder()
                .code("test")
                .description("test description")
                .build();
        Set<EcosSessionProgrammationStepEntity> steps = new HashSet<>();
        steps.add(ecosSessionProgrammationStepEntity);
        EcosSessionProgrammationEntity ecosSessionProgrammationEntity=EcosSessionProgrammationEntity
                .builder()
                .ecosSessionProgrammationStepEntities(steps)
                .build();

        ExamEntity examEntity=ExamEntity
                .builder()
                .name("test")
                .build();
        Set<ExamEntity> exams = new HashSet<>();
        exams.add(examEntity);
        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .name("test")
                .ecosSessionProgrammationEntity(ecosSessionProgrammationEntity)
                .examEntities(exams)
                .build();




        //when
        EcosSessionEntity response= sessionComponent.createSession(ecosSessionEntity);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(ecosSessionEntity);
        assertThat(ecosSessionRepository.count()).isEqualTo(1);
        assertThat(ecosSessionProgrammationRepository.count()).isEqualTo(1);
        assertThat(ecosSessionProgrammationStepRepository.count()).isEqualTo(1);
    }
}
