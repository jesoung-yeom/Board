package com.example.board.controller;

import com.example.board.model.dto.CommentDto;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/home/board/comment/create")
    public String createComment(HttpSession session, CommentDto commentDto) {
        commentDto.setUserId(session.getAttribute("userId").toString());
        this.commentService.create(commentDto);

        return "redirect:/home/board?id=" + commentDto.getBoardId();
    }

    @PostMapping("/home/board/comment/delete")
    public String deleteComment(CommentDto commentDto, Model model) {
        if (this.commentService.delete(commentDto.getId())) {

            return "redirect:/home/board?id=" + commentDto.getBoardId();
        } else {
            model.addAttribute("message", "삭제오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }
    }

    @PostMapping("/home/board/comment/update")
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
