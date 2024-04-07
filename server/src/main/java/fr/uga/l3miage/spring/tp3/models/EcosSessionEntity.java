package fr.uga.l3miage.spring.tp3.models;

import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EcosSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus status;

    @OneToMany(mappedBy = "sessionEntity")
    private Set<ExamEntity> examEntities;

    @OneToOne
    private EcosSessionProgrammationEntity ecosSessionProgrammationEntity;
}
