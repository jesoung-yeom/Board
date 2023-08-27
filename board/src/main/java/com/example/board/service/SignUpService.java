package com.example.board.service;

import com.example.board.global.enums.EResponse;
import com.example.board.model.Account;
import com.example.board.model.dto.AccountDto;
import com.example.board.model.dto.ResponseDto;
import com.example.board.model.mapper.AccountMapper;
import com.example.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder encoder;

    public ResponseDto signUp(AccountDto accountDto) {
        EResponse.EResponseValue response = EResponse.EResponseValue.OK;
        Account account = AccountMapper.MAPPER.toAccount(accountDto);

        try {
            if (!this.accountRepository.findByUserId(accountDto.getUserId()).isPresent()) {
                account.setUserPw(encoder.encode(account.getUserPw()));
                this.accountRepository.save(account);

                return new ResponseDto(response);
            }
            System.out.println(this.accountRepository.findByUserId(accountDto.getUserId()));
            log.info("Already exist account");
            response = EResponse.EResponseValue.AEA;

            return new ResponseDto(response);
        } catch (CannotGetJdbcConnectionException e) {
            log.error("Occurred CannotGetJdbcConnectionException during update");
            response = EResponse.EResponseValue.CNGJCE;
        } catch (DataAccessException e) {
            log.error("Occurred DataAccessException during update");
            response = EResponse.EResponseValue.DAE;
        } catch (NullPointerException e) {
            log.error("Occurred Null PointException during update");
            response = EResponse.EResponseValue.NPE;
        } catch (Exception e) {
            log.error("Occurred UnknownException during update");
            response = EResponse.EResponseValue.UNE;
        } finally {

            return new ResponseDto(response);
        }

    }
}
