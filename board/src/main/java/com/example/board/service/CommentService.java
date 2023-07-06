package com.example.board.service;

import com.example.board.model.Comment;
import com.example.board.model.dto.CommentDto;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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
        for (int i = 0; i < commentList.size(); i++) {
            CommentDto commentDto = CommentDto.builder()
                    .id(commentList.get(i).getId())
                    .boardId(commentList.get(i).getBoardId())
                    .userId(commentList.get(i).getUserId())
                    .contentOfComment(commentList.get(i).getContentOfComment())
                    .createdAt(commentList.get(i).getCreatedAt())
                    .updatedAt(commentList.get(i).getUpdatedAt())
                    .build();
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}
