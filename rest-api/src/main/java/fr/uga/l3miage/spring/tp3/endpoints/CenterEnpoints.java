package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.request.CandidateAddToCenterRequest;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Gestion des centres de test")
@RestController
@RequestMapping("/api/centers")
public interface CenterEnpoints {

    @Operation(description = "Ajouter à un centre de test une collection d'étudiants")
    @ApiResponse(responseCode = "202",description = "La collection de candidats a bien été rajoutée")
    @ApiResponse(responseCode = "404" ,description = "Le centre n'a pas été retrouvé!", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400" ,description = "La collection ne peut pas être vide!", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{centerId}/addCandidates")
    SessionResponse createSession(@RequestBody Set<CandidateAddToCenterRequest> request);
}
