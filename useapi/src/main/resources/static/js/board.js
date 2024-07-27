
//게시판 관련 스크립트

export function loadUrl(data){
    let target = ".content-change";
    let source = "#board";
    $.ajax({
        url : data.url,
        type : data.type,
        data : data.param,
        success:(resp)=>{
            let temp = $(resp).find(source);
            $(target).html(temp);
        }
    })
}




export function list(){
    let temp = sessionStorage.getItem("board");
    let board = JSON.parse(temp);
    let findStr = board.findStr;
    document.querySelector(".findStr").value=findStr;

    
    const boardDetail=(sno)=>{
        board.sno = sno;
        sessionStorage.setItem("board",JSON.stringify(board));
        let data={
            url  :"/board/detail",
            type :"GET",
            param:{"sno":sno,"nowPage":board.nowPage}
        }
        loadUrl(data);
    }
    document.querySelector(".boardRegister").onclick=()=>{
        
        let data={
            url:"/board/boardregister",
            type:"GET"
        };
        loadUrl(data);
    }

    document.querySelector(".boardSearch").onclick=()=>{
        let findStr = document.getElementById("searchText").value;
        console.log("findStr",findStr);
        let board = JSON.parse(sessionStorage.getItem("board"));
        board.nowPage = 1;
        board.findStr = findStr;
        sessionStorage.setItem("board",JSON.stringify(board));
        let data={
            url:"/board/boardSearch",
            type:"GET",
            param:{"nowPage":board.nowPage,"findStr":findStr}
        }
        loadUrl(data);
    }
    
    document.querySelector(".btnPrev").onclick=()=>{
        let findStr = document.querySelector(".findStr").value;
        let temp = sessionStorage.getItem("board");
        let board = JSON.parse(temp);
        if(board.nowPage>1) board.nowPage -= 1;
        board.findStr = findStr;
        sessionStorage.setItem('board', JSON.stringify(board));
        console.log("findStr", findStr, 'board', board);
        let data={
            url:"/board/boardSearch",
            type:"GET",
            param:{"nowPage":board.nowPage,"findStr":findStr}
        }
        loadUrl(data);


    }

    document.querySelector(".btnNext").onclick = ()=>{
        let findStr = document.querySelector(".findStr").value;

        let temp = sessionStorage.getItem("board");
        let board = JSON.parse(temp);
        
        board.nowPage += 1;
        board.findStr = findStr;

        console.log("findStr", findStr, 'board', board);
        sessionStorage.setItem("board", JSON.stringify(board));

        let data = {
            url:"/board/boardSearch",
            type : "GET",
            param : {"nowPage" : board.nowPage, "findStr" : findStr},
        }
        loadUrl(data);
    }
    return {boardDetail}
}

// 게시판 상세보기 폼 에서 버튼클릭시


export function boardDetail(){

    let deleteId = document.querySelector(".deleteId").value;
    let writeId = document.querySelector(".writeId").value;
    let btnBoardUpdate = document.querySelector(".btnBoardUpdate");
    
 
    btnBoardUpdate.addEventListener("click",function(){
       let sno = document.querySelector(".contentSno").value;
       if(deleteId === writeId){
       $.ajax({
        url:"/board/boardUdate",
        type:"POST",
        data:{"sno":sno},
        success:(resp)=>{
            let temp = $(resp).find("#board");
            $(".content-change").html(temp);
        }
       })
    }else{
        alert("작성자가 아님.삭제불가");
    }
       
    })

    let btnList = document.querySelector(".btnBoardList");
    btnList.onclick=()=>{
        let temp = sessionStorage.getItem("board");
        let board = JSON.parse(temp);
        let data = {
            url:"/board/boardSearch",
            type : "GET",
            param : {"nowPage" : board.nowPage, "findStr" : board.findStr}
        }
        loadUrl(data);
    }

    let btnDelete = document.querySelector(".btnBoardDelete");
    btnDelete.onclick=()=>{
        
        let form = document.frmBoard;
        let frmData = new FormData(form);
        if(deleteId === writeId){
            let yn= confirm("삭제하시겠습니까?");
            if (yn===true){
                    $.ajax({
                url:"/board/boardDelete",
                type:"POST",
                data:frmData,
                processData:false,
                contentType:false,
                success:(resp)=>{
                    // alert(resp);
                    let temp = sessionStorage.getItem("board");
                    let board = JSON.parse(temp);
                    let data = {
                        url:"/board/boardSearch",
                        type : "GET",
                        param : {"nowPage" : board.nowPage, "findStr" : board.findStr}
                    }
                    loadUrl(data);
                }
            }) 
            }
        
            }else{
            alert("작성자가 아님.수정불가");
        }
    }

    document.querySelector(".btnRepl").onclick=()=>{

        let temp = document.frmBoard;
        let frmData = new FormData(temp);
        if(deleteId !== null && deleteId !==""){
            $.ajax({
            url:"/board/boardRepl",
            type:"post",
            data:frmData,
            processData:false,
            contentType:false,
            success:(resp)=>{
                let temp = $(resp).find("#board");
                $(".content-change").html(temp);
            }
        }) 
        }else{
            alert("로그인하세요.")
        }
       
    }

   
}

