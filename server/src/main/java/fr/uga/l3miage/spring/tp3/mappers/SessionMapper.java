package fr.uga.l3miage.spring.tp3.mappers;

import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.EcosSessionProgrammationResponse;
import fr.uga.l3miage.spring.tp3.responses.EcosSessionProgrammationStepResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ExamMapper.class})
public interface SessionMapper {

    @Mapping(target = "ecosSessionProgrammationEntity",ignore = true)
    @Mapping(target = "examEntities",ignore = true)
    EcosSessionEntity toEntity(SessionCreationRequest request);

    @Mapping(target = "ecosSessionProgrammationStepEntities",ignore = true)
    EcosSessionProgrammationEntity toEntity(SessionProgrammationCreationRequest request);

    EcosSessionProgrammationStepEntity toEntity(SessionProgrammationStepCreationRequest request);

    SessionResponse toResponse(EcosSessionEntity entity);

    EcosSessionProgrammationResponse toResponse(EcosSessionProgrammationEntity entity);

    EcosSessionProgrammationStepResponse toResponse(EcosSessionProgrammationStepEntity entity);


    //This my new created  response
    /*
    @Mapping(target = "candidateEntity" ,ignore = true)
    @Mapping(target = "examinerEntity",ignore = true)
    @Mapping(target = "examEntity",ignore = true)
    @Mapping(target = "evaluationCriteriaEntities",ignore = true)
    CandidateEvaluationGridResponse toResponse(CandidateEvaluationGridEntity entity); */

}
