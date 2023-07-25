package com.example.board.service;

import com.example.board.global.EConstant;
import com.example.board.model.Comment;
import com.example.board.model.dto.CommentDto;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(CommentDto commentDto) {
        Comment comment = new Comment(commentDto);

        return this.commentRepository.save(comment);
    }

    public boolean delete(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (!comment.isPresent()) {

            return false;
        }
        comment.get().setDeleted(EConstant.EDeletionStatus.delete.getStatus());
        this.commentRepository.save(comment.get());

        return true;
    }

    public boolean update(CommentDto commentDto) {
        Comment comment = new Comment(commentDto);
        try {
            this.commentRepository.save(comment);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public List<CommentDto> findByBoardId(Long id) {
        List<Comment> commentList = this.commentRepository.findByBoardIdAndDeleted(id, EConstant.EDeletionStatus.exist.getStatus());
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDto commentDto = new CommentDto(comment);
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}
