package com.example.board.model.dto;

import com.example.board.model.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private Long boardId;

    private String userId;

    private String contentOfComment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String deleted;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.boardId = comment.getBoardId();
        this.userId = comment.getUserId();
        this.contentOfComment = comment.getContentOfComment();
        this.createdAt = comment.getCreatedAt();

        if (comment.getUpdatedAt() != null) {
            this.updatedAt = comment.getUpdatedAt();
        }
    }
}
