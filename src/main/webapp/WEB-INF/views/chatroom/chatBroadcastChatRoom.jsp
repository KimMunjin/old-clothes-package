<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>채팅하기</title>
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
		<link href="<c:url value="/static/css/chatBroadcastChatRoom.css"/>" rel='stylesheet' />
		
</head>
<body>
	<div class="container">
		<div class="chat-header">
			<div id="chatback">
				<img id="chatbackimg" src="/static/image/back.png">
			</div>
			<h1 class="chat-header__name">${ititle}</h1>
        </div>
		<div class="row">
			<div class="col_6">
				<div class="row_3">
					<div class="input_group">
						
					<!-- 
						<div class="btn_group">
							<button type="button" id="connect" class="btn btn-sm btn-outline-secondary" onclick="connect()">connect</button>
							<button type="button" id="disconnect" class="btn btn-sm btn-outline-secondary" onclick="disconnect()" disabled>disconnect</button>
						</div>
					 -->
						 
					</div>				
				</div>				
				<div class="col_6">
						<div id="content">
							<c:forEach var="chatRoom" items="${chatHistory}">
								<c:choose>
									<c:when test="${chatRoom.senderName eq authUser.nickname}">
										<div class="chat-item chat-item-me">
											<div class="message">
													<span class="message__user-name" id="chatRoomSenderName">${chatRoom.senderName}</span><br>
													<span class="message__text" id="chatRoomContent">${chatRoom.content}</span><br>
													<i class="fa fa-clock-o"></i>
													<span class="message__time" id="chatRoomSendTime">${chatRoom.sendTime}</span><br>
											</div>
											<img class="chat-item__img" src="https://raw.githubusercontent.com/heysafronov/mangosteen-chat/master/src/assets/img/kristy.png" alt="avatar">
										</div>
									</c:when>
								<c:otherwise>		
									<div class="chat-item chat-item-other">
										<img class="chat-item__img" src="https://raw.githubusercontent.com/heysafronov/mangosteen-chat/master/src/assets/img/matthew.png" alt="avatar">
										<div class="message">
											<span class="message__user-name" id="chatRoomSellerName">${chatRoom.senderName}</span><br>
											<span class="message__text" id="chatRoomContent">${chatRoom.content}</span><br>
											<i class="fa fa-clock-o"></i>
											<span class="message__time" id="chatRoomSendTime">${chatRoom.sendTime}</span><br>
										</div>
									</div>
								</c:otherwise>
								</c:choose>	
							</c:forEach>
						</div>
					<!-- 
					<div>
						<span class="float-right">
							<button id="clear" class="btn btn-primary" onclick="clearBroadcast()" style="display: none;">Clear</button>				
						</span>
					</div>
					-->
				</div>
				<div class="row_3">
					<div class="input_group chat-controls" id="sendMessage">
						<input type="text" class="chat-controls__textarea" id="message" placeholder="메시지를 입력해주세요." />
						<div class="input_group_append">
							<button id="send" class="chat-controls-buttons__send" onclick="send()">보내기</button>
							<input type="hidden" value="${authUser.getNickname()}" id="senderName"/>
							<input type="hidden" value="${authUser.getUserno()}" id="senderId"/>
							<input type="hidden" value="${chatno}" id="chatno"/>					
							<input type="hidden" value="${ino}" id="ino"/>					
							<input type="hidden" value="${buyerno}" id="buyerno"/>					
							<input type="hidden" value="${sellerno}" id="sellerno"/>
							<input type="hidden" value="${buyername}" id="buyerName"/>					
							<input type="hidden" value="${sellername}" id="sellerName"/>					
						</div>					
					</div>				
				</div>
			</div>
		</div>
	</div>
	
	<script src="/webjars/stomp-websocket/2.3.3-1/stomp.js" type="text/javascript"></script>
	<script src="/webjars/sockjs-client/1.1.2/sockjs.js" type="text/javascript"></script>
	
	<script type="text/javascript">
	
		var stompClient = null;
		var senderName = $('#senderName').val();
		var senderId = $('#senderId').val();
		var sellerno = $('#sellerno').val();
		var buyerno = $('#buyerno').val();
		var buyerName = $('#buyerName').val();
		var sellerName = $('#sellerName').val();
		var ino = $('#ino').val();
		var chatno = $('#chatno').val();
		
