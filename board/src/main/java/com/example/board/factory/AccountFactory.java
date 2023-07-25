package com.example.board.factory;

import com.example.board.model.Account;
import com.example.board.model.dto.AccountDto;

public class AccountFactory {
    public static Account convertAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setUserId(accountDto.getUserId())
                .setUserPw(accountDto.getUserPw())
                .setUserEmail(accountDto.getUserEmail())
                .setUserName(accountDto.getUserName());

        return account;
    }
}
