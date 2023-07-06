package com.example.board.controller;

import com.example.board.model.dto.BoardDto;
import com.example.board.service.BoardFileService;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("home")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final BoardFileService boardFileService;

    @GetMapping("")
    public String home(HttpSession session, Model model) {
        model.addAttribute("boardDtoList", this.boardService.findAll());

        return "home";
    }

    @GetMapping("/board")
    public String showBoard(BoardDto boardDto, Model model) {
        model.addAttribute("boardDto", this.boardService.findById(boardDto.getId(), this.boardFileService.convertToBase64(boardDto)));
        model.addAttribute("commentDtoList", this.commentService.findByBoardId(boardDto.getId()));

        if (boardDto.getId() != null) {

            return "board";
        } else {
            model.addAttribute("message", "조회오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @GetMapping("/board/create")
    public String showWrite() {
        return "write";
    }

    @PostMapping("/board/create")
    public String createBoard(HttpSession session, BoardDto boardDto) {
        boardDto.setUserId(session.getAttribute("user-id").toString());
        boardDto.setId(this.boardService.create(boardDto).getId());
        this.boardFileService.create(boardDto);

        return "redirect:/home";
    }

    @PostMapping("/board/delete")
    public String deleteBoard(BoardDto boardDto, Model model) {
        this.boardService.delete(boardDto);
        if (this.boardFileService.delete(boardDto)) {

            return "redirect:/home";
        } else {
            model.addAttribute("message", "삭제오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @GetMapping("/board/update")
    public String showUpdateBoard(BoardDto boardDto, Model model) {
        model.addAttribute("boardDto", this.boardService.findById(boardDto.getId(), this.boardFileService.convertToBase64(boardDto)));
        if (boardDto.getId() != null) {

            return "board-update";
        } else {
            model.addAttribute("message", "조회오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/board/update")
    public String updateBoard(HttpSession session, BoardDto boardDto, Model model) {
        boardDto.setUserId(session.getAttribute("user-id").toString());
        if (this.boardService.update(boardDto)) {
            this.boardFileService.update(boardDto);
            return "redirect:/home";
        } else {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }


}
