package com.example.board.factory;

import com.example.board.global.EConstant;
import com.example.board.model.Comment;
import com.example.board.model.dto.CommentDto;

import java.time.LocalDateTime;

public class CommentFactory {
    public static Comment convertComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setBoardId(commentDto.getBoardId())
                .setUserId(commentDto.getUserId())
                .setContentOfComment(commentDto.getContentOfComment())
                .setDeleted(EConstant.EDeletionStatus.exist.getStatus());

        if (commentDto.getId() != null) {
            comment.setId(commentDto.getId());
        }

        if (commentDto.getCreatedAt() != null) {
            comment.setCreatedAt(commentDto.getCreatedAt());

        } else {
            comment.setCreatedAt(LocalDateTime.now());
        }

        if (commentDto.getUpdatedAt() != null) {
            comment.setUpdatedAt(commentDto.getUpdatedAt());
        }

        return comment;
    }

    public static CommentDto convertCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .userId(comment.getUserId())
                .contentOfComment(comment.getContentOfComment())
                .createdAt(comment.getCreatedAt()).build();

        if (comment.getUpdatedAt() != null) {
            commentDto.setUpdatedAt(comment.getUpdatedAt());
        }

        return commentDto;
    }
}
