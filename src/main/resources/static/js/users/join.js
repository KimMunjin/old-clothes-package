/**
 * 
 */
 
 //업체 개인 radio버튼 체크씨 view전환
$(function(){
	$('#businessform').hide();
	$('#personalform').show();
	$('input[type="radio"][id="person"]').on('click', function(){
		var chkValue = $('input[type=radio][id="person"]:checked').val();
		if(chkValue){
			$('#businessform').hide();
			$('#personalform').show();
		}
	});
	$('input[type="radio"][id="business"]').on('click', function(){
		var chkValue = $('input[type=radio][id="business"]:checked').val();
		if(chkValue){
			$('#businessform').show();
			$('#personalform').hide();
		}
	});
});


//아이디 중복확인(개인)
$(function(){
  $('#userid').keyup(function(){
     $('#useridcheck').show();
     $('#checkeduserid').hide();
  });
  $('#useridcheck').click(function(){
	 var engid = /^[a-zA-Z0-9]*$/;
     var checkuserid =$('#userid').val();
     var checkPid = document.getElementById("userid");
     $.ajax({
        type : "post",
        url : "/checkuserid",
        data : {checkuserid:checkuserid},
        success:function(data,textStatus){
           if(data=="true"){
              alert("이미 사용중인 아이디입니다");
           }else if (checkuserid==''){
              alert("아이디을 입력해주세요");
           }else if(checkPid.value.length >= 10){
        	   alert("아이디가 너무 길어요 10자 이하로 해주세요.");
           }else if(checkPid.value.search(" ") != -1 ){ 
        	   alert("공백이 포함되면 안됩니다.");
          }else if(!engid.test(checkuserid)){
			   alert("영문+숫자 조합만 가능합니다.");
		   }else{
              alert("사용 가능한 아이디입니다.");
               $('#useridcheck').hide();
               $('#checkeduserid').show();
           }
        }
     })
  });
});

//아이디 중복확인(업체)
$(function(){
  $('#businessid').keyup(function(){
     $('#b_businessidcheck').show();
     $('#b_checkedbusinessid').hide();
  });
  $('#b_businessidcheck').click(function(){
	 var bengid = /^[a-zA-Z0-9]*$/;
     var businessid =$('#businessid').val();
     var checkBid = document.getElementById("businessid");
     $.ajax({
        type : "post",
        url : "/businessid",
        data : {businessid:businessid},
        success:function(data,textStatus){
           if(data=="true"){
              alert("이미 사용중인 아이디입니다");
           }else if (businessid==''){
              alert("아이디를 입력해주세요");
           }else if(checkBid.value.length >= 10){
        	   alert("아이디가 너무 길어요 10자 이하로 해주세요.");
           }else if(checkBid.value.search(" ") != -1 ){ 
        	   alert("공백이 포함되면 안됩니다.");
           }else if(!bengid.test(businessid)){
			   alert("영문+숫자 조합만 가능합니다.");
		   }else{
              alert("사용 가능한 아이디입니다.");
               $('#b_businessidcheck').hide();
               $('#b_checkedbusinessid').show();
           }
        }
     })
  });
});


//닉네임 중복확인(개인)
$(function(){
	$('#nickname').keyup(function(){
		$('#nicknamecheck').show();
		$('#checkednickname').hide();
	});
	
   $('#nicknamecheck').click(function(){
	  var checknick = document.getElementsByName("check_nick");
      var nickname =$('#nickname').val(); //사용자가 입력한 id값 
      var checkPname = document.getElementById("nickname");
      $.ajax({
         type : "post",
         url : "/nickname",
         data : {nickname:nickname},
         success:function(data,textStatus){
            if(data=="true"){
               alert("이미 사용중인 닉네임입니다.");
            }else if(nickname==''){
               alert("닉네임을 입력해주세요");
            }else if(checkPname.value.length >= 8){
         	   alert("닉네임이 너무 길어요 8자 이하로 해주세요.");
            }else if(checkPname.value.search(" ") != -1 ){ 
         	   alert("공백이 포함되면 안됩니다.");
            }else{
               alert("사용가능한 닉네임입니다.");
                $('#nicknamecheck').hide();
       			$('#checkednickname').show();
            }
         }
      })
   });
});

//상호명 중복확인(업체)
$(function(){
	$('#bname').keyup(function(){
		$('#brandnamecheck').show();
		$('#checkedbrandname').hide();
	});
   $('#brandnamecheck').click(function(){
      var bname =$('#bname').val(); //사용자가 입력한 id값 
      var checkBname = document.getElementById("bname");
      $.ajax({
         type : "post",
         url : "/bname",
         data : {bname:bname},
         success:function(data,textStatus){
            if(data=="true"){
               alert("이미 있는 상호명입니다.");
            }else if(bname==''){
               alert("상호명을 입력해주세요");
            }else if(checkBname.value.search(" ") != -1 ){ 
         	   alert("공백이 포함되면 안됩니다.");
            }else{
               alert("사용이 가능한 상호명입니다.");
                $('#brandnamecheck').hide();
       			$('#checkedbrandname').show();
            }
         }
      })
   });
});





