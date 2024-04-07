package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.EcosSessionProgrammationStepComponent;
import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionUpdatingStatusRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.StepNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.*;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionMapper sessionMapper;
    private final ExamComponent examComponent;
    private final SessionComponent sessionComponent;
    private final EcosSessionProgrammationStepComponent ecosSessionProgrammationStepComponent;

    public SessionResponse createSession(SessionCreationRequest sessionCreationRequest){
        try {
            EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessionCreationRequest);
            EcosSessionProgrammationEntity programmation = sessionMapper.toEntity(sessionCreationRequest.getEcosSessionProgrammation());
            Set<EcosSessionProgrammationStepEntity> stepEntities = sessionCreationRequest.getEcosSessionProgrammation()
                    .getSteps()
                    .stream()
                    .map(sessionMapper::toEntity)
                    .collect(Collectors.toSet());

            Set<ExamEntity> exams = examComponent.getAllById(sessionCreationRequest.getExamsId());

            ecosSessionEntity.setExamEntities(exams);
            programmation.setEcosSessionProgrammationStepEntities(stepEntities);
            ecosSessionEntity.setEcosSessionProgrammationEntity(programmation);

            ecosSessionEntity.setStatus(SessionStatus.CREATED);

            return sessionMapper.toResponse(sessionComponent.createSession(ecosSessionEntity));
        }catch (RuntimeException | ExamNotFoundException e){
            throw new CreationSessionRestException(e.getMessage());
        }
    }

    //ENd session method

    public  Set<CandidateEvaluationGridResponse> endSession(Long sessionId) {
        try {
            //récupérer la session demandée pour modifier son état.
            EcosSessionEntity requestedSession = sessionComponent.getSessionById(sessionId);
            //récupérer la dernière étape pour vérifier qu'elle est déjà passée.
            EcosSessionProgrammationStepEntity lastStep = ecosSessionProgrammationStepComponent.getLastStep(sessionId);


            //Si la date de la dernière étape est passée  et que l'état actuel de la session est EVAL8STARTED on peut faire la modification du statut de la session
            ecosSessionProgrammationStepComponent.IsYetPassed(lastStep, requestedSession);
            sessionComponent.isStatusEvalStarted(requestedSession);

            //À CE NIVEAU , on peut modifier le status de la session car si c'est pas le cas une exception aurait été levée avant.
            requestedSession.setStatus(SessionStatus.EVAL_ENDED);
            sessionComponent.createSession(requestedSession);
            Set<CandidateEvaluationGridResponse> reponse = new HashSet<>();
            requestedSession.getExamEntities().forEach(exam -> exam.getCandidateEvaluationGridEntities().forEach(grid -> reponse.add(sessionMapper.toResponse(grid))));

            //Maintenant que le status soit modifié, on construit notre réponse attendue

            return reponse;

        } catch (SessionNotFoundException e) {
            throw new SessionNotFoundRestException(e.getMessage(), e.getSessionId());

        } catch (StepNotFoundException e) {

            throw new StepNotFoundRestException(e.getMessage());

        } catch (LastStepDateTimeNotYetPassedException e) {
            throw new SessionUpdatingStatusRestException(e.getMessage(), e.getStatus());

        } catch (WrongCurrentStatusForUpdateException e) {
            throw new SessionUpdatingStatusRestException(e.getMessage(), e.getStatus());

        }
    }
}
