package com.myjob.useapi.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.myjob.useapi.controller.MemberController;
import com.myjob.useapi.mybatis.MyFactory;
import com.myjob.useapi.passHash.PasswordHash;
import com.myjob.useapi.vo.MemberManagerVo;
import com.myjob.useapi.vo.MemberVo;
import com.myjob.useapi.vo.PhotoVo;

@Component
public class MemberDao {
    SqlSession session;
     
    public boolean registerR(MemberVo vo, String passwordHash){
        boolean b = false;
        System.out.println("dao입니다: " + vo);
        session = new MyFactory().getSession();
        vo.setPassword(passwordHash);

        int checkNum = session.insert("member.register", vo);
        if (checkNum >0){
            b = true;
            session.commit();
            session.close();
        }else{
            session.rollback();
            session.close();
        }
        return b;
    }

    // 로그인
    public MemberVo login(String id, String password){
        session = new MyFactory().getSession();
        try {
            String hashPassword = PasswordHash.hashPassword(password);
        MemberVo vo = new MemberVo();
        Object ob = (Object) vo;
        ob = session.selectOne("member.login",id);
        if(ob !=null){
            vo =(MemberVo)ob;
            String pwd = vo.getPassword(); //해쉬되어진 비번 가져옴
            //비밀번호 비교
            if(hashPassword.equals(pwd)){
                return vo;
        } else {
            System.out.println("비번이 틀렸습니다.");
            return null; }
        } else {
            System.out.println("해당 아이디가 존재하지 않습니다.");
            return null;
        }
        //무조건 세션닫기
        }finally{
            if(session != null){
            session.close();
        }
    }
}
//mail로 받은 비번 로그인
public MemberVo loginForgot(String id, String password) {
    session = new MyFactory().getSession();
    Map<String, String> map = new HashMap<>();
    map.put("id",id);
    map.put("password",password);     
    MemberVo vo = new MemberVo();
    
    vo= session.selectOne("member.loginForgot",map);
    session.close();
    return vo;
}

//유저아이디 체크
public String userIdchk(String id){
    session = new MyFactory().getSession();
    MemberVo vo = new MemberVo();
    String message="";
    vo = session.selectOne("member.userIdchk", id);
    if(vo !=null){
        message = vo.getId();
        return message;
    }else{
        message = "ok";
        return message;
    }
}
// 개인정보 가져오기
public MemberVo personInfo(String id){
    session = new MyFactory().getSession();
    MemberVo vo = new MemberVo();
    vo = session.selectOne("member.userId",id);
    return vo;
}


//멤버이름 가져오기
public String getMemberName(String id){
    session = new MyFactory().getSession();
    String name = session.selectOne("member.getName", id);
    return name;
}
//상세페이지 정보요청
    public MemberVo detail(String id) {
        session = new MyFactory().getSession();
        // PhotoVo 받기 위해 리스트 객체 준비
        List<PhotoVo> list = null;
        MemberVo vo = session.selectOne("member.login", id);
        list = session.selectList("member.photos", id);
        // MemberVo의 포토리스트 필드에 세팅
        vo.setPhotos(list);
        session.close();
        return vo;
    }

//사진 파일 업로드
    public String fileUpload(MemberVo vo){
        System.out.println(vo.toString());
        String msg = "";
        session= new MyFactory().getSession();
        int cnt = session.insert("member.fileUpload", vo);
        if (cnt>0){
            session.commit();
            msg="ok";
        }else{
            session.rollback();
            msg="fail";
        }
        session.close();
        return msg;
    }
    // 대표사진
        public String changePhoto(String id, String photo){
            session = new MyFactory().getSession();
            String msg ="";
            Map<String, String> map = new HashMap<>();
            map.put("id",id);
            map.put("photo",photo);
            int cnt = session.insert("member.changePhoto", map);
            if(cnt>0){
                session.commit();
                msg = "ok";
            }else{
                session.rollback();
                msg = "fail";
            }
            session.close();
            return msg;
        }

    // default 사진 불러오기
    public PhotoVo defaultPhot(){
        session = new MyFactory().getSession();
        PhotoVo phot = session.selectOne("member.defaultPhoto");
        session.close();
        return phot;
    }
    // 수정
    
    public MemberVo updateFrom(String id){
        session = new MyFactory().getSession();
        List<PhotoVo> list = null;
        MemberVo vo = session.selectOne("member.login", id);
        list = session.selectList("member.photos", id);

        vo.setPhotos(list);
        session.close();
        return vo;

    }

