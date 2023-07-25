package com.example.board.service;

import com.example.board.factory.CommentFactory;
import com.example.board.global.EConstant;
import com.example.board.model.Comment;
import com.example.board.model.dto.CommentDto;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(CommentDto commentDto) {
        Comment comment = CommentFactory.convertComment(commentDto);

        return this.commentRepository.save(comment);
    }

    public boolean delete(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (!comment.isPresent()) {
            log.error("Can't delete comment");

            return false;
        }
        comment.get().setDeleted(EConstant.EDeletionStatus.delete.getStatus());
        this.commentRepository.save(comment.get());

        return true;
    }


    public boolean update(CommentDto commentDto) {
        Comment comment = CommentFactory.convertComment(commentDto);
        if (this.commentRepository.save(comment) != null) {
            log.error("Can't update comment");

            return true;
        }

        return false;
    }

    public List<CommentDto> findByBoardId(Long id) {
        Optional<List<Comment>> commentList = this.commentRepository.findByBoardIdAndDeleted(id, EConstant.EDeletionStatus.exist.getStatus());

        if (!commentList.isPresent()) {
            return Collections.emptyList();
        }

        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment comment : commentList.get()) {
            CommentDto commentDto = CommentFactory.convertCommentDto(comment);
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}
