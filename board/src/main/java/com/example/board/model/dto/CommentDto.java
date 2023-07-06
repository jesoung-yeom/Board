package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
public class CommentDto {

    private Long id;

    private Long boardId;

    private String userId;

    private String contentOfComment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
