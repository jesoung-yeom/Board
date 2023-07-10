package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreviewAttachFileDto {

    private Long attachFileId;

    private Long boardId;

    private String fileName;

}
