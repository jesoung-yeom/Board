package com.example.board.controller;

import com.example.board.form.BoardForm;
import com.example.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/index/write")
    public String write() {

        return "createboard";
    }

    @GetMapping("/index/board")
    public String board(@RequestParam("id")Long id,Model model) {
        model.addAttribute("board",this.boardService.findOneById(id));
        return "board";
    }

    @PostMapping("/createboard")
    public String createBoard(BoardForm boardForm, HttpSession session,Model model) {
        if(boardService.createBorad(boardForm,(Long)session.getAttribute("accountuid"))) {
            return alert(model, "success","/index");
        }
        else {
            return alert(model, "fail","/index");
        }

    }

    public String alert(Model model, String comment, String searchUrl){
        model.addAttribute("message", comment);
        model.addAttribute("searchUrl", searchUrl);

        return "alert";
    }
}
