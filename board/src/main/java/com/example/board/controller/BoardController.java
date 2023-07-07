package com.example.board.controller;

import com.example.board.model.dto.AttachFileDto;
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

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("home")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final BoardFileService boardFileService;

    @GetMapping("")
    public String home(HttpSession session, Model model) {
        Optional<Object> checkAccount = Optional.ofNullable(session.getAttribute("user-id"));
        if (checkAccount != null) {
            model.addAttribute("boardDtoList", this.boardService.findAll());

            return "home";
        } else {

            return "redirect:/";
        }
    }

    @GetMapping("/board")
    public String showBoard(BoardDto boardDto, Model model) {
        try {
            model.addAttribute("boardDto", this.boardService.findById(boardDto.getId(), this.boardFileService.convertToBase64(boardDto)));
            model.addAttribute("commentDtoList", this.commentService.findByBoardId(boardDto.getId()));

            return "board";
        } catch (Exception e) {
            model.addAttribute("message", "조회오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @GetMapping("/board/create")
    public String showWrite() {
        return "board-write";
    }

    @GetMapping("/board/update")
    public String showUpdateBoard(BoardDto boardDto, Model model) {

        try {
            model.addAttribute("boardDto", this.boardService.findById(boardDto.getId(), this.boardFileService.convertToBase64(boardDto)));
            return "board-update";
        } catch (Exception e) {
            model.addAttribute("message", "오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/board/create")
    public String createBoard(HttpSession session, BoardDto boardDto, AttachFileDto attachFileDto, Model model) {
        boardDto.setUserId(session.getAttribute("user-id").toString());
        boardDto.setId(this.boardService.create(boardDto).getId());
        attachFileDto.setBoardId(boardDto.getId());
        if (this.boardFileService.create(boardDto)) {
            this.boardFileService.fileAttach(attachFileDto);

            return "redirect:/home";
        } else {
            model.addAttribute("message", "저장오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/board/delete")
    public String deleteBoard(BoardDto boardDto, Model model) {
        if (!this.boardService.delete(boardDto)) {
            model.addAttribute("message", "삭제오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
        if (this.boardFileService.delete(boardDto)) {

            return "redirect:/home";
        } else {
            model.addAttribute("message", "삭제오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/board/update")
    public String updateBoard(HttpSession session, BoardDto boardDto, Model model) {
        boardDto.setUserId(session.getAttribute("user-id").toString());
        if (!this.boardService.update(boardDto)) {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
        if (this.boardFileService.update(boardDto)) {

            return "redirect:/home/board?id=" + boardDto.getId();
        } else {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }
}
