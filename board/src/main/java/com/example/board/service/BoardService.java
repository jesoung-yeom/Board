package com.example.board.service;

import com.example.board.model.Board;
import com.example.board.model.dto.BoardDto;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardDto findById(Long id) {
        Board board = this.boardRepository.findById(id).orElse(null);
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .userEmail(board.getUserEmail())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();

        return boardDto;
    }

    public List<BoardDto> findAll() {
        List<Board> boardList = this.boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();
        for (int i = 0; i < boardList.size(); i++) {
            BoardDto boardListDto = BoardDto.builder()
                    .id(boardList.get(i).getId())
                    .title(boardList.get(i).getTitle())
                    .content(boardList.get(i).getContent())
                    .createdAt(boardList.get(i).getCreatedAt())
                    .updatedAt(boardList.get(i).getUpdatedAt())
                    .userEmail(boardList.get(i).getUserEmail())
                    .build();
            boardDtoList.add(boardListDto);
        }

        return boardDtoList;
    }

    public Board create(BoardDto boardDto) {
        Board board = new Board();
        board.setTitle(boardDto.getTitle())
                .setContent(boardDto.getContent())
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .setUserEmail(boardDto.getUserEmail());

        return this.boardRepository.save(board);
    }

    public boolean delete(Long id) {
        try {
            this.boardRepository.deleteById(id);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean update(BoardDto boardDto) {
        Board board = new Board();
        board.setId(boardDto.getId())
                .setTitle(boardDto.getTitle())
                .setContent(boardDto.getContent())
                .setCreatedAt(this.boardRepository.findById(board.getId()).get().getCreatedAt())
                .setUpdatedAt(Date.valueOf(LocalDate.now()))
                .setUserEmail(boardDto.getUserEmail());

        try {
            this.boardRepository.save(board);

            return true;
        } catch (Exception e) {

            return false;
        }
    }
}