export function boardUpdate(){

    document.getElementById("btnGoList").onclick=()=>{
        let temp = sessionStorage.getItem("board");
        let board = JSON.parse(temp);
        let data = {
            url:"/board/boardSearch",
            type:"GET",
            param:{"nowPage":board.nowPage,"findStr":board.findStr}
        }
        loadUrl(data);
    }

    document.querySelector(".btnUpdateR").onclick=()=>{
        // let updateSno = document.querySelector(".updateSno").value;
        let temp = document.frmBoard;
        let frmData = new FormData(temp);
        $.ajax({
            url:"/board/board_update",
            type:"POST",
            data:frmData,
            processData:false,
            contentType:false,
            success:(resp)=>{
                console.log("update result: ",resp);
                let temp = sessionStorage.getItem("board");
                let board = JSON.parse(temp);
                let data = {
                    url:"/board/boardSearch",
                    type:"GET",
                    param:{"nowPage":board.nowPage,"findStr":board.findStr}
                }
                loadUrl(data);
            }
        })

    }
    
}
//쓰기
export function boardRegister(){

    document.querySelector(".btnList").onclick=()=>{
        let temp = sessionStorage.getItem("board");
        let board = JSON.parse(temp);
        let data = {
            url:"/board/boardSearch",
            type:"GET",
            param:{"nowPage":board.nowPage,"findStr":board.findStr}
        }
        loadUrl(data);

    }

    document.querySelector(".btnRegisterR").onclick=()=>{
        let frmBoard = document.frmBoard;
        let frmData = new FormData(frmBoard);
        $.ajax({
            url:"/board/boardRegisterR",
            type:"POST",
            data:frmData,
            processData:false,
            contentType:false,
            success:(resp)=>{
                console.log(resp);
                let temp = sessionStorage.getItem("board");
                let board = JSON.parse(temp);
                let data = {
                    url:"/board/boardSearch",
                    type:"GET",
                    param:{"nowPage":board.nowPage,"findStr":board.findStr}
                }
                loadUrl(data);
            }
        })
        
    }
}

export function boardRepl(){
    document.querySelector(".btnRegisterR").onclick=()=>{
           let frmBoard = document.frmBoard;
    let frmData = new FormData(frmBoard);
    $.ajax({
        url:"/board/boardReplR",
        type:"POST",
        data:frmData,
        processData:false,
        contentType:false,
        success:(resp)=>{
            console.log(resp);
            let temp = sessionStorage.getItem("board");
            let board = JSON.parse(temp);
            let data = {
                url:"/board/boardSearch",
                type:"GET",
                param:{"nowPage":board.nowPage,"findStr":board.findStr}
            }
            loadUrl(data);
        }
    })
    }

    document.querySelector(".btnList").onclick=()=>{
        let temp = sessionStorage.getItem("board");
        let board = JSON.parse(temp);
        let data = {
            url:"/board/boardSearch",
            type:"GET",
            param:{"nowPage":board.nowPage,"findStr":board.findStr}
        }
        loadUrl(data);

    }
 
}