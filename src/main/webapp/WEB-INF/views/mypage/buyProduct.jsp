<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<c:url value="/resources/css/modal.css"/>" rel='stylesheet' />
<link href="<c:url value="/resources/css/buyProduct.css"/>" rel='stylesheet' />
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<c:import url='/WEB-INF/views/mypage/usermypage.jsp' />
	<div class="my_Tab">
			<ul class="tabs">
				<li><a href ="/mypage/umypage/${users.userno }/sell">판매 상품</a></li>
				<li class="on"><a href ="/mypage/umypage/${users.userno }/buy">구매 상품</a></li>
				<li><a href ="#a">신청 목록</a></li>
				<li><a href ="/mypage/umypage/${users.userno }/review">거래 후기</a></li>
			</ul>
	</div>
	<select>
    	<option value="개인 판매">개인 판매</option>
    	<option value="무료 나눔">무료 나눔</option>
    </select>
	<div id="cards">
    	<div class="card-list">
		  <c:forEach var="buysell" items="${buysellList}">
		  	<div class="sellList">
				     <a href="/sellView/${buysell.ino}">
					     <div class="card2" data-sno=${buysell.ino }>
				          <div class="card-image">
				          	<c:if test="${'등록완료' != buysell.istatus}">
				          		<div class="individualStatus">${buysell.istatus }</div>
				          	</c:if>
				          	<c:choose>
				          		<c:when test="${empty buysell.ifileids }">
				          			<img src="/image/logo3.png" alt="로고">
				          		</c:when>
				          		<c:otherwise>
				              		<img src="/upload/${ buysell.ifileids}" alt="무료나눔 옷">
				          		</c:otherwise>
				          	</c:choose>
				          </div>
				          <div class="card-body">
				              <div class="priceAndDate"><span class="price">${buysell.price}원</span><span class="date">${buysell.regDate}</span></div>
				              <h2 class="sharingTitle">${buysell.ititle }</h2>
				              <p>${buysell.addressCity} ${buysell.addressTown }</p>
				          </div>
				          <c:choose>
				          	<c:when test="${empty buysell.addressCity}">
				          		<div class="card-footer">
				          			${buysell.idealType}
				          		</div>
				          	</c:when>
				          	<c:otherwise>
					          <div class="card-footer">
					              ${buysell.idealType} 
					          </div>
				          	</c:otherwise>
				          </c:choose>
				      </div>
			     </a>
			     <c:if test="${'거래중' eq buysell.istatus}">
	          		<button class="cancel3">거래 취소</button>
	          		<button class="complete3">거래 완료</button>
	          	</c:if>
	          	<c:if test="${'거래 완료' eq buysell.istatus}">
	          		<button class="review"><a href="#demo-modal">거래 후기</a></button>
	          	</c:if>
		  	</div>
	 	 </c:forEach>
	    </div>
    </div>
    
    <div id="demo-modal" class="firstmodal">
      		<div class="modal__content" id="modal__content">
      			<form action="smessage" method="post" id="messageform">
      				<input type="hidden" id="selectValue" value="${select }" />
      				<input type="hidden" id="submitcheck" value="${submitcheck}" />
      			</form>
				<a href="#" class="modal__close">&times;</a>
      		</div>
  	</div>
    
    <ul class="pagination">
		<c:choose>
			<c:when test="${bspageInfo.page<=1}">
				<li><a id="prev"><<</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="/mypage/umypage/${authUser.userno}?bspage=${bspageInfo.page-1}" id="prev"><<</a></li>&nbsp;
			</c:otherwise>
		</c:choose>
		<c:forEach var="i" begin="${bspageInfo.startPage }" end="${bspageInfo.endPage }">
			<c:choose>
				<c:when test="${bspageInfo.page==i }"><li><a class="active">${i }</a></li></c:when>
				<c:otherwise>
					<li><a href="/mypage/umypage/${authUser.userno}?bspage=${i}">${i }</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${bspageInfo.page>=bspageInfo.maxPage }">
				<li><a id="next">>></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="/mypage/umypage/${authUser.userno}?bspage=${bspageInfo.page+1}" id="next">>></a></li>
			</c:otherwise>
		</c:choose>
	</ul>
    
    <div id="cards">
    	<div class="card-list">
		  <c:forEach var="buysharing" items="${buysharingList}">
		  <div class="sellList">
	     <a href="/sharingView/${buysharing.sno}">
		     <div class="card sharingcard2" data-sno=${buysharing.sno }>
		          <div class="card-image">
		          	<input type="hidden" name="sstatus" value="${buysharing.sstatus}">
		          	<c:if test="${'등록완료' != buysharing.sstatus}">
		          		<div class="sharingStatus">${buysharing.sstatus}</div>
		          	</c:if>
		          	<c:choose>
		          		<c:when test="${empty buysharing.sfileids }">
		          			<img src="/image/logo3.png" alt="로고">
		          		</c:when>
		          		<c:otherwise>
		              		<img src="/upload/${ buysharing.sfileids}" alt="무료나눔 옷">
		          		</c:otherwise>
		          	</c:choose>
		          </div>
		          <div class="card-body">
		              <span class="date">${buysharing.regDate}</span>
		              <h2 class="sharingTitle">${buysharing.stitle }</h2>
		              <p>${buysharing.addressCity} ${buysharing.addressTown }</p>
		          </div>
		          <c:choose>
		          	<c:when test="${empty buysharing.addressCity}">
		          		<div class="card-footer">
		          			${buysharing.sdealType}
		          		</div>
		          	</c:when>
		          	<c:otherwise>
			          <div class="card-footer">
			              ${buysharing.sdealType} 
			          </div>
		          	</c:otherwise>
		          </c:choose>
		      </div>
	     		</a>
	     		<c:if test="${'거래중' eq buysharing.sstatus}">
	          		<button class="cancel4">거래 취소</button>
	          		<button class="complete4">거래 완료</button>
	          	</c:if>
	          	<c:if test="${'거래 완료' eq buysharing.sstatus}">
	          		<button class="review1"><a href="#demo-modal2">거래 후기</a></button>
	          	</c:if>
	     	 </div>
		  </c:forEach>
	    </div>
    </div>
    
    <div id="demo-modal2" class="firstmodal">
      		<div class="modal__content" id="modal__content">
      			<form action="smessage" method="post" id="messageform">
      				<div class="stars">
	      				<button class="star">1</button>
	      				<button class="star">2</button>
	      				<button class="star">3</button>
	      				<button class="star">4</button>
	      				<button class="star">5</button>
	      				<label for="content">거래 후기</label>
	      			</div>
      				<textarea name="content" class="content"></textarea>
      			</form>
				<a href="#" class="modal__close">&times;</a>
      		</div>
  	</div>
    
    <ul class="pagination">
		<c:choose>
			<c:when test="${ppageInfo.page<=1}">
				<li><a id="prev"><<</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="/mypage/umypage/${authUser.userno}?ppage=${ppageInfo.page-1}" id="prev"><<</a></li>&nbsp;
			</c:otherwise>
		</c:choose>
		<c:forEach var="i" begin="${ppageInfo.startPage }" end="${ppageInfo.endPage }">
			<c:choose>
				<c:when test="${ppageInfo.page==i }"><li><a class="active">${i }</a></li></c:when>
				<c:otherwise>
					<li><a href="/mypage/umypage/${authUser.userno}?ppage=${i}">${i }</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${ppageInfo.page>=ppageInfo.maxPage }">
				<li><a id="next">>></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="/mypage/umypage/${authUser.userno}?ppage=${ppageInfo.page+1}" id="next">>></a></li>
			</c:otherwise>
		</c:choose>
	</ul>
	<script>
		$('.cancel3').click(function(){
			var index = $(this).parent().index();
			var ino = $(".card2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/cancelDeal",
				data : {ino:ino},
				success : function(data) {
					console.log(data);
					console.log("성공");
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});
	
		})
		
		$('.cancel4').click(function(){
			var index = $(this).parent().index();
			var sno = $(".sharingcard2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/cancelSharingDeal",
				data : {sno:sno},
				success : function(data) {
					console.log(data);
					console.log("성공");
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});

		})
		
		$('.complete3').click(function(){
			var index = $(this).parent().index();
			$(this).attr("disabled", true);
			var ino = $(".card2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/completeDeal",
				data : {ino:ino},
				success : function(data) {
					console.log(data);
					console.log("성공");
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});

		})
		
		$('.complete4').click(function(){
			var index = $(this).parent().index();
			$(this).attr("disabled", true);
			var sno = $(".sharingcard2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/completeSharingDeal",
				data : {sno:sno},
				success : function(data) {
					console.log(data);
					console.log("성공");
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});

		})
		
		$(".star").mouseover(function(){
			for(var j=0;j<=4;j++){
				document.getElementsByClassName('star')[j].classList.remove('yellow');
			}
			var starindex = $(".star").index(this);
			for(var j=0;j<=starindex;j++){
				document.getElementsByClassName('star')[j].classList.add('yellow');
			}
		})
		
		
		
	</script>
</body>
</html>