package com.example.board.controller;

import com.example.board.model.dto.RegistDto;
import com.example.board.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/register")
    public String register() {
        return "Register";
    }

    @PostMapping("register")
    public String regist(RegistDto registDto) {
        this.registerService.regist(registDto);
        return "Home";
    }

}

