



export function checkout(){
    
    document.querySelector(".checkout").onclick=()=>{
        $.ajax({
            url:"/checkout",
            type:"GET",
            success:(resp)=>{
                let temp = $(resp).find(".payment");
                $(".content-change").html(temp);
            }
        })
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




    

