package com.example.board.controller;

import com.example.board.model.dto.CommentDto;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("home/board/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public String createComment(HttpSession session, CommentDto commentDto, Model model) {
        try {
            commentDto.setUserId(session.getAttribute("user-id").toString());
            this.commentService.create(commentDto);
            return "redirect:/home/board?id=" + commentDto.getBoardId();
        } catch (Exception e) {
            model.addAttribute("message", "생성오류");
            model.addAttribute("replaceUrl", "/home/board?id=" + commentDto.getBoardId());

            return "alert";
        }
    }

    @PostMapping("/delete")
    public String deleteComment(CommentDto commentDto, Model model) {
        if (this.commentService.delete(commentDto.getId())) {

            return "redirect:/home/board?id=" + commentDto.getBoardId();
        } else {
            model.addAttribute("message", "삭제오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/update")
    public String updateComment(CommentDto commentDto, Model model) {

        if (this.commentService.update(commentDto)) {
            return "redirect:/home/board?id=" + commentDto.getBoardId();
        } else {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }
}
