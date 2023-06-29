package com.example.board.service;

import com.example.board.model.Account;
import com.example.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final AccountRepository accountRepository;

}
