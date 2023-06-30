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

    private BoardService boardService;

    @GetMapping("/home/board/write")
    public String showWrite() {
        return "Write";
    }

    @PostMapping("/home/board/write")
    public String write(HttpSession session, BoardDto boardDto) {
        // boardDto.setUserName(session.getAttribute("userId").toString());
        //his.boardService.wrtie()

        return null;
    }


}
