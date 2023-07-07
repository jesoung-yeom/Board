package com.example.board.controller;


import com.example.board.model.dto.AccountDto;
import com.example.board.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/")
    public String showLogin(HttpSession session) {
        Optional<Object> checkAccount = Optional.ofNullable(session.getAttribute("user-id"));
        if (checkAccount.isPresent()) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/signin")
    public String signIn(AccountDto accountDto, Model model, HttpSession session) {

        if (loginService.signIn(accountDto)) {
            session.setAttribute("user-id", accountDto.getUserId());

            return "redirect:/home";
        } else {
            model.addAttribute("message", "아이디와 비밀번호가 일치하지 않습니다.");
            model.addAttribute("replaceUrl", "/");

            return "alert";
        }
    }

    @GetMapping("/signout")
    public String signOut(HttpSession session) {
        session.removeAttribute("user-id");
        return "redirect:/";
    }

}
