package com.example.board.model.dto;

import com.example.board.model.AttachFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreviewAttachFileDto {

    private Long attachFileId;

    private Long boardId;

    private String fileName;

    public PreviewAttachFileDto(AttachFile attachFile) {
        this.attachFileId = attachFile.getId();
        this.boardId = attachFile.getBoardId();
        this.fileName = attachFile.getFileName();
    }
}
