package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.LastStepDateTimeNotYetPassedException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.StepNotFoundException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EcosSessionProgrammationStepComponent {
    private  final EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;
    public EcosSessionProgrammationStepEntity getLastStep(Long sessionProgrammationId) throws StepNotFoundException {
         Optional<EcosSessionProgrammationStepEntity>  lastStep=ecosSessionProgrammationStepRepository.findFirstByEcosSessionProgrammationEntityIdOrderByDateTimeDesc(sessionProgrammationId);
         if(lastStep.isPresent()){
             return lastStep.get();
         }else {
             throw new StepNotFoundException("No last ecosSession programmation step was found with given sessionProg id :"+ sessionProgrammationId);
         }
    }

    //Lève une exception si la  Date en paramètre n'est pas encore passée.
    public void IsYetPassed(EcosSessionProgrammationStepEntity step, EcosSessionEntity session) throws LastStepDateTimeNotYetPassedException {
            if (!(step.getDateTime().isBefore(LocalDateTime.now()))){
                throw new LastStepDateTimeNotYetPassedException("Last Step not yet passed !", session.getStatus());
            }
    }


}
