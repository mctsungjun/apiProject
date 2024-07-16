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