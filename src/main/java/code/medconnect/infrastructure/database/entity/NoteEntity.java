package code.medconnect.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "note")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Integer noteId;

    @Column(name = "note_content")
    private String noteContent;

    @Column(name = "date_time")
    private OffsetDateTime dateTime;

    @OneToOne(mappedBy = "note", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private VisitEntity visit;
}