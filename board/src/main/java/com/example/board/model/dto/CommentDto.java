package com.example.board.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@Builder
@Accessors(chain = true)
public class CommentDto {

    private Long id;

    private Long boardId;

    private String userEmail;

    private String contentOfComment;

    private Date createdAt;

    private Date updatedAt;
}
