package com.goit.notes;

import com.goit.generic_response.Response;
import com.goit.notes.dto.create.CreateNoteRequest;
import com.goit.notes.dto.update.UpdateNoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    @PostMapping
    public Response<String> create(Principal principal, @RequestBody CreateNoteRequest request) {
        return noteService.create(principal.getName(), request);
    }
    @GetMapping
    public Response<List<Note>> getUserNotes(Principal principal) {
        return noteService.getUserNotes(principal.getName());
    }
    @PatchMapping
    public Response<Note> update(Principal principal, @RequestBody UpdateNoteRequest request) {
        return noteService.update(principal.getName(), request);
    }
    @DeleteMapping
    public Response<Void> delete(Principal principal, @RequestParam(name = "id") String id) {
        return noteService.delete(principal.getName(), id);
    }
}
