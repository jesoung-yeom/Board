package com.example.board.service;

import com.example.board.domain.Account;
import com.example.board.domain.Account;
import com.example.board.form.SignUpForm;
import com.example.board.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private final AccountRepository accountRepository;

    public LoginService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public boolean checkAccount(String id) {
        if(this.accountRepository.findOneAccountById(id)!=null){
            return true;
        }
        return false;

    }

    /*
    public boolean sendMail(String email) {
        Mail mail = new Mail();
        if(mail.send(email)){
            System.out.println("service");
            return true;
        }return false;

    }

    public String fixAccount(String id, String pw ,String rpw) {
        if(checkCollect(pw,rpw)){
            Account account = this.accountRepository.findOneAccountById(id);
            account.setPw(pw);
            this.accountRepository.saveAccount(account);
            return "success";
        }else{
            return "error for password collect";
        }

    }

*/

    public boolean checkCollect(String pw,String rpw) {
        if(pw.equals(rpw)) return true;
        return false;
    }

    public Account signIn(String id, String pw)
    {
        Account account = this.accountRepository.findOneAccount(id,pw);

        return account;

    }

    public String signUp(SignUpForm signUpForm) {
        if(signUpForm.getPw().equals(signUpForm.getCpw())) {
            if(this.accountRepository.findOneAccount(signUpForm.getId(),signUpForm.getPw())==null){
                Account account = new Account();
                account.setId(signUpForm.getId());
                account.setPw(signUpForm.getPw());
                account.setName(signUpForm.getName());
                this.accountRepository.saveAccount(account);
                return "success";
            }else{
                return "exist account";
            }
        }else{
            return "not match password";
        }
    }

}
