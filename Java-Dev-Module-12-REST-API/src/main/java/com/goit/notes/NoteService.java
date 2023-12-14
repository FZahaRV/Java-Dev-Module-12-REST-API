package com.goit.notes;

import com.goit.generic_response.Response;
import com.goit.notes.dto.create.CreateNoteRequest;
import com.goit.notes.dto.update.UpdateNoteRequest;
import com.goit.users.User;
import com.goit.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 1000;
    private final UserService userService;
    private final NoteRepository repository;

    private Optional<Response.Error> validateCreateFields(CreateNoteRequest request) {
        if (Objects.isNull(request.getTitle()) || request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(Response.Error.INVALID_TITLE);
        }
        if (Objects.isNull(request.getContent()) || request.getContent().length() > MAX_CONTENT_LENGTH) {
            return Optional.of(Response.Error.INVALID_CONTENT);
        }
        return Optional.empty();
    }

    private Optional<Response.Error> validateUpdateFields(UpdateNoteRequest request) {
        if (Objects.nonNull(request.getTitle()) && request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(Response.Error.INVALID_TITLE);
        }
        if (Objects.nonNull(request.getContent()) && request.getContent().length() > MAX_CONTENT_LENGTH) {
            return Optional.of(Response.Error.INVALID_CONTENT);
        }
        return Optional.empty();
    }

    public Response<String> create(String username, CreateNoteRequest request) {
        Optional<Response.Error> validationError = validateCreateFields(request);
        if (validationError.isPresent()) {
            return Response.failed(validationError.get());
        }
        User user = userService.findByUsername(username);
        Note createdNote = repository.save(Note.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build());
        return Response.success(String.valueOf(createdNote.getId()));
    }

    public Response<List<Note>> getUserNotes(String username) {
        List<Note> userNotes = repository.getUserNotes(username);
        return Response.success(userNotes);
    }

    public Response<Note> update(String username, UpdateNoteRequest request) {
        Optional<Note> optionalNote = repository.findById(request.getId());
        if (optionalNote.isEmpty()) {
            return Response.failed(Response.Error.INVALID_CONTENT);
        }
        Note note = optionalNote.get();
        boolean isNotUserNote = isNotUserNote(username, note);
        if (isNotUserNote) {
            return Response.failed(Response.Error.INVALID_CONTENT);
        }
        Optional<Response.Error> validationError = validateUpdateFields(request);
        if (validationError.isPresent()) {
            return Response.failed(validationError.get());
        }
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        repository.save(note);
        return Response.success(note);
    }

    public Response<Void> delete(String username, String id) {
        Optional<Note> optionalNote = repository.findById(id);
        if (optionalNote.isEmpty()) {
            return Response.failed(Response.Error.INVALID_CONTENT);
        }
        Note note = optionalNote.get();
        boolean isNotUserNote = isNotUserNote(username, note);
        if (isNotUserNote) {
            return Response.failed(Response.Error.INVALID_CONTENT);
        }
        repository.delete(note);
        return Response.success(null);
    }

    private boolean isNotUserNote(String username, Note note) {
        return !note.getUser().getUserId().equals(username);
    }
}