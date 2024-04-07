package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {
@Autowired
    private CandidateService candidateService;
@MockBean
    private CandidateComponent candidateComponent;
@Test
    void getCandidateAverage() throws CandidateNotFoundException {
        //given
    ExamEntity exam1= ExamEntity.builder()
            .weight(2)
            .build();
    CandidateEntity candidate1= CandidateEntity
            .builder()
            .lastname("Miel")
            .email("coincoin1@gmail.com")
            .build();
    CandidateEvaluationGridEntity grid1=CandidateEvaluationGridEntity
            .builder()
            .grade(10.0)
            .examEntity(exam1)
            .candidateEntity(candidate1)
            .build();
    CandidateEvaluationGridEntity grid2=CandidateEvaluationGridEntity
            .builder()
            .grade(17.0)
            .examEntity(exam1)
            .candidateEntity(candidate1)
            .build();
    CandidateEvaluationGridEntity grid3=CandidateEvaluationGridEntity
            .builder()
            .grade(9.0)
            .examEntity(exam1)
            .candidateEntity(candidate1)
            .build();
    Set<CandidateEvaluationGridEntity> mielGrid=new HashSet<>();
    mielGrid.add(grid1);
    mielGrid.add(grid2);
    mielGrid.add(grid3);
    exam1.setCandidateEvaluationGridEntities(mielGrid);
    candidate1.setCandidateEvaluationGridEntities(mielGrid);
        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidate1);
    // when
        double response= candidateService.getCandidateAverage((long)888);
    //then
        assertThat(response).isEqualTo(12.0);
}







}
