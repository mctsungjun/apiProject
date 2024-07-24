package com.myjob.useapi.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.useapi.passHash.PasswordHash;
import com.myjob.useapi.dao.MemberDao;
import com.myjob.useapi.vo.MemberVo;
import com.myjob.useapi.vo.PhotoVo;
import com.myjob.useapi.service.KakaoApi;
import com.myjob.useapi.service.NaverApi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@RestController
public class MemberController {
    @Autowired
    MemberDao dao; 
    @Autowired
    NaverApi nApi;
 
    @Autowired
    KakaoApi kakaoApi;

    @Autowired
    private JavaMailSender javaMailSender;
    
    public static String uploadPath= "H:\\job4project\\useapi\\src\\main\\resources\\static\\upload\\";
     
     // 메인화면 보이기
     @RequestMapping(path="/")
     public ModelAndView index(){
         ModelAndView mv = new ModelAndView();
         mv.setViewName("index");
         return mv;
     }
 
     //로그인폼
     @RequestMapping(path="/sung/loginF")
     public ModelAndView loginF(){
         ModelAndView mv = new ModelAndView();
         mv.setViewName("login/login");
         return mv;
     }
     //로그인
     @RequestMapping(path="/sung/login")
     public ResponseEntity<Map<String, Object>> login(@RequestParam("id") String id,@RequestParam("password") String password, HttpSession session){
         // 주석 처리 표시 는 필용없음
         //System.out.println(id+"   "+password);
         Map<String, Object> response = new HashMap<>();
         MemberVo vo = new MemberVo();
         if(password.length()>=20){
            vo = dao.loginForgot(id, password);
         }else{

             vo = dao.login(id,password);
         }
         if(vo !=null){
             session.setAttribute("id", vo.getId());
             session.setAttribute("name", vo.getName());
             response.put("message", "success");
             // response.put("htmlFile","sung/detail.html");
            
             //response.put("vo",vo);
             System.out.println("로그인 되었습니다.");
         }else{
             response.put("message","false");
             System.out.println("아이디, 비번이 틀립니다.");
         }
         return ResponseEntity.ok(response);
     }
     //로그아웃
     @RequestMapping(path="/sung/logout")
     public void logout(HttpSession session){
         session.setAttribute("id", null);
         session.setAttribute("name", null);
 
     }
       //상세페이지로
       @RequestMapping(path="/sung/detail")
       public ModelAndView detail(HttpSession sesssion){
           ModelAndView mv = new ModelAndView();
           String id = (String)sesssion.getAttribute("id");
           System.out.println("id:"  +id);
           MemberVo vo = dao.detail(id);
           if(vo.getPhoto() !=null && !vo.getPhoto().equals("vo")){
             for(PhotoVo pv:vo.getPhotos()){
                 if(pv.photo.contains(vo.getPhoto())){
                     vo.setPhoto(pv.photo);;
                 }
             }
           }
         System.out.println(vo);
           mv.addObject("vo", vo);
           mv.setViewName("memberinfo/detail");
           return mv;
       }
       // 관리자 상세페이지 보기
       @RequestMapping(path="/sung/detailM")
       public ModelAndView detailM(String id){
        ModelAndView mv = new ModelAndView();
        MemberVo vo = dao.detail(id);
        mv.addObject("vo", vo);
        mv.addObject("isadmin", true);
        mv.setViewName("memberinfo/detail");
        return mv;
       }
       //이미지/파일 업로드
       @RequestMapping(path="/sung/upload")
       public String fileUpload(@RequestParam("files") MultipartFile[] photo, HttpSession session, @RequestParam("selectedPhoto") String reprePhoto){
         //ModelAndView mv = new ModelAndView();
         List<PhotoVo> photos = new ArrayList<>();
         MemberVo vo = new MemberVo();
         if (photo != null){
             UUID uuid = null;
             String sysFile = "";
             for(MultipartFile f : photo){
                 if(f.getOriginalFilename().equals("")){
                     continue;
                 }
                 // 파일업로드
                 uuid = UUID.randomUUID();
                 sysFile = String.format("%s-%s",uuid,f.getOriginalFilename());
                 File savefile = new File(uploadPath+sysFile);
                 try{
                     f.transferTo(savefile);
                 }catch(Exception e){
                     e.printStackTrace();
                 }
                 PhotoVo pv = new PhotoVo();
                
                 pv.setOriPhoto(f.getOriginalFilename());
                 pv.setPhoto(sysFile);
                 photos.add(pv);
                 System.out.println(f.getOriginalFilename());
 
             }
             if(photos.size()>0){
                 vo.setPhotos(photos);
                 vo.setPhoto(reprePhoto);
                 vo.setId((String)session.getAttribute("id"));
                 System.out.println("vo: " +vo.getPhoto());
             }
         }
         String msg = dao.fileUpload(vo);
         return msg;
       }
 // 수정폼/정보가져오기
 @RequestMapping(path="/sung/modify" )
 public ModelAndView updateFrom(HttpSession session){
     ModelAndView mv = new ModelAndView();
     String id = (String)session.getAttribute("id");
     MemberVo vo = dao.updateFrom(id);
     mv.addObject("vo", vo);
     mv.setViewName("memberinfo/update");
     return mv;
 
 }
 //대표이미지 수정폼
 @RequestMapping(path="/sung/repreChangeForm")
 public ModelAndView repreChangForm(HttpSession session){
     ModelAndView mv = new ModelAndView();
     String id = (String)session.getAttribute("id");
     MemberVo vo =dao.detail(id);
     mv.addObject("vo", vo);
     mv.setViewName("memberinfo/reprePhoto");
     return mv;
 }
 //대표이미지 바꾸기
     @RequestMapping(path="/sung/changePhoto")
     public String changePhoto(HttpSession session, String photo){
        String id = (String)session.getAttribute("id");
         String msg = dao.changePhoto(id, photo);
         return msg;
     }
 //리스트 폼으로 가기
     @RequestMapping(path="/sung/list")
     public ModelAndView list(String code){
         ModelAndView mv = new ModelAndView();
         System.out.println(code);
         List<MemberVo> list = new ArrayList<>();
         list = dao.list(code);
         System.out.println(list);
         mv.addObject("list", list);
         mv.setViewName("memberInfo/list");
         return mv;
     }
 
