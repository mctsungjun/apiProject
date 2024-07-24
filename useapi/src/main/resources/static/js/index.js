// 로그인폼 불러오기
// let btnLoginForm = document.querySelector(".btnLoginForm");
function btnLoginForm(){
    $.ajax({
        url:"/sung/loginF",
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
function openDashboard(){
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

//shoping 폼 불러오기
function shoping(){
    $.ajax({
        url:"/shopingf",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".shoping");
            $(".content-change").html(temp);
        }
    })
}

//contact 폼 불러오기
function contact(){
    $.ajax({
        url:"/contactf",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".contact");
            $(".content-change").html(temp);
        }
    })
}
// board-list불러오기
function boardList(){
    $.ajax({
        url:"/board-list",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".boardlist");
            $(".content-change").html(temp);
        }

    })
}
//board-list.html 의 쓰기 버튼 클릭시 이동
function boardRegisterf(){
    $.ajax({
        url:"/boardregister",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".register");
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
// 로그아웃
function btnLogout(){
    $.ajax({
        url:"/sung/logout",
        type:"GET",
        success:(resp)=>{
            location.href="/index";
        }
    })
}
// 상세페이지 보기
function detailInfo(){
    $.ajax({
        url:"/sung/detail",
        type:"POST",
        success:(resp)=>{
            let temp =$(resp).find(".change");
            $(".content-change").html(temp);
        }
    })
}
// 수정버튼 클릭됨-------------------------------------------

function modifyFrom(){
	
	$.ajax({
		url:"/sung/modify",
		type:"GET",
		
		success:(resp)=>{
			let temp =$(resp).find(".change");
			$(".content-change").html(temp);
		}

	})
}
//update.html 의 취소버튼

function btnUpdateCancel(){
    detailInfo();

}
//update.html 의 수정버튼
function btnModifyR(){
    let frm = document.form;
    let frmdata = new FormData(frm);
    $.ajax({
        url:"/sung/updateR",
        type:"POST",
        data:frmdata,
        processData:false,
        contentType:false,
        success:(resp)=>{
            console.log(resp)
            detailInfo();
        }
    })
}
// update.html 파일 삭제 체크 박스에 체크
function checkUp(checkbox){
    var label = checkbox.parentNode;
    if(checkbox.checked){
        label.style.textDecoration = "line-through";
        label.style.color="red";
    }else{
        label.style.textDecoration = "none";
        label.style.color="";
    }
}

//대표이미지 바뀔때 테두리변함
let repreImage="";
function change(tag,photo){
    let allImg = document.querySelectorAll(".photo_list img");
    allImg.forEach((img)=>{
        img.style.border="5px solid #ffa500";
    })
    tag.style.border="5px solid #9400d3";
    repreImage=photo;
}
//대표이미지 수정폼
function btnChangePhoto(){
    $.ajax({
        url:"/sung/repreChangeForm",
        type:"GET",
        success:(resp)=>{
            let temp = $(resp).find(".photoSection");
            $(".photoSection").html(temp);
            // 대표이미지수정
            let btnRepreChange = document.querySelector(".btnRepreChange");
            btnRepreChange.onclick=()=>{
                
                
            
                $.ajax({
                    url:"/sung/changePhoto",
                    type:"GET",
                    data:{"photo":repreImage},
                    success:(resp)=>{
                        detailInfo();
                    
                    }
                })
            }
        }
    })
}


//이미지등록버튼 클릭됨-------------------------------------------------
function regiPhoto() {
	// photoSection의 내용을 새로운 내용으로 변경
	var photoSection = document.querySelector('.photoSection');
	photoSection.innerHTML = `
	<div class="container mt-5">
	<div class="col-md-5 m-auto">	
	<label>
			<h6 class="text-success fw-bolder">파일첨부<h6>
			<input type="file" id="fileInput" class="form-control" name="files" multiple onChange="fileChange(this)"/>
		</label>
		<br/>
		<button type="button" class="btn btn-outline-primary" onClick="uploadFiles()">업로드</button>
		<fieldset class="repre">
                    <legend>대표이미지를 선택해 주세요</legend>
                </fieldset>
	</div>
	</div>
	`;
}
//대표이미지 선택
function fileChange(tag){
    let repre = document.querySelector(".repre");
    repre.innerHTML = "";
    let legend = document.createElement("legend");
    legend.textContent = "대표이미지를 선택해 주세요";
    repre.appendChild(legend);
    for(f of tag.files){
        console.log(f.name);
        let chkbox = document.createElement('input');
        let label = document.createElement('label');
        let br = document.createElement('br');
        chkbox.type='radio';
        chkbox.name='photo';
        chkbox.value=f.name;
        label.textContent=f.name;
        label.prepend(chkbox);
        repre.append(label);
        repre.append(br);
    }
}

// 이미지upload클릭
function uploadFiles(){
    let input = document.getElementById("fileInput");
    let frmData = new FormData();
    let selectedPhoto = document.querySelector('input[name="photo"]:checked');
    for(let i=0;i<input.files.length;i++){
        frmData.append('files',input.files[i]);
    }
    if(selectedPhoto){
        frmData.append("selectedPhoto",selectedPhoto.value);
    }
    $.ajax({
        url:"/sung/upload",
        type:"POST",
        data:frmData,
        processData:false,
        contentType:false,
        success:(resp)=>{
            detailInfo();
        }
    })
}

// 목록으로 이동 (관리자만)
let btnListForm = ()=>{
	let managerCode = prompt("관리코드를 입력하세요");
	if( managerCode !=null && managerCode !=""){
		$.ajax({
			url:"/sung/list",
			type:"GET",
			data:{"code":managerCode},
			success:(resp)=>{
				let temp =$(resp).find(".change");
				$(".change").html(temp);
			}
		})
	}
}

// 관리자 상세 페이지 보기
function adminView(id){
    $.ajax({
        url:"/sung/detailM",
        type:"POST",
        data:{"id":id},
        success:(resp)=>{
            let temp =$(resp).find(".change");
            $(".content-change").html(temp);
        }
    })
}
// 홈으로 이동---------------------------------------------
function btnGoHome(){
	location.href="/";
}
//비밀번호----------------------------------------------------------
function text_chkpw() {
	var form = document.querySelector("#pwd").value;
    var pattern = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,20}$/;
	if(form.length < 6 || pattern.test(form) == false){
		$('#no_pw').show();
		$('#ok_pw').hide();
	}else{
		$('#no_pw').hide();
		$('#ok_pw').show();
	}
	form.passwd_check.value = "";
	$('#no_multipw').hide();
	$('#ok_multipw').hide();
}

function multi_chkpw(){
	var formchk = document.querySelector("#pwdchk").value;
    var form = document.querySelector("#pwd").value;
	if(form != formchk) {
		$('#no_multipw').show();
		$('#ok_multipw').hide();
	}else{
		$('#no_multipw').hide();
		$('#ok_multipw').show();
	}
}
// 체크박스 선택시 필요한 양식 표시-------------------------------------------

function joinform_chk() {
	var form = document.joinForm;

			if(form.id.value==""){
				alert('아이디를 입력해주세요.');
				form.id.focus();
                return false;
					
			}
				
			// if(form.idchk.value==''){
			// 	alert("아이디 중복체크하여 주세요.");
			// 	return false;
				
			// }

			if(form.pwd.value=="") {
				alert("비밀번호를 입력해 주세요.");
				form.pwd.focus();
                return false;
				
			}
		
            if(form.pwdchk.value=="") {
				alert("비밀번호확인를 입력해 주세요.");
				form.pwd_check.focus();
                return false;
				
			}

			if(form.name.value=="") {
				alert("이름을 입력해 주세요.");
				form.name.focus();
                return false;
				
			}

			if(form.birthday.value=="") {
				alert("생년월일을 입력해 주세요.");
				form.birth.focus();
                return false;
				
			}

			if(form.roadAddress.value=="") {
				alert("주소를 입력해 주세요.");
				form.roadAddress.focus();
                return false;
				
			}
			if(form.addressDetail.value=="") {
				alert("상세주소를 입력해 주세요.");
				form.detailAddress.focus();
                return false;
			
			}
			if(form.email.value=="") {
				alert("이메일을 입력해 주세요.");
				form.emailName.focus();
				return false;
				
			}
			if(form.phone1.value=="" || form.phone2.value=="" || form.phone3.value=="") {
				alert("회원님의 휴대폰 번호를 입력해 주세요.");
				form.phone1.focus();
				return false;
				
			}
			
		return true;
	}
    function joinformBtn() {

   
        var isProperForm = joinform_chk();
        
        if (isProperForm) {
            let frm = document.joinForm;
            let frmData = new FormData(frm);
    
            // alert("FormData: " + JSON.stringify(Object.fromEntries(frmData.entries())));
            //컨트롤러 /sung/registerR로 회원등록 정보 보냄
            $.ajax({
                url: "/sung/registerR",
                type: "POST",
                data: frmData,
                processData: false,  // 필수: FormData를 문자열로 변환하지 않음
                contentType: false,  // 필수: 컨텐츠 타입을 false로 설정하여 jQuery가 설정할 수 있도록 함
                success: function(resp) {
                    alert("가입을 환영합니다!");
                    location.reload(true);
                },
                error: function(xhr, status, error) {
                    console.error('AJAX 오류 발생:', status, error);
                    // 오류 발생 시 처리할 코드를 추가할 수 있습니다.
                }
            });
        }
        
    }
     // 아이디체크----------------------------------------------------
function userChk() {

    var userId = document.querySelector("#id");
    var pattern = /^(?=.*[a-zA-Z])(?=.*[0-9]).{3,16}$/;


    if(userId.value==""){
        alert('아이디를 입력해주세요.');
        userId.focus();
        return;
    }else if(userId.value.length < 3 || userId.value.length > 16) {
        alert("아이디는 3~15자의 영문과 숫자를 포함해 주세요.");
        userId.focus();
        return;
    }else if(!pattern.test(userId.value)){
    alert('아이디는 영문과 숫자를 포함해 주세요.');
        return;
   }else{

            $.ajax({
                url:"/memberId/chk",
                type:"GET",
                data:{"userId":userId.value},
                success:(resp)=>{

                    if(resp===0){
                        console.log(resp);
                        alert("사용가능한 아이디 입니다.");
                      
                    }else if(resp===1){
                        console.log(resp);
                        alert("현재 사용중인 아이디 입니다.");
                        userId.value="";
                        userId.focus();
                    }
                }
            })

        };
    

    
}
// 아이디/비번 찿기
function idpwSearch(){
    let frm = document.idsearchForm2;
    let name = document.getElementById("name");
    let email = document.getElementById("email");
    let form = new FormData(frm);
    if(name == ""){
        alert("이름을 입력해주세요.")
        return;
    }
    if(email ==""){
        alert("이메일을 입력해주세요.")
        return;
    }

    $.ajax({
        url:"/sung/passSearch",
        type:"POST",
        data:form,
        processData: false, 
		contentType: false,
		success:(resp)=>{
			if(resp.message==="ok"){
				alert("메일로 보냈습니다.");
			}else if(resp.message==="unKnown"){
				alert("정보가 일치하지않습니다.");
			}else{
				alert(resp.message);
			}
        }
    })
}
//회원탈되
function btnMemberOut(){
    $.ajax({
        url:"/sung/memberOff",
        type:"GET",
        success:(resp)=>{
            alert(resp);
            location.reload(true);
        }
    })
}
//이메일 option값 input에 넣기-------------------------------------------
function updateEmailInput(){
    var select = document.getElementById("email_sel");
    var selectedValue = select.value;
    var email2 = document.getElementById("email2");
    email2.value = selectedValue;
}   

//--------------------------------------------------------주소----------------------------
         //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
         function sample4_execDaumPostcode(){
            var postcodePopup = new daum.Postcode({
                oncomplete: function(data) {
                  
                    var roadAddr = data.roadAddress; // 도로명 주소 변수
                    var extraRoadAddr = ''; // 참고 항목 변수
        
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraRoadAddr += data.bname;
                    }
                   
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                       extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                 
                    if(extraRoadAddr !== ''){
                        extraRoadAddr = ' (' + extraRoadAddr + ')';
                    }
        
                  
                    document.getElementById('postcode').value = data.zonecode;
                    document.getElementById("roadAddress").value = roadAddr;
                    document.getElementById("jibunAddress").value = data.jibunAddress;
       
                
                }
                
            }).open();

        }