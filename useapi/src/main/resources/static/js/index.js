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
            //차트 집어넣기
            chartLine(frmData);
        },
        error: function(xhr, status, error) {
            console.error("AJAX request error:", status, error);
        }
    });
}

function chartLine(frmData){
    var chartone = document.getElementById("myChart1").getContext('2d');
    var charttwo = document.getElementById("myChart2").getContext('2d');
    $.ajax({
        url: "/chartadd",
        type: "POST",
        data: frmData,
        processData: false,
        contentType: false,
        success: function(resp) {
            // alert(resp);
            //Line chart
            // resp.periods와 resp.ratios를 각각 labels와 data로 사용
            let chart = new Chart(chartone, {
                type: 'line',
                data: {
                    labels: resp.periods, // labels에는 resp.periods 배열을 넣음
                    datasets: [{
                        data: resp.ratios, // 첫 번째 dataset의 data에 resp.ratios 배열을 넣음
                        label: resp.keywords,
                        borderColor: "#3e95cd",
                        fill: false
                    }, {
                        data: resp.ratios2, // 두 번째 dataset의 data에 resp.ratios2 배열을 넣음
                        label: resp.keywords2,
                        borderColor: "#8e5ea2",
                        fill: false 
                    }]
                },
                options: { // options는 옵션 (option이 아니라 options임에 유의)
                    title: {
                        display: false,
                        text: '검색어비율'
                    }
                }
            });

            //bar chart

            let chart2 = new Chart(charttwo, {
                type: 'bar',
                data: {
                    labels: resp.periods, // labels에는 resp.periods 배열을 넣음
                    datasets: [{
                        data: resp.ratios, // 첫 번째 dataset의 data에 resp.ratios 배열을 넣음
                        label: resp.keywords,
                        backgroundColor: '#3e95cd', // dataset 배경색
                        borderColor: 'white', // dataset 테두리 색상
                        borderWidth: 2, // dataset 테두리 두께
                        maxBarThickness: 30,// 최대 bar의 두께 설정
                        fill: false
                    }, {
                        data: resp.ratios2, // 두 번째 dataset의 data에 resp.ratios2 배열을 넣음
                        label: resp.keywords2,
                        backgroundColor: '#8e5ea2', // dataset 배경색
                        borderColor: 'white', // dataset 테두리 색상
                        borderWidth: 2, // dataset 테두리 두께
                        maxBarThickness: 30 ,// 최대 bar의 두께 설정
                        fill: false 
                    }]
                },
                options: { // options는 옵션 (option이 아니라 options임에 유의)
                    title: {
                        display: false,
                        text: '검색어비율'
                    }
                }
            });
        }
    });
}