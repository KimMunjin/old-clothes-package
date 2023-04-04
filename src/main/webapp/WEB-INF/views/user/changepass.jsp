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
    <title>Document</title>
     <!-- Latest compiled and minified CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
	<!-- Latest compiled JavaScript -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script>
//비밀번호 일치여부(개인)
function isSame(){
	var password = document.getElementById('password');
	var checkpass = document.getElementById('pcheckpassword');
	var checkPwd = document.getElementById("password");
	var checkPwd1 = document.getElementById("pcheckpassword");

	
	if(document.getElementById('password').value != '' && document.getElementById('pcheckpassword').value != ''){
		if(document.getElementById('password').value == document.getElementById('pcheckpassword').value){
			document.getElementById('same').innerHTML='비밀번호가 일치합니다.';
			document.getElementById('same').style.color='blue';
		}else{
			document.getElementById('same').innerHTML='비밀번호가 불일치합니다.';
			document.getElementById('same').style.color='red';
		}
	}else if(checkPwd.value.search(" ") != -1 && checkPwd1.value.search(" ") != -1 ){ 
 	   alert("공백이 포함되면 안됩니다.");
    }
	
}
</script>
<script>
	var msg ="${msg}";
	if (msg != ""){
		alert(msg)
	}
	
function Validation(){
	var PPwd = document.getElementById("password");//비밀번호
	var PPwd1 = document.getElementById("pcheckpassword")//비밀번호확인
	
	if(PPwd.value==''){
		alert("비밀 번호를 입력해주세요.");
		return false;
	}

	if(PPwd1.value==''){
		alert("비밀번호일치 확인해주세요.");
		return false;
	}

	if( PPwd.value != PPwd1.value){
		alert("비밀번호가 일치하지 않습니다!!")
		return false;
	}
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
                    	
						 <h3 class="text-uppercase text-center mb-5">새 비밀번호 설정</h3> 
                    		<form action="changepass" method="post">               			
								<div id=changepassform>
									
											<input type="hidden" id="id" name="id" value="${id }">
										
			                        <div class="form-outline mb-4" style="display:flex;">
				                        <label class="form-label" for="password" style="position:absolute; margin:2.5%;">비밀번호</label>
				                        <input type="password" id="password" name="password" onchange="isSame()" class="form-control form-control-lg" style="padding:.5rem 1rem .5rem 7rem;"/>
			                        </div>
	
			                        <div class="form-outline mb-4" style="display:flex;">
				                        <label class="form-label" for="pcheckpassword" style="position:absolute; margin:2.5%;">비밀번호 확인</label>
				                        <input type="password" id="pcheckpassword" name="pcheckpassword" onchange="isSame()" class="form-control form-control-lg" style="padding:.5rem 1rem .5rem 8rem;"/>
			                        </div>
			                        <span id="same"></span>
									
			                        <div class="d-flex justify-content-center">
				                        <button type="submit"
				                            class="btn btn-outline-primary" onclick="return Validation();">새 비밀번호 설정</button>
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