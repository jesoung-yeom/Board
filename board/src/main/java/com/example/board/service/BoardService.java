package com.example.board.service;

import com.example.board.global.EConstant;
import com.example.board.model.Board;
import com.example.board.model.dto.BoardDto;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

            return new BoardDto();
        }

        BoardDto boardDto = new BoardDto(board.get(), convertList);

        return boardDto;
    }

    public Page<BoardDto> findAll(Pageable pageable) {
        Page<Board> boardPage = this.boardRepository.findAllByDeleted(EConstant.EDeletionStatus.exist.getStatus(), pageable);
        Page<BoardDto> boardPageDto = boardPage.map(m -> new BoardDto(m));

        return boardPageDto;
    }

    public Board create(BoardDto boardDto) {
        Board board = new Board(boardDto);

        return this.boardRepository.save(board);
    }

    public boolean delete(BoardDto boardDto) {
        Optional<Board> board = this.boardRepository.findById(boardDto.getId());

        if (!board.isPresent()) {

            return false;
        }

        board.get().setDeleted(EConstant.EDeletionStatus.delete.getStatus());
        this.boardRepository.save(board.get());

        return true;
    }

    public boolean update(BoardDto boardDto) {
        Optional<Board> board = this.boardRepository.findById(boardDto.getId());

        if (!board.isPresent()) {

            return false;
        }

        boardDto.setCreatedAt(board.get().getCreatedAt());
        Board updateBoard = new Board(boardDto);
        this.boardRepository.save(updateBoard);

        return true;
    }
}
