package com.myjob.useapi.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.myjob.useapi.controller.BoardController;
import com.myjob.useapi.mybatis.MyFactory;
import com.myjob.useapi.page.PageOther;
import com.myjob.useapi.vo.BoardAtt;
import com.myjob.useapi.vo.BoardVo;

import jakarta.el.ELException;

@Component
public class BoardDao {
    
    SqlSession session;
    //상세보기
    public Map<String,Object> boardDetail(Integer sno){
        session = new MyFactory().getSession();
        System.out.println(sno);
        Map<String,Object> map = new HashMap<>();
        Map<String,Integer> map2 = new HashMap<>();
        BoardVo bo = session.selectOne("member.boardDetail", sno);
        int hitplus = bo.getHit()+1;
        System.out.println(hitplus);
        // 조회수 증가하기
        map2.put("sno", sno);
        map2.put("phit",hitplus);
        session.update("member.hitplus",map2);
        List<BoardAtt> attFiles = session.selectList("member.attFiles", sno);
        map.put("bo",bo);
        map.put("lists",attFiles);
        session.commit();
        session.close();
        return map;
    }
    // 목록보기
    public Map<String,Object> boardList(PageOther pageOther){
        session = new MyFactory().getSession();
        pageOther.compute();
        List<BoardVo> lists = session.selectList("member.boardSearch",pageOther);
        int totSize = session.selectOne("member.totSize", pageOther.getFindStr());
        System.out.println(totSize);
        pageOther.setTotSize(totSize);
        
        Map<String,Object> map = new HashMap<>();
        map.put("lists",lists);
        map.put("page",pageOther);
        session.close();
        return map;

    }
    // 삭제하기
    public String boardDelete(BoardVo bo){
        session= new MyFactory().getSession();
        String msg = "";
       try{
           int cnt = session.delete("member.boardDelete", bo.getSno());
           if(cnt<0){ throw new Exception();}
           // 삭제할파일 가져오기
           List<String> delfiles = session.selectList("member.select_delfiles",bo.getSno());
           //boardAtt삭제
           if(delfiles.size()>0){
            cnt = session.delete("member.delete_boardAtt",bo.getSno());
            if(cnt<0){throw new Exception();}
            for(String f: delfiles){
                File file = new File(BoardController.uploadPath+f);
                if(file.exists()) file.delete();
            }
           }
           msg = "삭제성공";
           session.commit();
       }catch(Exception e){
        e.printStackTrace();
        msg = "삭제실패";
        session.rollback();
       }
        return msg;
    }

    // 수정

    public String boardUpdateR(BoardVo boardVo,List<BoardAtt> files, String[] delFiles){
        String msg="";
        session = new MyFactory().getSession();
        System.out.println("확인: " +boardVo);
        System.out.println("파일  " + files);
        try{
            //본문수정
            int cnt = session.update("member.boardUpdater",boardVo);
            System.out.println(cnt);
            if(cnt<0){ throw new Exception();}

            //추가 파일 정보 저장
            if(files.size()>0){
                Map<String,Object> map = new HashMap<>();
                System.out.println("PSno:   "+boardVo.getSno());
                map.put("psno",boardVo.getSno());
                map.put("files",files);
                cnt = session.insert("member.registerAtt",map);
                if(cnt<0){throw new ELException();}
            }

            //삭제 선택된 파일 삭제
            if(delFiles !=null && delFiles.length>0){
                List<String> del = Arrays.asList(delFiles);
                cnt = session.delete("member.boardDelete_files",del);
                if(cnt !=del.size()){throw new Exception();}
                for(String f: del){
                    File file = new File(BoardController.uploadPath+f);
                    if(file.exists()) file.delete();
                }
                
            }
            session.commit();
            msg = "수정";

        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
            msg = "수정중 오류발생";

            //이미 업로드된 파일 삭제
            List<String> delList = new ArrayList<>();
            for(BoardAtt att : files){
                delList.add(att.getSysFile());
            }
            for(String f:delList){
                File file = new File(BoardController.uploadPath+f);
                if(file.exists()){file.delete();}
            }
            session.close();
            return msg;
        }


        return msg;
    }
    // 글 등록하기
    public String boardRegister(BoardVo bo, List<BoardAtt> savefiles){
        session = new MyFactory().getSession();
        String msg="";
        try{
            //본문 내용 저장
            int sno = session.selectOne("member.getSerial");
            bo.setSno(sno);
            bo.setPsno(sno);
            bo.setGrp(sno);
            int cnt = session.insert("member.boardRegisterR", bo);
            if(cnt<=0) throw new Exception();

            if(savefiles.size()>0){
                Map<String,Object> map = new HashMap<>();
                map.put("psno",sno);
                map.put("files",savefiles);
                cnt = session.insert("member.registerAtt", map);
                if(cnt<=0) throw new Exception();
            }
            msg="정상처리";
            session.commit();
        }catch(Exception e){
            msg="작업오류";
            if(savefiles.size()>0){
                List<String> deList = new ArrayList<>();
                for(BoardAtt att: savefiles){
                    deList.add(att.getSysFile());
                }
                for(String f: deList){

                    File delfile = new File(BoardController.uploadPath+f);
                    if(delfile.exists()) delfile.delete();
                }
            }
            session.rollback();
            e.printStackTrace();
        }
        return msg;

      
    }

    //댓글
    public String boardReplR(BoardVo bo, List<BoardAtt> files){
        String msg = "";
        session = new MyFactory().getSession();
        
        try{
            bo.setPsno(bo.getSno());
            int sno = session.selectOne("member.getSerial");
            bo.setSno(sno);
            // seq값 증가
            session.update("member.seq_up",bo);
            bo.setSeq(bo.getSeq()+1);
            bo.setDeep(bo.getDeep()+1);
            // System.out.println("psno: " +bo.getSno()+"seq: "+ bo.getSeq()+"deep: " +bo.getDeep());
            int cnt = session.insert("member.boardRegisterR",bo);
            System.out.println("dao: " +bo+"    "+files);
            if(cnt<=0) throw new Exception();

            if(files.size()>0){
                Map<String,Object> map = new HashMap<>();
                map.put("psno", bo.getSno());
                map.put("files",files);
                cnt = session.insert("member.registerAtt", map);
                if(cnt<=0){throw new Exception();}
            }
            session.commit();
            msg = "정상처리";
        }catch(Exception e){
            if(files.size()>0){
                for(BoardAtt ba: files){
                    String sysfile = ba.getSysFile();
                    File file = new File(BoardController.uploadPath+sysfile);
                    if(file.exists()){
                        file.delete();
                    }
                }
            }
            session.rollback();
            msg = "오류발생";
        }
        session.close();
        return msg;
    }

   
}