    // 리스트폼
    public List<MemberVo> list(String code){
        session = new MyFactory().getSession();
        MemberManagerVo reCode = session.selectOne("member.code", code);
        System.out.println(reCode);
        if (reCode !=null){
            List<MemberVo> list = session.selectList("member.list");
            session.close();
            return list;
        }else{
            session.close();
            return null;
        }
    }
    // 리스트폼에서 검색
    public List<MemberVo> search(String findStr){
        session = new MyFactory().getSession();
        List<MemberVo> list = session.selectList("member.search", findStr);
        return list;
    }
    //수정 비밀번호 없이
    public String modify(MemberVo vo){
        String msg="";
        session = new MyFactory().getSession();
        int cnt = session.update("member.update",vo);
        
        if(cnt>0){
            msg="정상 수정됨";
            // if(delFiles != null){
            //     List<String> deList = new ArrayList<>(Arrays.asList(delFiles));
            //     //데이타 베이스 파일 삭제
            //     session.delete("member.delete_files", deList);
            //     // 폴더 파일 삭제
            //     for(String delPhoto : delFiles){
                    
            //         File delFile = new File(MemberController.uploadPath+delPhoto);
            //         if(delFile.exists()){
            //             delFile.delete();
            //         }
            //     }
            //     session.commit();
            //     session.close();
            // }else{
            //     msg=" 수정중 오류 발생";
            //     session.rollback();
            //     session.close();
            // }
            session.commit();
            session.close();
        }
        return msg;
    }
    //수정 비밀번호 포함
    public String modifyWithPassword(MemberVo vo){
        String msg="";
        session = new MyFactory().getSession();
        int cnt = session.update("member.updateWithPassword",vo);
        
        if(cnt>0){
            msg="정상 수정됨";
       
            session.commit();
            session.close();
        }
        return msg;
    }

    // 네이버아이디 데이타베이스등록
    public String neverIdR(MemberVo vo){
        session = new MyFactory().getSession();
        System.out.println(vo.getNaverId());
        int cnt = session.update("member.snsId", vo);
        if (cnt>0){
            session.commit();
            //네이버아이디 등록 성공하면 현재프로그램 가입정보 가져오기
            vo = session.selectOne("member.takeInfo",vo);
            session.close();
            return vo.getId();
        }else{
            session.rollback();
            session.close();
            return "sns 아이디 등록실패";
        }
    }
    // 카카오아이디 데이타베이스등록
    public String kakaoIdR(MemberVo vo){
        session = new MyFactory().getSession();
        int cnt =session.update("member.snskId",vo);
        if (cnt>0){
            session.commit();
            //네이버아이디 등록 성공하면 현재프로그램 가입정보 가져오기
            vo = session.selectOne("member.takeInfo",vo);
            session.close();
            return vo.getId();
        }else{
            session.rollback();
            session.close();
            return "sns 아이디 등록실패";
        }
    }

    // 비번찾기
    public MemberVo findPass(String name, String email1, String email2){
        session = new MyFactory().getSession();
        System.out.println(name  +"    "+ email1+"@"+email2);
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("email1",email1);
        map.put("email2",email2);
        MemberVo vo = session.selectOne("member.findPass", map);
        System.out.println(vo);
        return vo;
    }
    // 회원삭제
    public String memberDelete(String id){
        session = new MyFactory().getSession();
        String msg = "";
        int cnt = session.delete("member.memberDelete", id);
        if (cnt>0){
            msg = "탈퇴하였습니다.";
            session.commit();
        }else{
            msg = "탈퇴실패";
            session.rollback();
        }
        session.close();
        return msg;
    }
    //회원수정
    public String modify(MemberVo vo,String[] delFiles){
        session = new MyFactory().getSession();
        String msg="";
        int cnt = session.update("member.update",vo);
        if(cnt>0){
            msg="정상 수정됨";
            if(delFiles !=null){
                List<String> deList = new ArrayList<>(Arrays.asList(delFiles));
                //데이타베이스 파일 삭제
                session.delete("member.delete_files",deList);
                //폴더 파일 삭제
                for(String delPhoto : delFiles){
                    File delFile = new File(MemberController.uploadPath+delPhoto);
                    if(delFile.exists()){
                        delFile.delete();
                    }
                }
                session.commit();
               
            }else{
                msg="수정중 오류 발생";
                session.rollback();
            }
            session.close();
        }
        return msg;

    }
}
