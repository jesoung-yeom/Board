package com.example.board.service;

import com.example.board.model.Account;
import com.example.board.model.dto.AccountDto;
import com.example.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;

    public boolean signIn(AccountDto accountDto) {
        Optional<Account> account = Optional.ofNullable(this.accountRepository.findByUserIdAndUserPw(accountDto.getUserId(), accountDto.getUserPw()));

        return account.isPresent();
    }
}
