package com.example.board.controller;

import com.example.board.model.dto.AccountDto;
import com.example.board.service.SignUpService;
import jakarta.servlet.http.HttpSession;
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
        return "signUp";
    }

    @PostMapping("/signup")
    public void signup(AccountDto accountDto) {
        this.signUpService.signUp(accountDto);
    }
}