/*
		function setConnected(connected) {		
			$('#connect').prop('disabled', connected);
			$('#disconnect').prop('disabled', !connected);
			if (connected) {
				$('#sendMessage').show();
			} else {
				$('#sendMessage').hide();				
			}
		};
*/
		
		$(document).ready(connect());
		
	//send를 통해 메시지를 전송했을 때 broker를 거쳐 유저에게 전달
	//메시지를 다이렉트로 전달 받는 경로는 subscribe로 설정
		function connect() {
			var socket = new SockJS('/broadcast');
			var url = '/user/' + chatno + '/queue/messages';
			stompClient = Stomp.over(socket);
			ajaxChatRead(chatno, senderName);

			
			stompClient.connect({}, function() {
				stompClient.subscribe(url, function(output) {
					console.log("broadcastMessage working");
					showBroadcastMessage(createTextNode(JSON.parse(output.body)));
				});
						//setConnected(true);				
				}, 
						function (err) {
							alert('error' + err);
			});

		};
		
/*
		function disconnect() {
			
			if(stompClient!= null) {
				
				stompClient.disconnect(function() {
					console.log('disconnected...');
					setConnected(false);
				});
			}
		}
*/
		
		
		
		function sendBroadcast(json) {
			
			stompClient.send("/app/broadcast", {}, JSON.stringify(json));
		}
		
		
		function send() {
			//ajaxChatRoom();
			var content = $('#message').val();
			sendBroadcast({
				'chatno': chatno,
				'senderName': senderName,
				'content': content,
				'ino': ino,
				'buyerno': buyerno,
				'senderId': senderId,
				'sellerno': sellerno,
				'sellerName': sellerName,
				'buyerName': buyerName 
				});
			$('#message').val("");
		}
		
		var inputMessage = document.getElementById('message'); 
		inputMessage.addEventListener('keyup', function enterSend(event) {
			
			if (event.keyCode === null) {
				event.preventDefault();
			}
			
			if (event.keyCode === 13) {
				send();
			}
		});
		
		function createTextNode(messageObj) {
			console.log("createTextNode");
			if(messageObj.senderName==senderName) {
				return '<div class="chat-item chat-item-me"><div class="message"><span class="message__user-name" id="chatRoomSenderName">' +
				messageObj.senderName +
	            '</span><br><span class="message__text" id="chatRoomContent">' +
	            messageObj.content+
	            '</span><br><i class="fa fa-clock-o"></i><span class="message__time" id="chatRoomSendTime"> [' +
	            messageObj.sendTime +
	            ']</span><br></div><img class="chat-item__img" src="https://raw.githubusercontent.com/heysafronov/mangosteen-chat/master/src/assets/img/kristy.png" alt="avatar"></div>';
			} else {
				return '<div class="chat-item chat-item-other"><img class="chat-item__img" src="https://raw.githubusercontent.com/heysafronov/mangosteen-chat/master/src/assets/img/matthew.png">'+
	            '<div class="message"><span class="message__user-name" id="chatRoomSellerName">'+ 
	            messageObj.senderName + 
	            '</span><br><span class="message__text" id="chatRoomContent">' + 
	            messageObj.content+
	            '</span><br><i class="fa fa-clock-o" aria-hidden="true"></i><span class="message__time" id="chatRoomSendTime"> [' +
	            messageObj.sendTime +
				'</span><br></div></div>';
				
			}
       
		}
		
		function showBroadcastMessage(message) {
            $("#content").html($("#content").html() + message);
        }
		
		function clearBroadcast() {
			$('#content').html("");
		}
		

		
		<%-- 읽음처리 --%>
		function ajaxChatRead(chatno, reader) {
			console.log("ajaxChatread");
			var flag = "";
			if (reader == buyerName) {
				flag = "buy";
			} else {
				flag = "sell";
			}
			$.ajax({
				url:'/chatread/chatroom/ajax',
				type: 'POST',
				data: JSON.stringify({
					chatno: chatno,
					flag: flag
				}),
				dataType: 'json',
				contentType: 'application/json'
			})
		}
	$('#chatback').click(function(){
		location.replace("http://13.125.101.12:8088/chatList")
	})
	</script>
</body>
</html>