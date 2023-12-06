package com.goit.notes.dto.update;

import com.goit.notes.Note;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class UpdateNoteResponse {
    private Error error;
    private Note updateNote;
    public enum Error {
        ok,
        invalidNoteId,
        invalidTitleLength,
        invalidContentLength,
        insufficientPrivileges
    }
    public static UpdateNoteResponse success(Note updateNote) {
        return builder().error(Error.ok).updateNote(updateNote).build();
    }
    public static UpdateNoteResponse failed(Error error) {
        return builder().error(error).updateNote(null).build();
    }
}
