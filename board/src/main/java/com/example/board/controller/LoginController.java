package com.example.board.controller;


import com.example.board.model.dto.AccountDto;
import com.example.board.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/")
    public String showLogin(HttpSession session) {
        return "login";
    }

    @PostMapping("/signin")
    public String signIn(AccountDto accountDto, Model model, HttpSession session) {
        if (loginService.signIn(accountDto)) {
            session.setAttribute("userId", accountDto.getUserId());

            return "redirect:/home";
        } else {

            return "redirect:/";
        }
    }

}
