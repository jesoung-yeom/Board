package com.example.board.controller;

import com.example.board.model.dto.BoardDto;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/home/createboard")
    public String showWrite() {
        return "write";
    }

    @GetMapping("/home/board")
    public String showBoard(BoardDto boardDto, Model model) {
        model.addAttribute("boardDto", this.boardService.findById(boardDto.getId()));
        model.addAttribute("commentDtoList", this.commentService.findByBoardId(boardDto.getId()));

        if (boardDto.getId() != null) {

            return "board";
        } else {
            model.addAttribute("message", "조회오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/home/createboard")
    public String createBoard(HttpSession session, BoardDto boardDto) {
        boardDto.setUserId(session.getAttribute("userId").toString());
        this.boardService.create(boardDto);

        return "redirect:/home";
    }


    @PostMapping("/home/board/delete")
    public String deleteBoard(BoardDto boardDto, Model model) {
        if (this.boardService.delete(boardDto.getId())) {

            return "redirect:/home";
        } else {
            model.addAttribute("message", "삭제오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @GetMapping("/home/board/show-update")
    public String showUpdateBoard(BoardDto boardDto, Model model) {
        model.addAttribute("boardDto", this.boardService.findById(boardDto.getId()));
        if (boardDto.getId() != null) {

            return "board-update";
        } else {
            model.addAttribute("message", "조회오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/home/board/update")
    public String updateBoard(HttpSession session, BoardDto boardDto, Model model) {
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
