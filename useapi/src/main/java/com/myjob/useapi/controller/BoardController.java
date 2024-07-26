package com.myjob.useapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.myjob.useapi.dao.BoardDao;
import com.myjob.useapi.page.PageOther;
import com.myjob.useapi.vo.BoardVo;

import jakarta.servlet.http.HttpSession;


@RestController
public class BoardController {
    
    @Autowired
    BoardDao boardDao;

    @RequestMapping(path="/board/detail")
    public ModelAndView boardDetail(@RequestParam("sno") int sno){
        ModelAndView mv = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map = boardDao.boardDetail(sno);
        mv.addObject("attFiles", map.get("attFiles"));
        mv.addObject("bo", map.get("bo"));
        mv.setViewName("board/board_view");
        return mv;

    }

    //쓰기폼
    @RequestMapping(path="/board/boardregister")
    public ModelAndView boardRegister(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("board/board_register");
        return mv;
    }

    //boardList에서 찿기
    @RequestMapping(path="/board/boardSearch")
    public ModelAndView boardSearch(PageOther pageOther, HttpSession session){
        ModelAndView mv = new ModelAndView();
        Map<String, Object> map = boardDao.boardList(pageOther);
        session.setAttribute("findStr", pageOther.getFindStr());
        
        System.out.println(map.get("page"));
        mv.addObject("map", map);
        mv.setViewName("board/board_list");
        return mv;
    }

}
