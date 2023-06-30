package com.example.board.controller;

import com.example.board.model.dto.BoardDto;
import com.example.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/home/board")
    public String showBoard(@RequestParam("id") Long id, Model model) {
        model.addAttribute("boardDto", this.boardService.findById(id));

        return "board";
    }
}
