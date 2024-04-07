package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,properties ="spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class EcosSessionProgrammationStepRepositoryTest {

    @Autowired
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;
    @Autowired
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;
    @AfterEach
    void resetALL(){
        ecosSessionProgrammationRepository.deleteAll();
        ecosSessionProgrammationRepository.deleteAll();
    }


    @Test
    void  FindLastProgrammationStepFound(){

        //given
        EcosSessionProgrammationEntity ecosSessionProgrammationEntity=EcosSessionProgrammationEntity.builder()
                .label("Programmation hivernale 2024")
                .ecosSessionProgrammationStepEntities(Set.of())
                .build();
        ecosSessionProgrammationRepository.save(ecosSessionProgrammationEntity);

        EcosSessionProgrammationStepEntity step1=EcosSessionProgrammationStepEntity.builder()
                .code("M1")
                .description("Une étape parmi d'autres")
                .dateTime(LocalDateTime.now())
                .build();
        EcosSessionProgrammationStepEntity step2=EcosSessionProgrammationStepEntity.builder()
                .code("M2")
                .description("Une autre étape parmi d'autres")
                .dateTime(LocalDateTime.now().plusMinutes(2))
                .build();
        ecosSessionProgrammationStepRepository.save(step1);
        ecosSessionProgrammationStepRepository.save(step2);

        Set<EcosSessionProgrammationStepEntity> stepEntities= new HashSet<>();
        stepEntities.add(step1);
        stepEntities.add(step2);

        ecosSessionProgrammationEntity.setEcosSessionProgrammationStepEntities(stepEntities);
        ecosSessionProgrammationRepository.save(ecosSessionProgrammationEntity);

        //when
        Optional<EcosSessionProgrammationStepEntity> response = ecosSessionProgrammationStepRepository.findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(ecosSessionProgrammationEntity.getId());

        //then
        assertThat(response).isPresent();



    }
    @Test
    void  FindLastProgrammationStepNotFound(){

        //given
        EcosSessionProgrammationEntity ecosSessionProgrammationEntity=EcosSessionProgrammationEntity.builder()
                .label("Programmation hivernale 2024")
                .ecosSessionProgrammationStepEntities(Set.of())
                .build();
        ecosSessionProgrammationRepository.save(ecosSessionProgrammationEntity);


        //when
        Optional<EcosSessionProgrammationStepEntity> response = ecosSessionProgrammationStepRepository.findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(ecosSessionProgrammationEntity.getId());

        //then
        assertThat(response).isEmpty();



    }



}
