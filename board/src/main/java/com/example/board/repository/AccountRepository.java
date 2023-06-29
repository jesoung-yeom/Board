package com.example.board.repository;

import com.example.board.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByIdAndPw(String id, String pw);

    Account findByUid(Long uid);

    Account save(Account account);

}
