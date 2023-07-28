package com.example.board.service;

import com.example.board.factory.CommentFactory;
import com.example.board.global.EConstant;
import com.example.board.global.EResponse;
import com.example.board.model.Comment;
import com.example.board.model.dto.CommentDto;
import com.example.board.model.dto.ResponseDto;
import com.example.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
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

    @Transactional
    public ResponseDto create(CommentDto commentDto) {
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        try {
            Comment comment = CommentFactory.convertComment(commentDto);
            this.commentRepository.save(comment);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during create");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during create");
            response = EResponse.EResponseValue.DAE;
        } catch (NullPointerException e) {
            log.error("Occurred Null PointException during create");
            response = EResponse.EResponseValue.NPE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during create");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }

    @Transactional
    public ResponseDto delete(Long id) {
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        try {
            Optional<Comment> comment = this.commentRepository.findById(id);
            comment.get().setDeleted(EConstant.EDeletionStatus.delete.getStatus());
            this.commentRepository.save(comment.get());
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during delete");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during delete");
            response = EResponse.EResponseValue.DAE;
        } catch (NullPointerException e) {
            log.error("Occurred Null PointException during delete");
            response = EResponse.EResponseValue.NPE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during delete");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }


    @Transactional
    public ResponseDto update(CommentDto commentDto) {
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        try {
            Comment comment = CommentFactory.convertComment(commentDto);
            this.commentRepository.save(comment);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during update");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during update");
            response = EResponse.EResponseValue.DAE;
        } catch (NullPointerException e) {
            log.error("Occurred Null PointException during update");
            response = EResponse.EResponseValue.NPE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during update");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
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
