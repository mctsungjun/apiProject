



export function checkout(){
    let sessionId = document.getElementById("sessionId").value;
   
    document.querySelector(".checkout").onclick=()=>{
        if(sessionId != null && sessionId.trim() != "" ){
        
            $.ajax({
                url:"/gotoPayment",
                type:"POST",
                success:(resp)=>{
                    let temp = $(resp).find(".shoping");
                    $(".content-change").html(temp);
                }
            })
        }else{
            alert("로그인하세요.")
        }
    }
    //장바구니 추가
    // function increaseCart(){
    //     let currentCountEl = document.getElementById("cartCount");
    //     let currentCount = parseInt(currentCountEl.textContent,10);
    //     currentCountEl.textContent = currentCount +1;

    //     //카트 데이타베이스에 추가
    // }
    // function addCart(){
    //     let temp = document.orderName;
    //     let orderData = new FormData(temp);
    //     increaseCart();
    //     $.ajax({
    //         url:"/goodsOrder",
    //         type:"POST",
    //         data:orderData,
    //         processData:false,
    //         contentType:false,
    //         success:(resp)=>{
    //             alert(resp);
    //         }
    //     })
    // }

    document.querySelector(".btnGoodsRegister").onclick=()=>{
        $.ajax({
            url:"/goodsRegister",
            type:"GET",
            success:(resp)=>{
                let temp = $(resp).find(".goodsRegister");
                $(".content-change").html(temp);
            }
        })
    }
    return {addCart}
}
export function payment(){

    document.getElementById("btnPayment").onclick=()=>{
        let radios = document.getElementsByName("paymentMethod");
       for(const radio of radios){
        if(radio.checked){
           const selectValue = radio.id;
            if(selectValue === "kakaopay"){

                $.ajax({
                url:"/kakaoPayment",
                type:"POST",
                success:(resp)=>{
                    
                   var fileInput = window.open(resp.next_redirect_pc_url,'_blank',"width=500 height=600");
                    const checkWindowClosed =  setInterval(function(){
                        if (fileInput.closed) {
                            clearInterval(checkWindowClosed);
                            $.ajax({
                                url:"/kakaoApprovedDetali",
                                type:"POST",
                                success:(resp)=>{
                                let temp = $(resp).find(".orderDetail");
                                $(".content-change").html(temp);
                                            }
                        
                                })
                        }
                    }, 300); // 1초마다 체크
                    
                  
                    
                }
            })
            }   
        }
       }
      
     

    }
}
function callOrderDetail(){
    $.ajax({
        url:"/kakaoApprovedDetali",
        type:"POST",
        success:(resp)=>{
        let temp = $(resp).find(".orderDetail");
        $(".content-change").html(temp);
                    }

        })
}



    

