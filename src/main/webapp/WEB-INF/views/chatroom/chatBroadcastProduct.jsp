<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://kit.fontawesome.com/5231ffc51c.js" crossorigin="anonymous"></script>
<title>채팅하기</title>
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
	<link href="<c:url value="/static/css/chatBroadcastProduct.css"/>" rel='stylesheet' />
	
</head>
<body>
	<div class="container">
		<div class="chat-header">
			<h1 class="chat-header__name">${chatRoomInfo.ititle}</h1>
        </div>
		<div class="row">
				<%--chatHistory와 member가 실시간 입력하는 메시지 출력 --%>
				
				<div id="content" class="chat-history">
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
									<span class="message__user-name" id="chatRoomSenderName">${chatRoom.senderName}</span><br>
									<span class="message__text" id="chatRoomContent">${chatRoom.content}</span><br>
									<i class="fa fa-clock-o"></i>
									<span class="message__time" id="chatRoomSendTime">${chatRoom.sendTime}</span><br>
								</div>
							</div>
						</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
				<%--메시지 입력창과 보내기 버튼 --%>
				<div class="row_3">
					<div class="input_group chat-controls" id="sendMessage">
						<input type="text" class="chat-controls__textarea" id="message" placeholder="메시지를 입력해주세요." />
						<div class="input_group_append">
							<button id="send" class="chat-controls-buttons__send" onclick="send()">보내기</button>
							<input type="hidden" value="${authUser.getNickname()}" id="buyerName"/>
							<input type="hidden" value="${authUser.getUserno()}" id="buyerno"/>
							<input type="hidden" value="${chatRoomInfo.ino}" id="ino"/>
							<input type="hidden" value="${chatRoomInfo.sellerno}" id="sellerno"/>
							<input type="hidden" value="${chatRoomInfo.sellerName}" id="sellerName"/>						
							<input type="hidden" value="${chatRoomInfo.chatno}" id="chatno"/>						
						</div>					
					</div>
				</div>					
		</div>	
	</div>
	
	<%-- STOMP와 sockjs webjars import --%>
	<script src="/webjars/stomp-websocket/2.3.3-1/stomp.js" type="text/javascript"></script>
	<script src="/webjars/sockjs-client/1.1.2/sockjs.js" type="text/javascript"></script>
	
	<script type="text/javascript">
	
		var stompClient = null;
		var buyerName = $('#buyerName').val();
		var buyerno = $('#buyerno').val();
		var ino = $('#ino').val();
		var sellerName = $('#sellerName').val();
		var sellerno = $('#sellerno').val();	
		var senderName = $('#buyerName').val();
		var chatno = $('#chatno').val();
		
		<%-- invoke when DOM(Documents Object Model; HTML(<head>, <body>...etc) is ready --%>
		$(document).ready(connect());
		$(document).ready(ajaxChatRead());
		
		function connect() {
			<%-- map URL using SockJS--%>
			console.log("connected");
			var socket = new SockJS('/broadcast');
			var url = '/user/' + chatno + '/queue/messages';
			<%-- webSocket 대신 SockJS을 사용하므로 Stomp.client()가 아닌 Stomp.over()를 사용함--%>
			stompClient = Stomp.over(socket);
			
			console.log("connect ajaxRead");
			
			<%-- connect(header, connectCallback(==연결에 성공하면 실행되는 메서드))--%>
			stompClient.connect({}, function() {
				
				console.log("connected STOMP");

				<%-- url: 채팅방 참여자들에게 공유되는 경로--%>
				<%-- callback(function()): 클라이언트가 서버(Controller broker로부터)로부터 메시지를 수신했을 때 실행 --%>
				stompClient.subscribe(url, function(output) {
					<%-- JSP <body>에 append할 메시지 contents--%>
					showBroadcastMessage(createTextNode(JSON.parse(output.body)));
				});
				}, 
					<%-- connect() 에러 발생 시 실행--%>
						function (err) {
							alert('error' + err);
			});

		};
		
		<%-- WebSocket broker 경로로 JSON형태 String 타입 메시지 데이터를 전송함 --%>
		function sendBroadcast(json) {
			
			stompClient.send("/app/broadcast", {}, JSON.stringify(json));
		}
		
		<%-- 보내기 버튼 클릭시 실행되는 메서드--%>
		function send() {
			var content = $('#message').val();
			sendBroadcast({
				'chatno': chatno,
				'buyerName': buyerName, 
				'content': content,
				'sellerName': sellerName,
				'buyerno': buyerno, 
				'sellerno': sellerno,
				'ino': ino,
				'senderName': senderName,
				'senderId': buyerno
				});
			$('#message').val("");
		}
		
		<%-- 메시지 입력 창에서 Enter키가 보내기와 연동되도록 설정 --%>
		var inputMessage = document.getElementById('message'); 
		inputMessage.addEventListener('keyup', function enterSend(event) {
			
			if (event.keyCode === null) {
				event.preventDefault();
			}
			
			if (event.keyCode === 13) {
				send();
			}
		});
		
		<%-- 입력한 메시지를 HTML 형태로 가공 --%>
		function createTextNode(messageObj) {
			console.log("createTextNode");
			if(messageObj.senderName==sellerName) {
				return '<div class="chat-item chat-item-other"><img class="chat-item__img" src="https://raw.githubusercontent.com/heysafronov/mangosteen-chat/master/src/assets/img/matthew.png">'+
	            '<div class="message"><span class="message__user-name" id="chatRoomSellerName">'+ 
	            messageObj.senderName + 
	            '</span><br><span class="message__text" id="chatRoomContent">' + 
	            messageObj.content+
	            '</span><br><i class="fa fa-clock-o" aria-hidden="true"></i><span class="message__time" id="chatRoomSendTime"> [' +
	            messageObj.sendTime +
				'</span><br></div></div>';
			} else{
				return '<div class="chat-item chat-item-me"><div class="message"><span class="message__user-name" id="chatRoomSenderName">' +
	            messageObj.senderName +
	            '</span><br><span class="message__text" id="chatRoomContent">' +
	            messageObj.content+
	            '</span><br><i class="fa fa-clock-o"></i><span class="message__time" id="chatRoomSendTime"> [' +
	            messageObj.sendTime +
	            ']</span><br></div><img class="chat-item__img" src="https://raw.githubusercontent.com/heysafronov/mangosteen-chat/master/src/assets/img/kristy.png" alt="avatar"></div>';
			}
        }
		
		<%-- HTML 형태의 메시지를 화면에 출력해줌 --%>
		<%-- 해당되는 id 태그의 모든 하위 내용들을 message가 추가된 내용으로 갱신해줌 --%>
		function showBroadcastMessage(message) {
			$("#content").html($("#content").html() + message);
        }
		

		
		<%-- 읽음처리 --%>
		function ajaxChatRead() {

			
			$.ajax({
				url:'/chatread/product/ajax',
				type: 'POST',
				data: JSON.stringify({
					chatno: chatno,
					buyerno: buyerno
				}),
				dataType: 'json',
				contentType: 'application/json'
			})

		}

	
	</script>
</body>
</html>