package com.myjob.useapi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;


import com.myjob.useapi.mybatis.MyFactory;
import com.myjob.useapi.page.PageOther;
import com.myjob.useapi.vo.BoardAtt;
import com.myjob.useapi.vo.BoardVo;

@Component
public class BoardDao {
    
    SqlSession session;

    public Map<String,Object> boardDetail(Integer sno){
        session = new MyFactory().getSession();
        System.out.println(sno);
        Map<String,Object> map = new HashMap<>();
        BoardVo bo = session.selectOne("member.boardDetail", sno);
        List<BoardAtt> attFiles = session.selectList("member.attFiles", sno);
        map.put("bo",bo);
        map.put("lists",attFiles);
        session.close();
        return map;
    }
    public Map<String,Object> boardList(PageOther pageOther){
        session = new MyFactory().getSession();
        List<BoardVo> lists = session.selectList("member.boardSearch",pageOther);
        int totSize = session.selectOne("member.totSize", pageOther.getFindStr());
        System.out.println(totSize);
        pageOther.setTotSize(totSize);
        pageOther.compute();
        Map<String,Object> map = new HashMap<>();
        map.put("lists",lists);
        map.put("page",pageOther);
        session.close();
        return map;

    }
}
