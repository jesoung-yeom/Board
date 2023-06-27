package com.example.board.controller;


import com.example.board.domain.Account;
import com.example.board.form.LoginForm;
import com.example.board.form.SignUpForm;
import com.example.board.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService)
    {
        this.loginService = loginService;
    }
    @GetMapping("/")
    public String Login(HttpSession session) {

        session.removeAttribute("accountname");
        session.invalidate(); //세션의 모든 속성을 삭제
        return "login";
    }

    //////////////login////////////
    @PostMapping("/login/signin")
    public String signIn(LoginForm loginForm, Model model, HttpSession session) {
        Account account = loginService.signIn(loginForm.getId(), loginForm.getPw());
        if (account != null) {
            model.addAttribute("account",account);
            session.setAttribute("accountuid",account.getUid());
            return "redirect:/index";
        } else {
            String comment = "login fail";
            return alert(model, comment,"/");
        }

    }

    public RedirectView changeUrl(String url) {
        return new RedirectView("/"+url);
    }

    public String alert( Model model, String comment, String searchUrl){
        model.addAttribute("message", comment);
        model.addAttribute("searchUrl", searchUrl);

        return "alert";
    }
    ////////forgot-password//////
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    ////////////signup//////////
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/login/signup")
    public String signup(SignUpForm signUpForm, Model model) {
        String comment = this.loginService.signUp(signUpForm);
        System.out.println(comment);
        if(comment.equals("success")){
            return alert(model,comment,"/");
        }else {
            return alert(model,comment,"/register" );
        }
    }
/*
    @PostMapping("/sendmail")
    public String sendmail(String email, Model model) {
        if(this.loginService.checkAccount(email)){
            if(this.loginService.sendMail(email)==true) {
                return alert(model, "success send","/");
            }else{
                return alert(model, "fail send","/");
            }}else{
            return alert(model, "not exsit account","/");
        }


    }

    @PostMapping("/fixpassword")
    public String fixpassword( Model model,String id,String pw, String rpw) {

        if( this.loginService.fixAccount(id,pw,rpw).equals("success")){
            return alert(model, "success fix password","/");
        }else{
            return alert(model, "not match password","/fixpassword?id="+id);
        }
    }

    @GetMapping("/fixpassword")
    public String fixpassword(@RequestParam("id")String id, Model model) {
        model.addAttribute("id",id);
        return "fixpassword";
    }
    */
}
