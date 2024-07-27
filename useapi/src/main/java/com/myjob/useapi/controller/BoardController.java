package com.myjob.useapi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import com.myjob.useapi.dao.BoardDao;
import com.myjob.useapi.page.PageOther;
import com.myjob.useapi.vo.BoardAtt;
import com.myjob.useapi.vo.BoardVo;

import jakarta.servlet.http.HttpSession;


@RestController
public class BoardController {
    
    @Autowired
    BoardDao boardDao;
    public static String uploadPath = "H:\\job4project\\useapi\\src\\main\\resources\\static\\upload\\";
    @RequestMapping(path="/board/detail")
    public ModelAndView boardDetail(@RequestParam("sno") int sno){
        ModelAndView mv = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map = boardDao.boardDetail(sno);
        System.out.println(map.get("lists"));
        mv.addObject("attFiles", map.get("lists"));
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
        System.out.println("page: " +pageOther);
        Map<String, Object> map = boardDao.boardList(pageOther);
        session.setAttribute("findStr", pageOther.getFindStr());
        
        System.out.println(map.get("page"));
        mv.addObject("map", map);
        mv.setViewName("board/board_list");
        return mv;
    }

    //updateForm 불러오기
    @RequestMapping(path = "/board/boardUdate")
    public ModelAndView boardUdateF(@RequestParam("sno") int sno, HttpSession session){
        ModelAndView mv = new ModelAndView();
        //수정하기위해서 자기가 작성한것만 수정할수있음
        String id = (String)session.getAttribute("id");
        System.out.println(id);
        Map<String,Object> map = new HashMap<>();
        map = boardDao.boardDetail(sno);
        mv.addObject("attFiles", map.get("lists"));
        mv.addObject("bo", map.get("bo"));
        mv.setViewName("/board/board_update");
        return mv;
    }

    //글 삭제
    @RequestMapping(path = "/board/boardDelete",method = RequestMethod.POST)
    public String boardDelete(@ModelAttribute BoardVo boardVo ){
        
        String msg="";
        msg = boardDao.boardDelete(boardVo);
   
     
        return msg;
    }

    //수정
    @RequestMapping(path="/board/board_update")
    public String boardUpdate(@ModelAttribute BoardVo boardVo, String[] delFiles, @RequestParam("files") List<MultipartFile> files){
        System.out.println("del files: "+ Arrays.toString(delFiles));
        System.out.println("boardVo file: " + boardVo);

        String msg= "ok";
        UUID uuid = null;
        String sysfile="";
        List<BoardAtt> attFiles = new ArrayList<>();

        for(MultipartFile file: files){
            if(file.getOriginalFilename().equals("")) continue;
            uuid = UUID.randomUUID();
            sysfile=String.format("%s-%s",uuid,file.getOriginalFilename());
            File saveFile = new File(BoardController.uploadPath+sysfile);
            try{
                file.transferTo(saveFile);

            }catch(Exception e){
                e.printStackTrace();
            }
            BoardAtt att = new BoardAtt();
            att.setOriFile(file.getOriginalFilename());
            att.setSysFile(sysfile);
            attFiles.add(att);
        }
        msg = boardDao.boardUpdateR(boardVo, attFiles, delFiles);
        return msg;
    }

    //글쓰기
    @RequestMapping(path="/board/boardRegisterR")
    public String boardRegister(BoardVo bo, @RequestParam("files") List<MultipartFile> files){
        String msg = "";
        UUID uuid = null;
        String sysfile = "";
        
        List<BoardAtt> saveFile = new ArrayList<>();
        if(files.size()>0){
            for(MultipartFile f: files){
                if(f.getOriginalFilename().equals("")) continue;
                uuid = UUID.randomUUID();
                sysfile = String.format("%s-%s",uuid,sysfile);

                BoardAtt att = new BoardAtt();
                att.setOriFile(f.getOriginalFilename());
                att.setSysFile(sysfile);
                saveFile.add(att);
                File file = new File(uploadPath+sysfile);
                try{
                    f.transferTo(file);

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
        
        msg = boardDao.boardRegister(bo,saveFile);
        return msg;

    }
    //댓글 폼
    @RequestMapping(path="/board/boardRepl")
    public ModelAndView boardRepl(BoardVo boardVo){
        ModelAndView mv = new ModelAndView();
        mv.addObject("bo", boardVo);
        mv.setViewName("board/board_repl");
        return mv;
    }
    //댓글 쓰기
    @RequestMapping(path="/board/boardReplR")
    public String boardReplR(BoardVo boardVo, @RequestParam("files") List<MultipartFile> files){
        System.out.println("컨트롤러: " + boardVo);
        String msg="";
        String sysfile="";
        UUID uuid = null;
        List<BoardAtt> savefiles = new ArrayList<>(); 
        if(files.size()>0){
            for(MultipartFile f: files){
                if(f.getOriginalFilename().equals("")) continue;
                uuid = UUID.randomUUID();
                sysfile = String.format("%s-%s",uuid,f.getOriginalFilename());
                BoardAtt att = new BoardAtt();
                att.setOriFile(f.getOriginalFilename());
                att.setSysFile(sysfile);
                savefiles.add(att);
                File file = new File(BoardController.uploadPath+sysfile);
                try{
                    f.transferTo(file);
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                
                
                
            }
        }
        msg = boardDao.boardReplR(boardVo, savefiles);
        return msg;
    }
}

