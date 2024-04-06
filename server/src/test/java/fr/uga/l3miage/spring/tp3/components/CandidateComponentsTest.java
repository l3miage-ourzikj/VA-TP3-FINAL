package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.primitives.UnsignedInts.toLong;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentsTest {
    @Autowired
    private CandidateComponent candidateComponent;
    @MockBean
    private CandidateRepository candidateRepository;
    @Test
    void getAllEliminatedCandidateIsEmpty() {
        //given
        when(candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(anyDouble())).thenReturn(new HashSet<>());
        //when
        Set<CandidateEntity> response = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5.0);
        //then
        assertThat(response).isEmpty();
    }
    @Test
    void getAllEliminatedCandidateIsNotEmpty() {
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Miel")
                .email("coincoin1@gmail.com")
                .build();
        Set<CandidateEntity> candidate1Set = new HashSet<>();
        candidate1Set.add(candidate1);
        //given
        when(candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(anyDouble())).thenReturn(candidate1Set);
        //when
        Set<CandidateEntity> response = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5.0);
        //then
        assertThat(response).isNotEmpty();
    }
    @Test
    void getCandidateByIdNotFound() {
        //given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CandidateNotFoundException.class,()->candidateComponent.getCandidatById((long)8888));

    }
    @Test
    void getCandidateByIdFound() {
        //given
        CandidateEntity candidate1= CandidateEntity
                .builder()
                .lastname("Miel")
                .email("coincoin1@gmail.com")
                .build();
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidate1));
        assertDoesNotThrow(()->candidateComponent.getCandidatById((long)8888));

    }
}
