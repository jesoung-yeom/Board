package com.example.board.controller;

import com.example.board.model.dto.CommentDto;
import com.example.board.model.dto.ResponseDto;
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
        commentDto.setUserId(session.getAttribute("user-id").toString());
        ResponseDto createResponse = this.commentService.create(commentDto);
        if (!createResponse.getSuccess()) {
            model.addAttribute("message", "생성오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";
        }

        return "redirect:/home/board?id=" + commentDto.getBoardId();
    }

    @PostMapping("/delete")
    public String deleteComment(CommentDto commentDto, Model model) {
        ResponseDto commentDeleteResponse = this.commentService.delete(commentDto.getId());

        if (!commentDeleteResponse.getSuccess()) {
            model.addAttribute("message", "삭제오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";

        }

        return "redirect:/home/board?id=" + commentDto.getBoardId();
    }

    @PostMapping("/update")
    public String updateComment(CommentDto commentDto, Model model) {
        ResponseDto commentUpdateResponse = this.commentService.update(commentDto);

        if (!commentUpdateResponse.getSuccess()) {
            model.addAttribute("message", "수정오류");
            model.addAttribute("replaceUrl", "/home");

            return "alert";

        }

        return "redirect:/home/board?id=" + commentDto.getBoardId();
    }
}
