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

    public List<BoardDto> findAll() {
        List<Board> boardList = this.boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();
        for (int i = 0; i < boardList.size(); i++) {
            BoardDto boardListDto = BoardDto.builder()
                    .title(boardList.get(i).getTitle())
                    .content(boardList.get(i).getContent())
                    .createdAt(boardList.get(i).getCreatedAt())
                    .updatedAt(boardList.get(i).getUpdatedAt())
                    .userId(boardList.get(i).getUserId())
                    .build();
            boardDtoList.add(boardListDto);
        }
        return boardDtoList;
    }

    public Board createBoard(BoardDto boardDto) {
        Board board = new Board();
        board.setTitle(boardDto.getTitle())
                .setContent(boardDto.getContent())
                .setCreatedAt(Date.valueOf(LocalDate.now()))
                .setUserId(boardDto.getUserId());
        return this.boardRepository.save(board);
    }

}
