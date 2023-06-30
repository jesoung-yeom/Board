package com.example.board.service;

import com.example.board.model.Account;
import com.example.board.model.dto.AccountDto;
import com.example.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final AccountRepository accountRepository;

    public boolean signUp(AccountDto accountDto) {
        Account account = new Account();
        account.setUserId(accountDto.getUserId())
                .setUserPw(accountDto.getUserPw())
                .setUserName(accountDto.getUserName());
        this.accountRepository.save(account);
        return true;
    }


}
