package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.form.BoardForm;
import com.example.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> findAllBoards(){
        return this.boardRepository.findAllBoards();
    }

    public boolean createBorad(BoardForm boardForm, Long accountuid) {
        Board board = new Board();
        board.setUid(accountuid);
        board.setTitle(boardForm.getTitle());
        board.setContent(boardForm.getContents());
        Date utilDate = new Date();
        java.sql.Date date = new java.sql.Date(utilDate.getTime());
        board.setDate(date);
        return this.boardRepository.create(board);
    }

    public Board findOneById(Long id) {
        return this.boardRepository.findOneById(id);
    }
}
