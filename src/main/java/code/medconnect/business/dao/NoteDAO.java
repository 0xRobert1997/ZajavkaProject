package code.medconnect.business.dao;

import code.medconnect.domain.Note;
import code.medconnect.domain.Visit;

import java.util.Optional;

public interface NoteDAO {


    Note saveNote(Note note, Visit visit);

    Optional<Note> findNoteByVisitId(Integer visitId);
}
