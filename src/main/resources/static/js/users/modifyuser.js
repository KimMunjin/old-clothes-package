/**
 * 
 */
 
//닉네임 중복확인(개인)
$(function(){
	$('#nickname').keyup(function(){
		$('#nicknamecheck').show();
		$('#checkednickname').hide();
	});
	
   $('#nicknamecheck').click(function(){
      var nickname =$('#nickname').val(); //사용자가 입력한 id값 
      var checkPname = document.getElementById("nickname");
      var hidden = document.getElementById("check_nick").value;
      
      
      $.ajax({
         type : "post",
         url : "/nickname",
         data : {nickname:nickname},
         success:function(data,textStatus){
	 		if(nickname==hidden){
				alert("사용가능한 닉네임입니다..");
                $('#nicknamecheck').hide();
       			$('#checkednickname').show();
			}else if(data=="true"){
               alert("이미 사용중인 닉네임입니다.");
            }else if(nickname==''){
               alert("닉네임을 입력해주세요");
            }else if(checkPname.value.length >= 8){
         	   alert("닉네임이 너무 길어요 8자 이하로 해주세요.");
            }else if(checkPname.value.search(" ") != -1 ){ 
         	   alert("공백이 포함되면 안됩니다.");
            }else{
               alert("사용가능한 닉네임입니다..");
                $('#nicknamecheck').hide();
       			$('#checkednickname').show();
            }
         }
      })
   });
});


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
					if(response=="true"){
						alert("동일번호로 가입할 수 있는 아이디는 최대 3개입니다.");
					}else{
					
					authCode.attr('value', response);	// authCode의 속성 value값을 인증번호로 설정
					console.log("input태그에 담긴 인증번호: " + authCode.val());	// 확인용
	
					alert('인증 번호가 발송 되었습니다.sms는 유료기능이므로 다음 코드를 입력해주세요:' + authCode.val());
					}
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


