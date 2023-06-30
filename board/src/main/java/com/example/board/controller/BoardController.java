package com.example.board.controller;

import com.example.board.model.dto.BoardDto;
import com.example.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/home/createboard")
    public String showWrite() {
        return "Write";
    }

    @PostMapping("/home/createboard")
    public String createBoard(HttpSession session, BoardDto boardDto) {
        boardDto.setUserId(session.getAttribute("userId").toString());
        this.boardService.createBoard(boardDto);
        return "redirect:/home";
    }

}