     // 로그인 완료시 메인페이지 사용자 이름과 로그아웃 활성화
 
 
     // @RequestMapping(path="/sung/login")
     // public ModelAndView login(@RequestParam("id") String id,@RequestParam("password") String password, HttpSession session){
     //     ModelAndView mv = new ModelAndView();
     //     System.out.println(id);
     //     System.out.println(password);
     //     String responseId = dao.login(id,password);
         
     //     if(responseId !=null){
     //         session.setAttribute("id", responseId);
     //         mv.setViewName("sung/list");
     //         System.out.println("로그인 되었습니다.");
     //         return mv;
     //     }else{
     //         mv.addObject("message", "false");
     //         System.out.println("아이디,비번이 틀립니다.");
     //         return mv;
     //     }
 
     // }
   
     //회원등록폼
     @RequestMapping(path="/sung/registerF")
     public ModelAndView registerF(){
         ModelAndView mv = new ModelAndView();
         mv.setViewName("sung/register");
         return mv;
     }
     //회원등록
           
     @RequestMapping(path="/sung/registerR" )
     public String registerR(@ModelAttribute MemberVo vo){
         ModelAndView mv = new ModelAndView();
         System.out.println("컨트롤러입니다 :"+vo);
         String passwordHash = PasswordHash.hashPassword(vo.getPassword());
         
         boolean b= false;
         b= dao.registerR(vo,passwordHash);
         mv.addObject("b", b);
         // mv.setViewName("sung/list");
         return "hi";
     }
     //아이디중복확인
     @RequestMapping(path="/memberId/chk")
    public int userIdchk(@RequestParam("userId") String id ){
        String matchId = dao.userIdchk(id);
        System.out.println("usedId:  "+matchId);
        if(matchId.equals("ok")){
            return 0;
        }else{
            return 1;

        }

    }
 