//인증번호 5자리 번호 = 전송(개인)
$(function(){
	$('#phone').keyup(function(){
		$('#confirmBnt').hide();
		$('#checkedauthNumber').hide();
	});
$(function(){
	var check = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;	
	let authCode= $('#authCode');
	$('#confirmBnt').hide();
	$('#authNumber').hide();
	$('#authlabel').hide();
	$('#timer').hide();
	$(document).on('click','#goSMS', function () {		// 버튼을 클릭 했을 경우
		let phone = $('#phone').val();	// 사용자가 입력한 전화번호
		let authCode= $("#authCode");	//휴대폰 인증번호 담을 변수		
		// 사용자가 입력한 전화번호가 공백이 아니고, 8자리 이상일 경우
		if (phone != '' && phone.length == 11) {
		$('#confirmBnt').show();
		$('#authNumber').show();
		$('#authlabel').show();
		$('#timer').show();
		$("#phone").attr("readonly",true);        // readonly 처리
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
					alert('인증 번호가 발송 되었습니다. 유로기능이므로 다음 코드를 입력해주세요:'+authCode.val());
					startCountdown();
					}
				},
				error: function(response) {
					alert('인증번호 발송에 실패하였습니다.\n잠시 후 다시 시도해주시기 바랍니다.')
				}
			});
		}else if(!check.test(phone.value)){
			alert("휴대폰 번호를 제대로 입력해 주세요");
			return false;
		}else{
			alert("휴대폰 번호를 입력해 주세요");
			return false;
		} 
	});

 $("#txt").attr("readonly",true);        // readonly 처리
        
        $("#txt").removeAttr("readonly");       // readonly 삭제
	
	//인증번호 확인 (개인)
	let authNumbers = document.getElementById("authNumber");
	$(document).on('click', '#confirmBnt', function () {
			// 인증번호가 공백이 아니고 0 이상일 경우
			if (authNumbers.value !='' && parseInt(authNumbers.value)>0) {	// val()로 받으면 문자열이기 때문에 형변환
				if(authNumbers.value == authCode.val()){
					cnfCheck = true;
					alert('휴대폰 번호 인증이 완료되었습니다. 감사합니다.');
					$('#confirmBnt').hide();
					$('#timer').hide();
					$('#checkedauthNumber').show();
					$("#authNumber").attr("readonly",true);        // readonly 처리
					stopCountdown();
				} else {
					alert('인증번호가 일치하지 않습니다.');
				}
				// 인증번호를 입력하지 않았을 경우
			}else{alert("인증번호를 입력해 주세요"); cnfCheck = false;}
		});
	})
})
//개인 인증 타이머
var countdownTimer;
var countdownTime;
function startCountdown(){
  if(countdownTimer) clearInterval(countdownTimer);
  countdownTime = 180; //시간초
  countdownTimer = setInterval(function(){ //타이머 시작
    if(countdownTime>0){ //타이머가 진행중이라면
      var ct = new Date(null);
      ct.setSeconds(countdownTime);
      var rt = ct.toISOString().substr(14, 5); //시간을 표시하기 위한 변수정의  
      document.getElementById('timer').value=rt; //텍스트에 표시
      countdownTime -= 1; //1초씩 줄어든다.
    }else { //타이머가 끝났다면
      clearInterval(countdownTimer); //끝
      alert('입력시간이 끝났습니다. 본인인증 버튼을 다시 눌러주세요.'); 
      document.getElementById('timer').value="";//텍스트를 비운다.
    }
  },1000); //1초씩 줄어든다.
}	

