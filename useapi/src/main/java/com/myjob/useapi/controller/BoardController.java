package com.myjob.useapi.controller;

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
    public ModelAndView boardDetail(@RequestParam("id") String id){
        ModelAndView mv = new ModelAndView();
        BoardVo board = boardDao.boardDetail(id);
        mv.addObject("bo", board);
        mv.setViewName("board/board-view");
        return mv;

    }

}
