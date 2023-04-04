<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>    
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>check</title>
     <!-- Latest compiled and minified CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">	
	<!-- Latest compiled JavaScript -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Latest compiled JavaScript -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
//인증번호 5자리 번호 = 전송(개인)
$(function(){
	$('#phone').keyup(function(){
		$('#confirmBnt').show();
		$('#checkedauthNumber').hide();
	});
$(function(){
	
	let phone;
	let authCode= $('#authCode');
	$(document).on('click','#goSMS', function () {		// 버튼을 클릭 했을 경우
		let phone = $('#phone').val();	// 사용자가 입력한 전화번호
		let authCode= $("#authCode");	//휴대폰 인증번호 담을 변수
	
		// 사용자가 입력한 전화번호가 공백이 아니고, 8자리 이상일 경우
		if (phone != '' && phone.length > 8) {
			$.ajax({
				url: '/main/execute',	// 요청보낼 url
				method: 'get',
				data: {'phone': phone},	// 사용자가 입력한 휴대폰번호 전송
				success: function (response) {
					authCode.attr('value', response);	// authCode의 속성 value값을 인증번호로 설정
					console.log("input태그에 담긴 인증번호: " + authCode.val());	// 확인용
	
					alert('인증 번호가 발송 되었습니다. 다음 코드를 입력해주세요:'+authCode.val());
				
				},
				error: function(response) {
					alert('인증번호 발송에 실패하였습니다.\n잠시 후 다시 시도해주시기 바랍니다.')
				}
			});
		}else{
			alert("휴대폰 번호를 입력 해 주세요");
		}
	});

	
	//인증번호 확인 (개인)
	let authNumbers = document.getElementById("authNumber");
	$(document).on('click', '#confirmBnt', function () {
			// 인증번호가 공백이 아니고 0 이상일 경우
			console.log("text에 사용자가 입력한 인증 번호 : " + authNumbers.value);
			console.log("coolsms에서 받아온 인증 번호 : " + authCode.val());
			if (authNumbers.value !='' && parseInt(authNumbers.value)>0) {	// val()로 받으면 문자열이기 때문에 형변환
				if(authNumbers.value == authCode.val()){
					cnfCheck = true;
					alert('휴대폰 번호 인증이 완료되었습니다. 감사합니다.');
					$('#confirmBnt').hide();
					$('#checkedauthNumber').show();
				} else {
					alert('인증번호가 일치하지 않습니다.');
				}
				// 인증번호를 입력하지 않았을 경우
			}else{alert("인증번호를 입력 해 주세요"); cnfCheck = false;}
		});
	})
})


//회원가입 유효성 검사
function Validation(){
//[개인]
var id = document.getElementById("id");//아이디

	if(id.value==''){
		alert("아이디를 입력해주세요.");
		return false;
	}
	
	
	/*최종때 주석 풀어야 해요!
	if($('#checkedauthNumber').css("display")=="none"){
	      alert("본인 인증 완료해주세요.");
	      return false;
	}*/
	
}

var msg ="${msg}";
if (msg != ""){
	alert(msg)
} 

</script>

<style>

	
</style>
</head>
<body>
	<div>
		<c:import url='/WEB-INF/views/includes/header.jsp' />
	</div>
    <section class="vh-100 bg-image">
        <div class="mask d-flex align-items-center h-100 gradient-custom-3">
            <div class="container h-100" style="width:1200px;">
            	<div class="row d-flex justify-content-center align-items-center h-100">
                	<div class="col-12 col-md-9 col-lg-7 col-xl-6">
                	<div class="card" style="border-radius: 15px;">
                    	<div class="card-body p-5">
                    	<!-- <h3 class="text-uppercase text-center mb-5">아이디 찾기</h3> -->
						<h3>인증 후 <br>새 비밀번호를 설정할 수 있습니다.</h3>
                    		<form action="checkidnphone" method="post">               			
								<div id=checkidnphone>
									<br>
									
									
			                        <div class="form-outline mb-4" style="display:flex;">
				                        <label class="form-label" for="id" style="position:absolute; margin:2.5%;">아이디</label>
				                        <input type="text" id="id" name="id" class="form-control form-control-lg" style="padding:.5rem 1rem .5rem 7rem;"/>
			                        </div>
                                    <div class="form-outline mb-4" style="display:flex;">
				                        <label class="form-label" for="phone" style="position:absolute; margin:2.5%;" >전화번호</label>
				                 		<input type="text" id="phone" name="phone" class="form-control form-control-lg" style="padding:.5rem 1rem .5rem 7rem;" />
					                    <input type="button" id="goSMS" class="btn btn-light" value="본인인증" style="margin-left:2%;">
			                        </div>
			                        
			                        <div class="form-outline mb-4" style="display:flex;">
				                        <label class="form-label" for="authNumber" style="position:absolute; margin:2.5%;" >인증번호</label>
				                 		<input type="tel" id="authNumber" name="authNumber" class="form-control form-control-lg" style="padding:.5rem 1rem .5rem 8rem;" />
				                 		<input type="hidden" id="authCode"/>
					                    <input type="button" id="confirmBnt" class="btn btn-light" value="인증확인" style="margin-left:2%;">
					                    <input type="button" id="checkedauthNumber" class="btn btn-light disabled" value="확인완료" style="margin-left:2%; display:none;">
			                        </div>
									
			                        <div class="d-flex justify-content-center">
				                        <button type="submit"
				                            class="btn btn-outline-primary" onclick="return Validation();">새 비밀번호 설정하기</button>
			                        </div>
								</div>								
                    		</form>
                    	</div>
                	</div>
                	</div>
            	</div>
            </div>
        </div>
    </section>
    <div>
		<c:import url='/WEB-INF/views/includes/footer.jsp' />
	</div>
</body>

</html>