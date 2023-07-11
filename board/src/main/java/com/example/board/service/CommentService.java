package com.example.board.service;

import com.example.board.model.Comment;
import com.example.board.model.dto.CommentDto;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setBoardId(commentDto.getBoardId())
                .setUserId(commentDto.getUserId())
                .setContentOfComment(commentDto.getContentOfComment())
                .setCreatedAt(LocalDateTime.now());

        return this.commentRepository.save(comment);
    }

    public boolean delete(Long id) {
        try {
            this.commentRepository.deleteById(id);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean update(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId())
                .setBoardId(commentDto.getBoardId())
                .setUserId(commentDto.getUserId())
                .setContentOfComment(commentDto.getContentOfComment())
                .setCreatedAt(commentDto.getCreatedAt())
                .setUpdatedAt(LocalDateTime.now());
        try {
            this.commentRepository.save(comment);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public List<CommentDto> findByBoardId(Long id) {
        List<Comment> commentList = this.commentRepository.findByBoardId(id);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .boardId(comment.getBoardId())
                    .userId(comment.getUserId())
                    .contentOfComment(comment.getContentOfComment())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}
