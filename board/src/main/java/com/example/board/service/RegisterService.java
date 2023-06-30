package com.example.board.service;

import com.example.board.model.Account;
import com.example.board.model.dto.RegistDto;
import com.example.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final AccountRepository accountRepository;

    public boolean regist(RegistDto registDto) {
        Account account = new Account();
        account.setUserId(registDto.getUserId())
                .setUserPw(registDto.getUserPw())
                .setUserName(registDto.getUserName());
        this.accountRepository.save(account);
        return true;
    }


}
