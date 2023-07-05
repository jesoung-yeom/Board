package com.example.board.repository;

import com.example.board.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUserEmailAndUserPw(String userEmail, String userPw);

    Account save(Account account);

}
