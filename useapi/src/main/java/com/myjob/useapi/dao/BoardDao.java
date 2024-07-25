package com.myjob.useapi.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;


import com.myjob.useapi.mybatis.MyFactory;
import com.myjob.useapi.vo.BoardVo;

@Component
public class BoardDao {
    
    SqlSession session;

    public BoardVo boardDetail(String id){
        session = new MyFactory().getSession();
        System.out.println(id);
        BoardVo bo = session.selectOne("member.detail", id);
        session.close();
        return bo;
    }
    public List<BoardVo> boardList(){
        session = new MyFactory().getSession();
        List<BoardVo> lists = session.selectList("member.boardList");
        session.close();
        return lists;

    }
}
