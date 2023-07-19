package com.example.board.service;

import com.example.board.global.EConstant;
import com.example.board.model.Board;
import com.example.board.model.dto.BoardDto;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardDto findById(Long id, ArrayList<String> convertList) {
        Board board = this.boardRepository.findById(id).orElse(null);
        BoardDto boardDto = new BoardDto(board, convertList);

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
        try {
            Optional<Board> board = Optional.ofNullable(this.boardRepository.findById(boardDto.getId()).orElse(null));
            Board deleteBoard = board.get();
            deleteBoard.setDeleted(EConstant.EDeletionStatus.delete.getStatus());
            this.boardRepository.save(deleteBoard);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean update(BoardDto boardDto) {
        boardDto.setCreatedAt(this.boardRepository.findById(boardDto.getId()).get().getCreatedAt());
        Board board = new Board(boardDto);

        try {
            this.boardRepository.save(board);

            return true;
        } catch (Exception e) {

            return false;
        }
    }
}
