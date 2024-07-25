function boardDetail(id){
    $.ajax({
        url:"/board/detail",
        type:"GET",
        data:{"id":id},
        success:(resp)=>{
            let temp = $(resp).find(".view");
            $(".boardlist").html(temp);
        }
    })
}