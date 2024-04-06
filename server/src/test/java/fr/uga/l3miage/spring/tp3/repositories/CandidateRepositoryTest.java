package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"
})
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private TestCenterRepository testCenterRepository;
    @Autowired
    private  CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    void cleanCandidate(CandidateEntity candidate) {

        Set<CandidateEvaluationGridEntity> gridVide=new HashSet<>();
        candidate.setCandidateEvaluationGridEntities(gridVide);
        candidate.setTestCenterEntity(null);
    }
    void cleanCandidateEvaluationGrid(CandidateEvaluationGridEntity candidateEvaluationGrid) {
        candidateEvaluationGrid.setCandidateEntity(null);
    }
    @Test
    void findAllByTestCenterEntityCode(){
        //given
        TestCenterEntity testCenter1 = TestCenterEntity
                .builder()
                .code(TestCenterCode.GRE)
                .build();
        TestCenterEntity testCenter2 = TestCenterEntity
                .builder()
                .code(TestCenterCode.DIJ)
                .build();

        testCenterRepository.save(testCenter1);
        testCenterRepository.save(testCenter2);

        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Miel")
                .email("coincoin1@gmail.com")
                .testCenterEntity(testCenter1)
                .build();

        CandidateEntity candidate2= CandidateEntity
                .builder()
                .lastname("Ourzik")
                .email("coincoin2@gmail.com")
                .testCenterEntity(testCenter1)
                .build();
        CandidateEntity candidate3= CandidateEntity
                .builder()
                .lastname("Chirac")
                .email("coincoin3@gmail.com")
                .testCenterEntity(testCenter2)
                .build();

        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);


        Set<CandidateEntity>grenoble=new HashSet<>();
        grenoble.add(candidate1);
        grenoble.add(candidate2);
        Set<CandidateEntity>dijon=new HashSet<>();
        dijon.add(candidate3);

        testCenter1.setCandidateEntities(grenoble);
        testCenter2.setCandidateEntities(dijon);

        testCenterRepository.save(testCenter1);
        testCenterRepository.save(testCenter2);

        //when
        Set<CandidateEntity> response1=candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.GRE);

        Set<CandidateEntity> response2=candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.TOU);
        //then
        assertThat(response1).allMatch(candidateEntity -> TestCenterCode.GRE.equals(candidateEntity.getTestCenterEntity().getCode()));
        assertThat(response1.size()).isEqualTo(2);
        assertThat(response2).isEmpty();
        cleanCandidate(candidate1);
        cleanCandidate(candidate2);
        cleanCandidate(candidate3);

    }
    @Test
    void findAllByCandidateEvaluationGridEntitiesGradeLessThan(){
        //given
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Miel")
                .email("coincoin1@gmail.com")
                .build();
        CandidateEntity candidate2= CandidateEntity
                .builder()
                .lastname("Ourzik")
                .email("coincoin2@gmail.com")
                .build();
        CandidateEntity candidate3= CandidateEntity
                .builder()
                .lastname("Chirac")
                .email("coincoin3@gmail.com")
                .build();
        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);
        CandidateEvaluationGridEntity grid1=CandidateEvaluationGridEntity
                .builder()
                .grade(10.0)
                .candidateEntity(candidate2)
                .build();
        CandidateEvaluationGridEntity grid2=CandidateEvaluationGridEntity
                .builder()
                .grade(13.0)
                .candidateEntity(candidate1)
                .build();
        CandidateEvaluationGridEntity grid3=CandidateEvaluationGridEntity
                .builder()
                .grade(7.5)
                .candidateEntity(candidate3)
                .build();
        CandidateEvaluationGridEntity grid4=CandidateEvaluationGridEntity
                .builder()
                .grade(17.5)
                .candidateEntity(candidate3)
                .build();
        candidateEvaluationGridRepository.save(grid1);
        candidateEvaluationGridRepository.save(grid2);
        candidateEvaluationGridRepository.save(grid3);
        candidateEvaluationGridRepository.save(grid4);
        Set<CandidateEvaluationGridEntity> mielGrid=new HashSet<>();
        mielGrid.add(grid2);
        candidate1.setCandidateEvaluationGridEntities(mielGrid);
        Set<CandidateEvaluationGridEntity> ourzikGrid=new HashSet<>();
        ourzikGrid.add(grid1);
        candidate2.setCandidateEvaluationGridEntities(ourzikGrid);
        Set<CandidateEvaluationGridEntity> chiracGrid=new HashSet<>();
        chiracGrid.add(grid3);
        chiracGrid.add(grid4);
        candidate3.setCandidateEvaluationGridEntities(chiracGrid);
        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);

        //when
        Set<CandidateEntity> response1=candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(10.0);
        //then

        assertThat(response1).allMatch(candidateEntity -> candidateEntity.getCandidateEvaluationGridEntities().stream().anyMatch(candidateEvaluationGridEntity -> candidateEvaluationGridEntity.getGrade()<10.0));
        assertThat(response1.size()).isEqualTo(1);
        cleanCandidate(candidate1);
        cleanCandidate(candidate2);
        cleanCandidate(candidate3);
        cleanCandidateEvaluationGrid(grid1);
        cleanCandidateEvaluationGrid(grid2);
        cleanCandidateEvaluationGrid(grid3);
        cleanCandidateEvaluationGrid(grid4);


    }
    @Test
    void findAllByHasExtraTimeFalseAndBirthDateBefore(){
        // given
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Miel")
                .email("coincoin1@gmail.com")
                .birthDate(LocalDate.of(2003,12,15))
                .hasExtraTime(false)
                .build();
        CandidateEntity candidate2= CandidateEntity
                .builder()
                .lastname("Ourzik")
                .email("coincoin2@gmail.com")
                .birthDate(LocalDate.of(2002,7,6))
                .hasExtraTime(true)
                .build();
        CandidateEntity candidate3= CandidateEntity
                .builder()
                .lastname("Chirac")
                .hasExtraTime(false)
                .birthDate(LocalDate.of(1932,11,29))
                .email("coincoin3@gmail.com")
                .build();

        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);
        //when
        Set<CandidateEntity> response1=candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2003,1,1));
        //then
        assertThat(response1.size()).isEqualTo(1);
        assertThat(response1).allMatch(candidate -> !candidate.isHasExtraTime());
        assertThat(response1).allMatch(candidate -> candidate.getBirthDate().isBefore(LocalDate.of(2003,1,1)));

}
}
