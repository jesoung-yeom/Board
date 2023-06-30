package com.example.board.controller;

import com.example.board.service.BoardService;
import com.example.board.service.HomeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    private final BoardService boardService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        model.addAttribute("boardList", this.boardService.findAll());

        //   model.addAttribute("boardlist",this.boardService.findAllBoards());
        //   model.addAttribute("accountname", this.homeService.checkAccount((Long) session.getAttribute("accountuid")).getName());
        return "Home";
    }

}
