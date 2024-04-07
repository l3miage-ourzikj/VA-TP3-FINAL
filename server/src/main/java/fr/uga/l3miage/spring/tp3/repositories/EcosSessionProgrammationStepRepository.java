package fr.uga.l3miage.spring.tp3.repositories;


import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EcosSessionProgrammationStepRepository extends JpaRepository<EcosSessionProgrammationStepEntity,Long> {

    Optional<EcosSessionProgrammationStepEntity> findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(Long idEcosSessionProgrammation);

}
