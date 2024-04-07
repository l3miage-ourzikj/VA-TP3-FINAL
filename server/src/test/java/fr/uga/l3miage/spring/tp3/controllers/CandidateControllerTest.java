package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.controller.CandidateController;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;
    @SpyBean
    private CandidateComponent candidateComponent;

    @Test
    void getCandidateAverage() throws Exception {
        ExamEntity exam1= ExamEntity.builder()
                .weight(2)
                .build();
        examRepository.save(exam1);
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Miel")
                .email("coincoin1@gmail.com")
                .build();
        candidateRepository.save(candidate1);
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
        candidateEvaluationGridRepository.save(grid1);
        candidateEvaluationGridRepository.save(grid2);
        candidateEvaluationGridRepository.save(grid3);
        Set<CandidateEvaluationGridEntity> mielGrid=new HashSet<>();
        mielGrid.add(grid1);
        mielGrid.add(grid2);
        mielGrid.add(grid3);
        exam1.setCandidateEvaluationGridEntities(mielGrid);
        examRepository.save(exam1);
        candidate1.setCandidateEvaluationGridEntities(mielGrid);
        candidateRepository.save(candidate1);
        final HttpHeaders headers = new HttpHeaders();
        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("candidateId", (long)2);
        ResponseEntity<Double> response = testRestTemplate.exchange("/api/candidates//{candidateId}/average", HttpMethod.GET,new HttpEntity<>(null,headers), Double.class,urlParams);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(12);
        cleanCandidate(candidate1);
        cleanCandidateEvaluationGrid(grid1);
        cleanCandidateEvaluationGrid(grid2);
        cleanCandidateEvaluationGrid(grid3);

        candidateEvaluationGridRepository.delete(grid1);
        candidateEvaluationGridRepository.delete(grid2);
        candidateEvaluationGridRepository.delete(grid3);
        candidateRepository.delete(candidate1);
        assertThat(candidateRepository.count()).isEqualTo(0);


    }
    void cleanCandidate(CandidateEntity candidate) {

        Set<CandidateEvaluationGridEntity> gridVide=new HashSet<>();
        candidate.setCandidateEvaluationGridEntities(gridVide);
        candidate.setTestCenterEntity(null);
    }
    void cleanCandidateEvaluationGrid(CandidateEvaluationGridEntity candidateEvaluationGrid) {
        candidateEvaluationGrid.setCandidateEntity(null);
    }
}
