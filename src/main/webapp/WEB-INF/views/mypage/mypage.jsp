<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>나의 옷장</title>
<link href="<c:url value="/static/css/mypage.css"/>" rel='stylesheet' />
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<link href="<c:url value="/static/css/review.css"/>" rel='stylesheet' />
</head>
<body>
	<div>
		<c:import url='/WEB-INF/views/includes/header.jsp' />
	</div>
	<div class="closet_wrap" >  
    
    <div class="title">
 
      <div class="nickname">
      <c:if test="${authUser.sect eq 'users' }">
      		<input type="hidden" value="${authUser.userno }" id="chatuserno"/>
      </c:if>
      
      
      <c:choose>    
	    	<c:when test="${users.sect eq 'users' }">
	    			<strong>${users.nickname }의 옷장</strong>
	    	</c:when>
      <c:otherwise>
      				<strong>${business.bname }의 옷장</strong>
      </c:otherwise>
      </c:choose>            
       
      </div>   
      <div class="fname_closet">
      			<c:if test="${authUser.sect eq 'users' }">
      				<c:if test = "${authUser.userno eq users.userno }">
				        <a href="/mypage/likelist/${authUser.userno }">
					      <img id="like" src="/static/image/redheart.png" alt="찜" title="나의 찜목록">
				        </a>
		        	</c:if>
		        </c:if>   
			        <c:if test="${authUser.sect eq 'users' }">
	      				<c:if test = "${authUser.userno eq users.userno }">
					        <a href="/mypage/umessage/${authUser.userno }">
					          <input type="hidden" id="muserno" name="muserno" value="${users.userno }">
						      <img id="letter" src="/static/image/letter.png" alt="쪽지" title="나의 쪽지함">
					        </a>
				        </c:if>
			        </c:if>
			        <c:if test="${authUser.sect eq 'business' }">
			        	<c:if test="${authUser.bno eq business.bno }">
					        <a href="/mypage/bmessage/${authUser.bno }">
					         <input type="hidden" id="muserno" name="muserno" value="${business.bno }">
						      <img id="letter" src="/static/image/letter.png" alt="쪽지" title="나의 쪽지함">
					        </a>			        		
			        	</c:if>
			        </c:if>	
		        <c:if test="${authUser.sect eq 'users'}">
		        	<c:if test="${users.userno eq authUser.userno}">          
		      			<a href="javascript:openDetail('/chatList')">
							<img src="/static/image/blackchat.png" id="chaticon_img" alt="채팅" title="나의 채팅방">
								<span class="nav__notification nbadge" id="messageAlert" style="display:none;"></span>
						</a>
					</c:if>	
				</c:if>
				<c:if test="${authUser.sect eq 'users' }">	
					<c:if test="${authUser.userno eq users.userno }">			
			        <a href="/passcheck">
	              	  <img id="prosetting" src="/static/image/profilesetting.png" alt="프로필수정" title="회원정보 수정">
			        </a>
			        </c:if>
		        </c:if>
		        
		        <c:if test="${authUser.sect eq 'business' }">	
					<c:if test="${authUser.bno eq business.bno }">			
			        <a href="/passcheck">
	              	  <img id="prosetting" src="/static/image/profilesetting.png" alt="프로필수정" title="회원정보 수정">
			        </a>
			        </c:if>
		        </c:if>
        </div>
      </div>        	    
  	

	<div class="self_Introduction">
		<div class="closetImage">
			<img src="/static/image/closet.png" alt="옷장">
    	</div>
    <div class="second">
		  <div class= "myRecord">
		  <c:choose>
		 	 <c:when test="${users.sect eq 'users' }">		  
			  <span>상품등록 : ${totalcount } 개</span>
			  <span>거래완료 : ${statuscount } 건</span>
			  <span>받은 거래후기 : ${reviewcount }개</span>
			 </c:when>
			 <c:otherwise>
			 	<c:choose>
			 		<c:when test="${!empty authUser}">
			 			<c:choose>
			 				<c:when test="${authUser.sect eq 'users'}">
					 			<span>신청목록 : ${applycount} 개</span>
								<span>수거완료 : ${ccount} 개</span>
								<span>수거거절: ${bcount} 개</span>
								<span>거래후기 : ${acount} 개</span>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${authUser.bno eq business.bno}">
										<span>신청목록 : ${applycount} 개</span>
										<span>수거신청 : ${dcount} 개</span>
										<span>수거중 : ${ecount} 개</span>
										<span>신청거절 : ${bcount} 개</span>
									</c:when>
									<c:otherwise>
										<span>신청목록 : ${applycount} 개</span>
										<span>수거완료 : ${ccount} 개</span>
										<span>수거거절: ${bcount} 개</span>
										<span>거래후기 : ${acount} 개</span>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
			 		</c:when>
			 		<c:otherwise>
			 			<span>신청목록 : ${applycount} 개</span>
						<span>수거완료 : ${ccount} 개</span>
						<span>수거거절 : ${bcount} 개</span>
						<span>거래후기 : ${acount} 개</span>
			 		</c:otherwise>	
			 
			 	</c:choose>
			 </c:otherwise>
			</c:choose>	   
      	  
		  </div>		

		  <div class="introduce" id="introduce">
		  	 		
		  		<c:if test="${authUser.sect eq 'users'}">
			  		<c:if test="${users.userno eq authUser.userno }">			 
						<img id="setting" src="/static/image/setting1.png" alt="소개수정" >
					</c:if>
				</c:if>
				<c:if test="${authUser.sect eq 'business'}">
					<c:if test="${business.bno eq authUser.bno}">			 
						<img id="setting" src="/static/image/setting1.png" alt="소개수정" >
					</c:if>
				</c:if>
				<c:choose>
				
				<c:when test="${users.introduce eq null && business.bintroduce eq null}">
					<c:choose>
						<c:when test="${users.sect eq 'users' }">
							<p> 안녕하세요 ,  ${users.nickname }의 옷장입니다. </p>
						</c:when>
						<c:otherwise>						
							<p> 안녕하세요 ,  ${business.bname }의 옷장입니다. </p>				
						</c:otherwise>
					</c:choose>
				</c:when>
				
				<c:otherwise>					
						<c:if test="${users.sect eq 'users'}">											
								<p>${users.introduce }</p>
						</c:if >
						<c:if test="${business.sect eq 'business'}"> 	
							<p>${business.bintroduce}</p>
						</c:if>
				</c:otherwise>										
			</c:choose>
			
			
		  </div>	
		<div class="intro_check">  
	  			<c:if test="${authUser.sect eq 'users'}">
	  				<c:if test="${users.userno eq authUser.userno}">
						<textarea class="intro_text">${users.introduce }</textarea>
					</c:if>
				</c:if>
				<c:if test="${authUser.sect eq 'business'}">	
					<c:if test="${ business.bno eq authUser.bno}">
						<textarea class="intro_text">${business.bintroduce }</textarea>
					</c:if>
				</c:if>		

					<button class="intro_btn">확인</button>
			  </div>
		</div>
  	</div>
  	</div>
  		
 <script>
  		
   	 $('.intro_check').css('display','none');   	 
   	 $('#setting').click(function(){ //설정버튼 클릭했을 때
			$(this).css('display','none');
			$('.introduce').css('display','none');
			$('.intro_check').css('display','flex');
	  });
	  
	  	$('.intro_btn').click(function(){
	  		var introduce = $('.intro_text').val();
	  		
	  		$.ajax({
				type : "post",
				url : "/mypage",
				data : {introduce:introduce},				
				success : function(data) {
					var text ="";					
					text += '<img id="setting" src="/static/image/setting1.png" alt="소개수정" >';
					text += "<p>"+data+"</p>";
					document.getElementById('introduce').innerHTML = text;	
					 location.reload();
					//$('#introduce').html(data);
								
				},
				error : function(err) {
					console.log(err);
				}
			});	  		
	  	});	  		 
	  	
	  	$('.intro_btn').click(function(){
  			$('.introduce').css('display','block');
			$('.intro_check').css('display','none');
			$('#setting').css('display','block');			
  		});
	  	
	 /* 마이페이지에서 채팅 */
	 // 채팅 아이콘 클릭했을 때 새 창으로
	 function openDetail(url) {
			var width = 470;
			var height = 650;
			//var left = (window.screen.width - width) / 2;
			//var top = (window.screen.height - height) / 2;
			window.open(url, 'detail', 'width=' + width +', height='+height +', location=no, status=no, scrollbar=yes');
		} 
	 
		var chatuserno = document.getElementById('chatuserno').value;

		$(document).ready(function() {
		
			if (chatuserno != null) {
				getUnread();
				getInfiniteUnread();
			}
		});
	 // 안읽은 채팅방 개수
	 function getUnread() {
			$.ajax({
				url: "/chatUnreadAlert/ajax",
				type: "POST",
				data: JSON.stringify({
					chatuserno: chatuserno
				}) ,
				dataType: "json",
				contentType: "application/json",
				success: function(result) {
					if (result >= 1) {
						showUnread(result);
						$("#messageAlert").show();
					} else {
						$("#messageAlert").css('display','none');
						showUnread('');
					}
				}
			});
		}
		
		function getInfiniteUnread() {
			setInterval(() => {
				getUnread();
			}, 1000);
		}
		
		function showUnread(result) {
			$('#messageAlert').html(result);
			/*if(result < 1) {
				$("#messageAlert").css('display','none');
			}else {
			}*/
		}
  </script>
<script src="<c:url value='/static/js/mypage/mypage.js'/>"></script>

</body>
</html>