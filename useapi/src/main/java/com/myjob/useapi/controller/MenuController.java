package com.myjob.useapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.myjob.useapi.dao.BoardDao;
import com.myjob.useapi.dao.GoodsDao;
import com.myjob.useapi.page.PageOther;

import com.myjob.useapi.vo.GoodsVo;

@Controller
public class MenuController {

     @Autowired
    GoodsDao goodsDao;
    @Autowired
    BoardDao boardDao;
    
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
    public String shopingf(Model model){
         Map<String,Object> map = new HashMap<>();
        List<GoodsVo> gv = new ArrayList<>();
        
        gv = goodsDao.goodsBri();
       
        map.put("gv",gv);
        model.addAttribute("map", map);
        return "menu/shop";
    }
    // contack폼
    @GetMapping("/contactf")
    public String contactf(){
        return "menu/contact";
    }
    //board-list폼
    @GetMapping("/board_list")
    public String boardList(Model model, PageOther pageOther){
        System.out.println("ctrl : " + pageOther);
        
        Map<String,Object> map = boardDao.boardList(pageOther);
        model.addAttribute("map", map);
        System.out.println(map);
        return "board/board_list";
    }
   

}
