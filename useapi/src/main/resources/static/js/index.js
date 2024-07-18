// 로그인폼 불러오기
let btnLoginForm = document.querySelector(".btnLoginForm");
btnLoginForm.onclick = ()=>{
    $.ajax({
        url:"/loginf",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".change");
            $(".content-change").html(temp);
        }
    })
}

// 회원가입폼 불러오기

function btnSignupForm(){
    
    $.ajax({
        url:"/registerf",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".register");
            $(".content-change").html(temp);
        }
    })
}

// dashboard폼 불러오기
let openDashboard = ()=>{
    $.ajax({
        url:"/dashboardf",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".dashboard");
            $(".content-change").html(temp);
        }
    })
}

// 아이디/비밀번호 찾기폼 불러오기
function findIdPwd(){
    $.ajax({
        url:"/findIdPwf",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".change");
            $(".content-change").html(temp);
        }
    })
}

// 네이버트랜드검색어 컨트롤러 보내기
// let btnTrend = document.getElementById("btnTrend");

// function searchTrend(){
//     let trendFrm = document.trendForm;
//     let frmData = new FormData(trendFrm);
//     let pageSize = document.getElementById("pageSize").value;
//     frmData.append("pageSize",pageSize)
//     $.ajax({
//         url:"/trendFormApi",
//         type:"POST",
//         data:frmData,
//         processData: false,  // 필수: FormData를 문자열로 변환하지 않음
//         contentType: false,  // 필수: 컨텐츠 타입을 false로 설정하여 jQuery가 설정할 수 있도록 함
//         success:(resp)=>{
            
//             let temp = $(resp).find(".dashboard");
//             $(".content-change").html(temp);
//         }
//     })
// }
// 정수형으로

// n개씩 보기

function changePage(page) {
    let pageNum = parseInt(page,10);
    
    let trendFrm = document.trendForm;
    let frmData = new FormData(trendFrm);
    let pageSize = document.getElementById("pageSize").value;
    frmData.append("pageSize", pageSize);
    frmData.append("pageNum", pageNum); // 페이지 번호 추가
    
    $.ajax({
        url: "/trendFormApi",
        type: "POST",
        data: frmData,
        processData: false,
        contentType: false,
        success: function(resp) {
            let temp = $(resp).find(".dashboard");
            $(".content-change").html(temp);
        },
        error: function(xhr, status, error) {
            console.error("AJAX request error:", status, error);
        }
    });
}

