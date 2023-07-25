package com.example.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviewAttachFileDto {

    private Long attachFileId;

    private Long boardId;

    private String fileName;

}