function stopCountdown(){ //타이머 멈춤
  clearInterval(countdownTimer);
  document.getElementById('timer').value='';
}
//인증번호 5자리 번호 = 전송(업체)
$(function(){
	$('#buserPhoneNum').change(function(){
		$('#bconfirmBnt').hide();
		$('#bcheckedauthNumber').hide();

	});
$(function(){
	var check = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;
	let bnumber;
	let authCode= $('#bauthCode');
	$('#bconfirmBnt').hide();
	$('#bauthNumber').hide();
	$('#bauthlabel').hide();
	$('#timer1').hide();
	$(document).on('click','#bgoSMS', function () {		// 버튼을 클릭 했을 경우
		let bphone = $('#bphone').val();	// 사용자가 입력한 전화번호
		let authCode= $("#bauthCode");	//휴대폰 인증번호 담을 변수
		// 사용자가 입력한 전화번호가 공백이 아니고, 8자리 이상일 경우
		if (bphone != '' && bphone.length == 11) {
			$('#bconfirmBnt').show();
			$('#bauthNumber').show();
			$('#bauthlabel').show();
			$('#timer1').show();
			$("#bphone").attr("readonly",true);        // readonly 처리
			$.ajax({
				url: '/main/execute',	// 요청보낼 url
				method: 'get',
				data: {'phone': bphone},	// 사용자가 입력한 휴대폰번호 전송
				success: function (response) {					
					if(response=="true"){
						alert("동일번호로 가입할 수 있는 아이디는 최대 3개입니다.");
					}else{
					authCode.attr('value', response);	// authCode의 속성 value값을 인증번호로 설정
					console.log("input태그에 담긴 인증번호: " + authCode.val());	// 확인용
					alert('인증 번호가 발송 되었습니다. 유로기능이므로 다음 코드를 입력해주세요:'+authCode.val());
					startCountdown1();
					}
				},
				error: function(response) {
					alert('인증번호 발송에 실패하였습니다.\n잠시 후 다시 시도해주시기 바랍니다.')
				}
			});
		}else if(!check.test(bphone.value)){
			alert("휴대폰 번호를 제대로 입력해 주세요");
			return false;
		}else{
			alert("휴대폰 번호를 입력해 주세요");
			return false;
		} 
	});

	
	//인증번호 확인 (업체)
	let authNumbers = document.getElementById("bauthNumber");
	$(document).on('click', '#bconfirmBnt', function () {
			// 인증번호가 공백이 아니고 0 이상일 경우
			console.log("text에 사용자가 입력한 인증 번호 : " + authNumbers.value);
			console.log("coolsms에서 받아온 인증 번호 : " + authCode.val());
			if (authNumbers.value !='' && parseInt(authNumbers.value)>0) {	// val()로 받으면 문자열이기 때문에 형변환
				if(authNumbers.value == authCode.val()){
					cnfCheck = true;
					alert('휴대폰 번호 인증이 완료되었습니다. 감사합니다.');
					$('#bconfirmBnt').hide();
					$('#timer1').hide();
					$('#bcheckedauthNumber').show();
					$("#bauthNumber").attr("readonly",true);        // readonly 처리
					stopCountdown1();
				} else {
					alert('인증번호가 일치하지 않습니다.');
				}
				// 인증번호를 입력하지 않았을 경우
			}else{alert("인증번호를 입력 해 주세요"); cnfCheck = false;}
		});

	});
});

//인증번호 타이머 [업체]
var countdownTimer1;
var countdownTime1;
function startCountdown1(){
  if(countdownTimer1) clearInterval(countdownTimer1);
  countdownTime1 = 180; //시간초
  countdownTimer1 = setInterval(function(){ //타이머 시작
    if(countdownTime1>0){ //타이머가 진행중이라면
      var ct = new Date(null);
      ct.setSeconds(countdownTime1);
      var rt = ct.toISOString().substr(14, 5); //시간을 표시하기 위한 변수정의  
      document.getElementById('timer1').value=rt; //텍스트에 표시
      countdownTime1 -= 1; //1초씩 줄어든다.
    }else { //타이머가 끝났다면
      clearInterval(countdownTimer1); //끝
      alert('입력시간이 끝났습니다. 본인인증 버튼을 다시 눌러주세요.');
      document.getElementById('timer1').value="";//텍스트를 비운다.
    }
  },1000); //1초씩 줄어든다.
}	

function stopCountdown1(){ //타이머 멈춤
  clearInterval(countdownTimer1);
  document.getElementById('timer1').value='';
}


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
    }else if(checkPwd.value.length >= 10){
	   alert("비밀번호가 길어요.");
	}
	
}

//비밀번호 일치여부(업체)
function isSame1(){
	var password = document.getElementById('bpassword');
	var checkpass = document.getElementById('b_checkpassword');
	var checkPwd2 = document.getElementById("bpassword");
	var checkPwd3 = document.getElementById("b_checkpassword");
	
	if(document.getElementById('bpassword').value != '' && document.getElementById('b_checkpassword').value != ''){
		if(document.getElementById('bpassword').value == document.getElementById('b_checkpassword').value){
			document.getElementById('same1').innerHTML='비밀번호가 일치합니다.';
			document.getElementById('same1').style.color='blue';
		}else{
			document.getElementById('same1').innerHTML='비밀번호가 불일치합니다.';
			document.getElementById('same1').style.color='red';
		}
	}else if(checkPwd2.value.search(" ") != -1 && checkPwd3.value.search(" ") != -1 ){ 
 	   alert("공백이 포함되면 안됩니다.");
    }else if(checkPwd2.value.length >= 10){
	   alert("비밀번호가 길어요.");
	}
}

//주소 찾기
function findAddr(){
	new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            
            // 우편번호와 주소 정보를 해당 필드에 넣는다.
           
            if(roadAddr !== ''){
                document.getElementById("baddress").value = roadAddr;
                
            } 
        }
    }).open();
}

