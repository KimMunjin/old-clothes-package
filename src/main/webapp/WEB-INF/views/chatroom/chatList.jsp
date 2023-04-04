<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script src="https://code.jquery.com/jquery-latest.js"></script>
<head>
<title>ChatList</title>
<link href="<c:url value="/static/css/chatList.css"/>" rel='stylesheet' />

</head>
<body>
	<input type="hidden" value="${authUser.userno }" id="chatuserno"/>	
	<div class="wrapper">
		<div class="chatHeader" id="chatHeader">
			<img id="greenchat" src="/static/image/greenyellowchat.png" />
        	<h5>채팅</h5>
		</div>
		<div id="chatContainer">
	        
      	</div>
	</div>
	 
	 <script type="text/javascript">
	 
	 //home.jsp에 있는 email 정보를 받아줌
	 	var chatuserno = document.getElementById('chatuserno').value;
	 //html(JSP)파일이 로드되면 바로 initialize() 메서드를 실행시킴
		$(document).ready(function (){
			initialize();
		})
		
		function initialize() {
			getChatList();
			getUnreadMessageInfo();
			unreadAlertInfinite();
		}
		 
		//async(비동기)로 일정간격 업뎃되지 않아도 되는 img 파일을 불러옴
		function getChatList() {
			console.log("getChatList inprocess");
			$.ajax({
				url: "/chatList/ajax",
				type: "POST",
				data: JSON.stringify({
					chatuserno: chatuserno
				}),
				contentType: "application/json",
				//전달을 성공했을때 Controller로부터 data를 return 받아 처리해주는 메서드	
				success: function(data) {
					 if(data.length<=15){
						 alert("채팅 내역이 없습니다.")
						 window.close();
					 }
					 var parsed = JSON.parse(data);
					 var length = parsed.chatList.length;
					 for (var i = 0; i < length; i++) {
						 //채팅방 갯수만큼 반복문을 돌면서 채팅방 틀(div, img 태그)를 만들어줌 
						 addChatDivImg(i);
					 }
				}
			});
		}
		
		 //async(비동기) 방식으로 일정간격 업데이트 되어야 하는 정보들(메세지 알림기능) 
		 function getUnreadMessageInfo() {

				 $.ajax({
					 url:"/chatUnreadMessageInfo/ajax",
					 type: "POST",
					 data: JSON.stringify({
						 chatuserno: chatuserno
					 }),
					 contentType: "application/json",
					 success: function(data) {
						 var parsed = JSON.parse(data);
						 var length = parsed.chatList.length;
						 
						 for (var i = 0; i < length; i++) {
							$('.wrapSellerTitle' + i).html('');
						 	addChatList(parsed.chatList[i].ino, parsed.chatList[i].buyerno, parsed.chatList[i].senderName, parsed.chatList[i].ititle, parsed.chatList[i].messageUnread, i);
						 }
					 }
			 });
		 }
	 	
		 
		 //1000milliseconds(==1초) 간격으로 getUnreadMessageInfo()를 실행시키는 반복 메서드
	 	function unreadAlertInfinite() {
	 		setInterval(() => {
	 			getUnreadMessageInfo();				
			}, 1000);
	 	}
	 	
	 	//일정 간격으로 업데이트된 데이터를 화면에 출력하는 메서드 됨
	 	function addChatList(ino, buyerno, senderName, ititle, messageUnread, idx) {
			
	 		var str =
	 		'<a href="/chatRoom/' + ino + '/' + buyerno + '">' +
	 				'<span id="titlename"><div id="title">' + ititle + '</div>' + 
	 				'<div id="sellerName">' +
	 					'<img id="usericon" src="/static/image/orangeuser.png">' + senderName +'&nbsp;'+
	 				'</div></span>' +
	 				'<span id="message">' + 
	 		messageUnread+'</span></a>';
	 		
	 		//HTML화면의 <div class="wrapSellerTitle0,1,...etc"> 하위에 str 변수를 추가해준다.		 		 
	 		 $('.wrapSellerTitle' + idx).append(str);
	 	} 
	 	
	 	//페이지가 로드되는 시점 한 번만 출력하면 되는 div, img를 출력하는 메서드
	 	function addChatDivImg(idx) {
	 			$(document.body).append('<div class="chatcontainer chatMessageInfo' + idx + '"><div class="sellertitle wrapSellerTitle' +
	 					idx +
	 					'"></div></div>');
	 	}
	 	

	 	
	 </script>
</body>
</html>