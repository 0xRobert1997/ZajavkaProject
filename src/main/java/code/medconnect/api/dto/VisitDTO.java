package code.medconnect.api.dto;

import code.medconnect.domain.Doctor;
import code.medconnect.domain.Note;
import code.medconnect.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitDTO {


    Integer visitId;
    LocalDate day;
    LocalTime startTime;
    LocalTime endTime;
    boolean cancelled;
    Note note;
    Patient patient;
    Doctor doctor;


}
