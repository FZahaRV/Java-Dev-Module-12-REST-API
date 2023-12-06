package com.goit.notes.dto.update;

import lombok.Data;
@Data
public class UpdateNoteRequest {
    private String id;
    private String title;
    private String content;
}
