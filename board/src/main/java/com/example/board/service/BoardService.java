package com.example.board.service;

import com.example.board.global.enums.EBoard;
import com.example.board.global.enums.EResponse;
import com.example.board.model.Board;
import com.example.board.model.dto.BoardDto;
import com.example.board.model.dto.ResponseDto;
import com.example.board.model.mapper.BoardMapper;
import com.example.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardDto findById(Long id, List<String> convertList) {
        Optional<Board> board = this.boardRepository.findById(id);

        if (!board.isPresent()) {
            log.error("Can't find board");

            return new BoardDto();
        }

        BoardDto boardDto = BoardMapper.MAPPER.toBoardDto(board.get(), convertList);

        return boardDto;
    }

    public Page<BoardDto> findAll(Pageable pageable) {
        Page<Board> boardPage = this.boardRepository.findAllByDeleted(EBoard.EDeletionStatus.EXIST.getStatus(), pageable);
        Page<BoardDto> boardPageDto = boardPage.map(m -> BoardMapper.MAPPER.toBoardDto(m));

        return boardPageDto;
    }


    public Board create(BoardDto boardDto) {
        Board board = BoardMapper.MAPPER.toBoard(boardDto);

        try {
            this.boardRepository.save(board);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during create");
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during create");
        } catch (Exception e) {
            log.error("Occurred UnknownException during create");
        } finally {

            return board;
        }
    }

    @Transactional
    public ResponseDto delete(BoardDto boardDto) {
        Optional<Board> board = this.boardRepository.findById(boardDto.getId());
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        if (!board.isPresent()) {
            log.error("Can't not find board");
            response = EResponse.EResponseValue.CNF;

            return new ResponseDto(response);
        }

        board.get().setDeleted(EBoard.EDeletionStatus.DELETE.getStatus());

        try {
            this.boardRepository.save(board.get());
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during delete");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during delete");
            response = EResponse.EResponseValue.DAE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during delete");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }

    @Transactional
    public ResponseDto update(BoardDto boardDto) {
        Optional<Board> board = this.boardRepository.findById(boardDto.getId());
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        if (!board.isPresent()) {
            log.error("Can't not find board");
            response = EResponse.EResponseValue.CNF;

            return new ResponseDto(response);
        }

        boardDto.setCreatedAt(board.get().getCreatedAt());
        Board updateBoard = BoardMapper.MAPPER.toBoard(boardDto);
        updateBoard.setUpdatedAt(LocalDateTime.now());

        try {
            this.boardRepository.save(updateBoard);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during update");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during update");
            response = EResponse.EResponseValue.DAE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during update");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }
    }
}
