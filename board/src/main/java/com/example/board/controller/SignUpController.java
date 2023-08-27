package com.example.board.controller;

import com.example.board.model.dto.AccountDto;
import com.example.board.model.dto.ResponseDto;
import com.example.board.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(AccountDto accountDto, Model model) {
        ResponseDto signupResponse = this.signUpService.signUp(accountDto);
        if (!signupResponse.getSuccess()) {
            model.addAttribute("message", "생성오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
            model.addAttribute("replaceUrl", "/");

            return "alert";
        }

        return "/";
    }
}

