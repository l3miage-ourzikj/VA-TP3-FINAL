package fr.uga.l3miage.spring.tp3.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CandidateEvaluationGridResponse {
    private Long sheetNumber;
    private double grade;
    private LocalDateTime submissionDate;



}
