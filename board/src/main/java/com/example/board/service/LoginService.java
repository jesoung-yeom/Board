package com.example.board.service;

import com.example.board.global.EResponse;
import com.example.board.model.Account;
import com.example.board.model.dto.AccountDto;
import com.example.board.model.dto.ResponseDto;
import com.example.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AccountRepository accountRepository;

    public ResponseDto signIn(AccountDto accountDto) {
        Optional<Account> account = Optional.ofNullable(this.accountRepository.findByUserIdAndUserPw(accountDto.getUserId(), accountDto.getUserPw()));
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;

        if (!account.isPresent()) {
            log.info("Not match account");
            response = EResponse.EResponseValue.NMA;

            return new ResponseDto(response);
        }

        return new ResponseDto(response);
    }
}
