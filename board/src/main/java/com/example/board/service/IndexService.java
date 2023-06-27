package com.example.board.service;

import com.example.board.domain.Account;
import com.example.board.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

    private final AccountRepository accountRepository;
    @Autowired
    public IndexService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account checkAccount(Long uid) {
        Account account = this.accountRepository.findOneAccountByUid(uid);
        return account;
    }
}
