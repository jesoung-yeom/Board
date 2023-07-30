package com.example.board.model.mapper;

import com.example.board.model.Account;
import com.example.board.model.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);

    Account toAccount(AccountDto accountDto);

}
