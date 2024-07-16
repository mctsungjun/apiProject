package com.myjob.useapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    
    //메인 페이지
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    //로그인 페이지
    @GetMapping("/loginf")
    public String loginF(){
        return "login";
    }

    //회원등록 페이지
    @GetMapping("/registerf")
    public String registerf(){
        return "register";
    }

    //비밀번호 찾기페이지
    @GetMapping("/passwordf")
    public String findPasswordForm(){
        return "password";
    }

    //dashBoard 페이지
    @GetMapping("/dashboardf")
    public String dashboardF(){
        return "dashboard";
    }

    // 아이디/비번찿기 폼
    @GetMapping("/findIdPwf")
    public String findIdPwF(){
        return "findIdPw";
    }
}
