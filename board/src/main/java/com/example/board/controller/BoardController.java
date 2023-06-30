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
        return "write";
    }

    @PostMapping("/home/createboard")
    public String create(HttpSession session, BoardDto boardDto) {
        boardDto.setUserId(session.getAttribute("userId").toString());
        this.boardService.create(boardDto);

        return "redirect:/home";
    }

    @GetMapping("/home/board")
    public String showBoard(@RequestParam("id") Long id, Model model) {
        model.addAttribute("boardDto", this.boardService.findById(id));

        return "board";
    }

    @PostMapping("/home/board/delete")
    public String deleteBoard(@RequestParam("id") Long id, Model model) {
        if (this.boardService.delete(id)) {

            return "home";
        } else {
            model.addAttribute("message", "삭제오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @GetMapping("/home/board/update")
    public String showUpdateBoard(@RequestParam("id") Long id, Model model) {
        model.addAttribute("boardDto", this.boardService.findById(id));
        System.out.println("asd");
        return "board-update";
    }

    @PostMapping("/home/board/update")
    public String updateBoard(@RequestParam("id") Long id, HttpSession session, BoardDto boardDto, Model model) {
        boardDto.setId(id);
        boardDto.setUserId(session.getAttribute("userId").toString());
        if (this.boardService.update(boardDto)) {

            return "redirect:/home";
        } else {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }


}
