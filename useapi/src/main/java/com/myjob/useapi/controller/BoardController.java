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
        return "login/login";
    }

    //회원등록 페이지
    @GetMapping("/registerf")
    public String registerf(){
        return "login/register";
    }

    //비밀번호 찾기페이지
    @GetMapping("/passwordf")
    public String findPasswordForm(){
        return "login/password";
    }

    //dashBoard 페이지
    @GetMapping("/dashboardf")
    public String dashboardF(){
        return "menu/dashboard";
    }

    // 아이디/비번찿기 폼
    @GetMapping("/findIdPwf")
    public String findIdPwF(){
        return "login/findIdPw";
    }
    // shoping폼("/shopingf")
    @GetMapping("/shopingf")
    public String shopingf(){
        return "menu/shop";
    }
    // contack폼
    @GetMapping("/contactf")
    public String contactf(){
        return "menu/contact";
    }
    //board-list폼
    @GetMapping("/board-list")
    public String boardList(){
        return "board/board-list";
    }
    //board-register폼
    @GetMapping("/boardregister")
    public String boardRegisterf(){
        return "board/board-register";
    }

}
