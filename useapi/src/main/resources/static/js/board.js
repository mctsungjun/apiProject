
//게시판 관련 스크립트

export function loadUrl(data){
    let target = ".boardlist";
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
    return {boardDetail}
}

// 게시판 상세보기

//게시판 업데이트

// let btnBoardUpdate = document.querySelector(".btnBoardUpdate");
// let 
// btnBoardUpdate.addEventListener("click",function(){
//     $.ajax({
//         url:"/board/boardUpdate",

//     })
// })