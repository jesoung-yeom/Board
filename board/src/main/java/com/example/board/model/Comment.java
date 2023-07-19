package com.example.board.model;


import com.example.board.global.EConstant;
import com.example.board.model.dto.CommentDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "board_comment")
@NoArgsConstructor
@Accessors(chain = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "board_info_id")
    private Long boardId;

    @Column(name = "account_id")
    private String userId;

    @Column(name = "content")
    private String contentOfComment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private String deleted;

    public Comment(CommentDto commentDto) {
        this.boardId = commentDto.getBoardId();
        this.userId = commentDto.getUserId();
        this.contentOfComment = commentDto.getContentOfComment();
        if (commentDto.getCreatedAt() != null) {
            this.createdAt = commentDto.getCreatedAt();
        } else {
            this.createdAt = LocalDateTime.now();
        }
        this.deleted = EConstant.EDeletionStatus.exist.getStatus();
        if (commentDto.getUpdatedAt() != null) {
            this.updatedAt = commentDto.getUpdatedAt();
        }
    }
}
