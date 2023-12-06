package com.goit.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM notes n WHERE n.user_id = :username")
    List<Note> getUserNotes(String username);
}