     //아이디/비번 찿기form
     @RequestMapping(path="/sung/findIdPwd")
     public ModelAndView findIdPwdForm(){
         ModelAndView mv = new ModelAndView();
         mv.setViewName("sung/findIdPw");
         
         return mv;
     }
     //리스트폼
 
 
       //네이버 로그인
    @RequestMapping(path="/naver/callback")
    // 네이버로 부터 받은 정보 code / state 를 가지고
    public ModelAndView login(@RequestParam("code") String code,@RequestParam("state") String state, HttpSession session, HttpServletRequest request) throws IOException {
        // 토근파일을 받기위해 serviec 폴더의 NaverApi.java로이동
      String accessToken = nApi.gettoken(code,state);  

      session = request.getSession();
    //   System.out.println("session:"+session);
      ModelAndView mv = new ModelAndView();
      
      MemberVo vo =null;
      // 받은 토큰을 이용해서 다시 NaverApi.java파일의 userInfo()로 보냄
      vo = nApi.userInfo(accessToken);
      
      //네이버 아이디 데이타 베이스 등록
      String resp = dao.neverIdR(vo);
    
      System.out.println(resp);
      //연결끊기
       String msg = nApi.connectionOff(accessToken);

       System.out.println(msg);
      // 세션에 담기
      
      session.setAttribute("id", resp);
      session.setAttribute("name", vo.getName());
     
      //네이버 팝어창으로 다시 이동
        mv.setViewName("login/navercallback");
    return  mv;
        
}
 
 
 //카카오 로그인
@RequestMapping(path="kakao/callback")
public ModelAndView kakaoLogin(@RequestParam("code") String code, HttpSession session) throws IOException{
    MemberVo vo=null;
    //code를 이용해서 토큰받아오기 
    
    String accessToken = kakaoApi.getAccessToken(code);
   
    //System.out.println("code: "+ code);
    ModelAndView mv = new ModelAndView();
    //받은 토큰을 이용해서 유저정보 받기
    vo = kakaoApi.userInfo(accessToken);
    System.out.println(vo.getBirthday());
    //카카오 아이디 데이타 베이스 등록
    String resp = dao.kakaoIdR(vo);
    System.out.println("카카오로그인: "+resp);
    session.setAttribute("id", resp);
    session.setAttribute("name", vo.getName());
    
    mv.setViewName("login/kakaocallback");
    return mv;
    }

    //수정정보 
    @RequestMapping(path="/sung/updateR")
    public String updateR(@RequestParam("files") List<MultipartFile> photo, String[] delFiles, @ModelAttribute MemberVo vo){
        List<PhotoVo> photos = new ArrayList<>();
        UUID uuid = null;
        String sysFile = null;
        if(photo !=null && photo.size()>0){
            for(MultipartFile f : photo){
                if(f.getOriginalFilename().equals("")){
                    continue;
                }
                PhotoVo v = new PhotoVo();
                uuid = UUID.randomUUID();
                sysFile = String.format("%s-%s",uuid, f.getOriginalFilename());
                File saveFile = new File(uploadPath+sysFile);
                try{
                    f.transferTo(saveFile);
                }catch(Exception e){
                    e.printStackTrace();
                }
                v.setPhoto(sysFile);
                v.setOriPhoto(f.getOriginalFilename());
                photos.add(v);
            }
            if(photos.size()>0){
                vo.setPhotos(photos);
            }
        }
        String msg = dao.modify(vo,delFiles);
        return msg;
    }

    // 메일로 비번 보내주기
    @RequestMapping(path="/sung/passSearch")
    public ResponseEntity<Map<String,Object>> findPass(@RequestParam("name") String name,@RequestParam("email") String email){
        String[] email1=email.split("@");
        Map<String, Object> response = new HashMap<>();
        int index = 1;
        if(index >=0 && index<email1.length){
            System.out.println(email1[0]+" "+email1[1]);
            MemberVo vo = dao.findPass(name, email1[0].trim(), email1[1].trim());
            if(vo !=null){
                String id = vo.getId();
                // 아이디/비번 메일로 보내주기
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email1[0]+"@"+email1[1]);
                message.setSubject("아이디와 비번 보냅니다.");
                message.setText("id:" + vo.getId()+" password: " +vo.getPassword());
                javaMailSender.send(message);
                response.put("message","ok");
                return ResponseEntity.ok(response);
            }else{
                response.put("message", "unknow");
                return ResponseEntity.ok(response);
            }
        }else{
            response.put("message","이메일을 다시적어주세요");
            return ResponseEntity.ok(response);
        }
    }
    //회원탈퇴
    @RequestMapping(path = "/sung/memberOff")
    public String memberOut(HttpSession session){
        String id = (String)session.getAttribute("id");
        session.setAttribute("id",null);
        String msg = dao.memberDelete(id);
        return msg;
    }
}
