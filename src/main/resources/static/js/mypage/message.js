$(function () {
	/* 쪽지함 위 nav 숨김 */
	$(".my_Tab").hide();
	
	var sect = $("#recv option:selected").val();
		console.log(sect);
		if(sect == "receive") {
			$("#sendtable").css('display','none');
			$("#recvtable").css('display','table');
			
		} else {
			$("#recvtable").css('display','none');
			$("#sendtable").css('display','table');
		}
		
	$("#recv").change(function() {
		var sect = $("#recv option:selected").val();
		console.log(sect);
		if(sect == "receive") {
			$("#sendtable").css('display','none');
			$("#recvtable").css('display','table');
			
			$(".spaging").css('display','none');
			$(".rpaging").css('display','table');
			
		} else {
			$("#recvtable").css('display','none');
			$("#sendtable").css('display','table');
			
			$(".rpaging").css('display','none');
			$(".spaging").css('display','table');
		}
	})
	
	/* 첫 화면에서 전체선택 해제(default) */
	$("input[type='checkbox']").prop("checked", false);
	
	/* 전체 선택 */
	$("#checkhead").change(function() {
		console.log("answkd");
		if($("#checkhead").is(":checked")) {
			$(".form-check-input1").prop("checked", true);
		} else {
			$(".form-check-input1").prop("checked", false);
		}
	})

	$("#checkhead2").change(function() {
		console.log("바꿔~");
		if($("#checkhead2").is(":checked")) {
			console.log("바궈2");
			$(".form-check-input2").prop("checked", true);
		} else {
			$(".form-check-input2").prop("checked", false);
		}
	})
	
	/* 받은 쪽지 상세페이지 */
	$(".view-modal").click(function() {
		var index = $('.view-modal').index(this);
		var mno = $('.mno:eq('+index+')').val();
		$.ajax({
			type : "get",
			url : "/viewRecvMessage",
			data : {mno:mno},
			success : function(data) {
				console.log(data);
				var sendUser = data.nickname;
				var title = data.mtitle;
				var content = data.mcontent;
				$(".modalContent").empty();  
				let modalcontext = "";
				modalcontext += "<h5 class='mUser'>보낸 사람: "+ sendUser +"</h5>";
				modalcontext += "<div class='modaltitle'><label class="+"'mcontext'"+" for="+"'mtitle'"+">제목 </label><br> "+title+"</div>";
				modalcontext += "<div><label class='mcontext' for='mcontent'>내용 </label><br>"+content+"</div>";
				modalcontext += "<a href='#' class='modal__close'>&times;</a></div>";
				$(".modalContent").append(modalcontext);
			},
			error : function(err) {
				console.log(err);
			}
		});
	})
	
	/* 보낸 쪽지 상세페이지 */
	$(".view-modal2").click(function() {
		var index = $('.view-modal2').index(this);
		var mno = $('.mno2:eq('+index+')').val();
		$.ajax({
			type : "get",
			url : "/viewSendMessage",
			data : {mno:mno},
			success : function(data) {
				var recvUser = data.nickname;
				var title = data.mtitle;
				var content = data.mcontent;
				$(".modalContent2").empty();  
				let modalcontext = "";
				modalcontext += "<h5 class='mUser'>받는 사람: "+ recvUser +"</h5>";
				modalcontext += "<div class='modaltitle'><label class="+"'mcontext'"+" for="+"'mtitle'"+">제목 </label><br> "+title+"</div>";
				modalcontext += "<div><label class='mcontext' for='mcontent'>내용 </label><br>"+content+"</div>";
				modalcontext += "<a href='#' class='modal__close'>&times;</a></div>";
				$(".modalContent2").append(modalcontext);
			},
			error : function(err) {
				console.log(err);
			}
		});
	})
	
	/* 받은 쪽지 답장 보내기 */
	$(".letter_img").off().on('click', function() {
		var index = $('.letter_img').index(this);
		var sendUser = $('.sendUser:eq('+index+')').html();
		var sendUserno = $('.checkbox:eq('+index+') .sendUserno').val();
		var sendBno = $('.checkbox:eq('+index+') .sendBno').val();
		$("#messageform").empty();  
		let modalcontext = "";
		if(sendUserno != null) {
			modalcontext += "<input type='hidden' name='recvUserno' value='"+sendUserno+"'>";
		} else {
			modalcontext += "<input type='hidden' name='recvBno' value='"+sendBno+"'>";
		}
		modalcontext += "<h5>받는 사람: "+ sendUser +"</h5>";
		modalcontext += "<div class='modaltitle'><label class="+"'mcontext'"+" for="+"'mtitle'"+">제목 </label>";
		modalcontext += "<input type='text' class='form-control' name='mtitle' id='mtitle' required /><br></div>";
		modalcontext += "<div><label class='mcontext' for='mcontent'>내용 </label>";
		modalcontext += "<textarea class='form-control' rows='3' cols='50' name='mcontent' id='mcontent' required></textarea><br></div>";
		modalcontext += "<input type='submit' class='btn btn-warning center' value='보내기' />";		
		$("#messageform").append(modalcontext);
	})
	
	/* 쪽지 확인 */
	var submitcheck =$('#submitcheck').val();
	var muserno = $('#muserno').val();
	if(submitcheck == "true"){
		alert("메시지가 성공적으로 발송되었습니다.");
		window.location.href="redirect:/mypage/umessage/"+ muserno ;
	} else if(submitcheck =="false"){
		alert("메시지 발송에 실패하였습니다.");
	}
	
	/* 삭제 */
	$("#delete").click(function() {
		var sect = $("#recv option:selected").val();
		
		if(sect == "receive") {
			if($('.form-check-input1:checked').length == 0) {
				alert("삭제할 항목을 선택해주세요.");
			} else {
				var recv = document.querySelectorAll('input[name="option1"]:checked');
				recv.forEach((el) => {
					var mno = el.parentElement.parentElement.querySelector('.mno').value;
					$.ajax({
						type : "get",
						url : "/recvDelete",
						data : {mno:mno},
						success : function(data) {
							alert("삭제가 완료되었습니다.");
							location.reload();
						},
						error : function(err) {
							console.log(err);
						}
					});
				})
			}
		}
		
		if(sect == "send") {
			if($('.form-check-input2:checked').length == 0) {
				alert("삭제할 항목 선택해라");
			} else {
				var send = document.querySelectorAll('input[name="option2"]:checked');
				send.forEach((e) => {
					var mno = e.parentElement.parentElement.querySelector('.mno2').value;
					$.ajax({
						type : "get",
						url : "/sendDelete",
						data : {mno:mno},
						success : function(data) {
							alert("삭제가 완료되었습니다.");
							location.reload();

						},
						error : function(err) {
							console.log(err);
						}
					});
				})
			}
		}
	
	})
	
	/* select box 페이징 */
	var select = $("#selectValue");
	if(select.val()==1) {
		$("#recv option:eq(1)").attr("selected", "selected");
	}
	
	if($("#recv option:selected").val()=="send") {
		$("#recvtable").css('display','none');
		$("#sendtable").css('display','table');
		
		$(".rpaging").css('display','none');
		$(".spaging").css('display','table');
	} else {
		$("#recvtable").css('display','table');
		$("#sendtable").css('display','none');
		
		$(".rpaging").css('display','table');
		$(".spaging").css('display','none');
	}
	
	//답장 보내기 쪽지 유효성
	function valid(){
		var sname = document.getElementById("mtitle");
		var scontent = document.getElementById("mcontent");
		if(sname.value != ''){
			alert("제목을 입력해주세요.");
			return false;
		}
		
		if(scontent.value == ''){
			alert("내용을 입력해주세요.");
			return false;
		}
	
	}
})