package com.example.board.repository;

import com.example.board.domain.Account;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Transactional
public class AccountRepository {
    private final EntityManager em;

//    public boolean test(){
//        em.createQuery("ALTER TABLE 'Account' AUTO_INCREMENT=1;" +
//                "SET @COUNT = 0;" +
//                "UPDATE 'Account' SET  = @COUNT:=@COUNT+1;");
//
//        return true;
//    }

    public Account findOneAccount(String id, String pw) {
        List<Account> allList = findAllAccount();
        for(int i = 0; i<allList.size();i++) {
            if(id.equals(allList.get(i).getId())&&pw.equals(allList.get(i).getPw())){
                return allList.get(i);
            }
        }
        return null;
    }
    public Account findOneAccountById(String id) {
        List<Account> allList = findAllAccount();
        for(int i = 0; i<allList.size();i++) {
            if(id.equals(allList.get(i).getId())){
                return allList.get(i);
            }
        }
        return null;
    }
    public Account findOneAccountByUid(Long uid) {
        List<Account> allList = findAllAccount();
        for(int i = 0; i<allList.size();i++) {
            if(uid.equals(allList.get(i).getUid())){
                return allList.get(i);
            }
        }
        return null;
    }

    public List<Account> findAllAccount() {
        return em.createQuery("select c from Account c", Account.class).getResultList();
    }
    @Transactional
    public void saveAccount(Account account) {
        em.persist(account);
    }

//    @Transactional
//    public void update(Account account){
//        em.persist(account);
//    }


}
