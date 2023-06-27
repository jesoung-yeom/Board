package com.example.board.controller;

import com.example.board.service.BoardService;
import com.example.board.service.IndexService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final IndexService indexService;
    private final BoardService boardService;

    @Autowired
    public IndexController(IndexService indexService, BoardService boardService) {
        this.indexService = indexService;
        this.boardService = boardService;
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        model.addAttribute("boardlist",this.boardService.findAllBoards());
        model.addAttribute("accountname", this.indexService.checkAccount((Long) session.getAttribute("accountuid")).getName());
        return "index";
    }

}
