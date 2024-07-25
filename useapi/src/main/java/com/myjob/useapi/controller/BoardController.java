package com.myjob.useapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.useapi.dao.BoardDao;
import com.myjob.useapi.vo.BoardVo;


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

}
