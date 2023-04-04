<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<c:url value="/static/css/selectoption.css"/>" rel='stylesheet'/>
<link href="<c:url value="/static/css/free.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/sharing.css"/>"rel='stylesheet' />
<link href="<c:url value="/static/css/modal.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/individual.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/buyProduct.css"/>" rel='stylesheet' />
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<c:import url='/WEB-INF/views/mypage/usermypage.jsp' />
	<c:if test="${authUser.sect eq 'users' }">
		<c:if test="${authUser.userno eq users.userno }">
	<div class="my_Tab">
			<ul class="tabs">
				<li><a href ="/mypage/umypage/${users.userno }/sell">판매 상품</a></li>
				<li class="on"><a href ="/mypage/umypage/${users.userno }/buy">구매 상품</a></li>
				<li><a href ="/mypage/umypage/${users.userno }/apply">신청 목록</a></li>
				<li><a href ="/mypage/umypage/${users.userno }/review">거래 후기</a></li>
			</ul>
	</div>
	</c:if>
	</c:if>
	<div class="buyProductWrap">
		<div class="select" id="buyselect">
			<select class="buyProductSelect">
		    	<option value="개인 판매">개인 판매</option>
		    	<option value="무료 나눔">무료 나눔</option>
		    </select>
		</div>    
		<div id="cards" class="sellCards1">
	    	<div class="card-list">
			  <c:forEach var="buysell" items="${buysellList}">
			  	<div class="sellList">
			  			<input type="hidden" value="${buysell.ino}" class="selectedIno"/>
					     <a href="/sellView/${buysell.ino}" id="sellListCard">
						     <div class="card2 card" data-sno=${buysell.ino }>
						      <div class="individualCard-header">
					     	  	  ${buysell.idealType}
					     	  </div>
					          <div class="card-image">
					          	<c:if test="${'등록완료' != buysell.istatus}">
					          		<div class="individualStatus">${buysell.istatus }</div>
					          	</c:if>
					          	<c:choose>
					          		<c:when test="${empty buysell.ifileids }">
					          			<img src="/static/image/logo3.png" alt="로고">
					          		</c:when>
					          		<c:otherwise>
					              		<img src="/upload/${ buysell.ifileids}" alt="무료나눔 옷">
					          		</c:otherwise>
					          	</c:choose>
					          </div>
					          <div class="card-body">
					              <div class="priceAndDate"><span class="price">${buysell.price}원</span><span class="date">${buysell.regDate}</span></div>
					              <h2 class="sharingTitle">${buysell.ititle }</h2>
					              <div class="individualDealNick"><p><img src="/static/image/yellowuser.png">${buysell.nickname}</p></div>
					          </div>
					          <div class="card-footer">
					              <img src="/static/image/pin.png"><p>${buysell.iaddress}</p>
					          </div>
					      </div>
				     </a>
				     <c:if test="${'거래중' eq buysell.istatus}">
				     	<c:if test="${buysell.buyCompletedCount eq 0}">
				     		<div class="buttonbox">
		          				<button class="gray buttoncontent cancel3">거래 취소</button>
		          				<button class="buttoncontent complete3">거래 완료</button>
		          			</div>
		          		</c:if>
		          	</c:if>
		          	<c:if test="${'거래 완료' eq buysell.istatus}">
		          		<div class="buttonbox">
		          			<button class="buttoncontent review1">거래 후기</button>
		          		</div>
		          	</c:if>
			  	</div>
		 	 </c:forEach>
		    </div>
	    </div>
	    
	    <div id="demo-modal" class="firstmodal">
	      		<div class="modal__content" id="modal__content">
	   				<div class="stars">
	     				<button class="star1">1</button>
	     				<button class="star1">2</button>
	     				<button class="star1">3</button>
	     				<button class="star1">4</button>
	     				<button class="star1">5</button>
	     				<label for="content1">거래 후기</label>
					</div>
	   				<textarea name="content1" class="content1"  placeholder="거래 후기를 입력해 주세요."></textarea>
	   				<button id="reviewcomplete1"  class="buttoncontent">작성완료</button>	
					<a href="#" class="modal__close">&times;</a>
	      		</div>
	  	</div>
	    
	    <ul class="pagination sellPage1" id="buypropage">
			<c:choose>
				<c:when test="${bspageInfo.page<=1}">
					<li><a id="prev"><<</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="/mypage/umypage/${authUser.userno}/buy?bspage=${bspageInfo.page-1}" id="prev"><<</a></li>&nbsp;
				</c:otherwise>
			</c:choose>
			<c:forEach var="i" begin="${bspageInfo.startPage }" end="${bspageInfo.endPage }">
				<c:choose>
					<c:when test="${bspageInfo.page==i }"><li><a class="active">${i }</a></li></c:when>
					<c:otherwise>
						<li><a href="/mypage/umypage/${authUser.userno}/buy?bspage=${i}">${i }</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${bspageInfo.page>=bspageInfo.maxPage }">
					<li><a id="next">>></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="/mypage/umypage/${authUser.userno}/buy?bspage=${bspageInfo.page+1}" id="next">>></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	    
	    <div id="cards" class="sharingCards1">
	    	<div class="card-list">
			  <c:forEach var="buysharing" items="${buysharingList}">
			  <div class="sharingList">
			  <input type="hidden" value="${buysharing.sno}" class="selectedSno"/>
		     <a href="/sharingView/${buysharing.sno}" id="sellListCard">
			     <div class="card sharingcard2" data-sno=${buysharing.sno }>
			     	  <div class="sharingCard-header">
			     	  	${buysharing.sdealType}
			     	  </div>
			          <div class="card-image">
			          	<input type="hidden" name="sstatus" value="${buysharing.sstatus}">
			          	<c:if test="${'등록완료' != buysharing.sstatus}">
			          		<div class="sharingStatus">${buysharing.sstatus}</div>
			          	</c:if>
			          	<c:choose>
			          		<c:when test="${empty buysharing.sfileids }">
			          			<img src="/static/image/logo3.png" alt="로고">
			          		</c:when>
			          		<c:otherwise>
			              		<img src="/upload/${ buysharing.sfileids}" alt="무료나눔 옷">
			          		</c:otherwise>
			          	</c:choose>
			          </div>
			          <div class="card-body">
			              <span class="date">${buysharing.regDate}</span>
			              <h2 class="sharingTitle">${buysharing.stitle }</h2>
			              <div class="sharingDealNick"><p><img src="/static/image/yellowuser.png">${buysharing.nickname}</p></div>
			          </div>
			          <div class="card-footer">
			          	  <img src="/static/image/pin.png"><p>${buysharing.saddress}</p>
			          </div>
			      </div>
		     		</a>
		     		<c:if test="${'거래중' eq buysharing.sstatus}">
		     			<c:if test="${buysharing.buyCompletedCount eq 0}">
		     				<div class="buttonbox">
			          			<button class="gray buttoncontent cancel4">거래 취소</button>
			          			<button class="buttoncontent complete4">거래 완료</button>
			          		</div>	
		          		</c:if>
		          	</c:if>
		          	<c:if test="${'거래 완료' eq buysharing.sstatus}">
		          		<div class="buttonbox">
		          			<button class="buttoncontent review">거래 후기</button>
		          		</div>
		          	</c:if>
		     	 </div>
			  </c:forEach>
		    </div>
	    </div>
	    
	    <div id="demo-modal2" class="firstmodal">
	      		<div class="modal__content" id="modal__content">
	      				<label for="content"><strong>[거래 후기]</strong></label>
	      				<div class="stars">
		      				<button class="star">1</button>
		      				<button class="star">2</button>
		      				<button class="star">3</button>
		      				<button class="star">4</button>
		      				<button class="star">5</button>
		      			</div>
	      				<textarea name="content" class="content" placeholder="거래 후기를 입력해 주세요."></textarea>
	      				<button id="reviewcomplete" class="buttoncontent">작성완료</button>	
					<a href="#" class="modal__close">&times;</a>
	      		</div>
	  	</div>
	    
	    <ul class="pagination sharingPage1" id="buypropage">
			<c:choose>
				<c:when test="${ppageInfo.page<=1}">
					<li><a id="prev"><<</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="/mypage/umypage/${authUser.userno}/buy?ppage=${ppageInfo.page-1}" id="prev"><<</a></li>&nbsp;
				</c:otherwise>
			</c:choose>
			<c:forEach var="i" begin="${ppageInfo.startPage }" end="${ppageInfo.endPage }">
				<c:choose>
					<c:when test="${ppageInfo.page==i }"><li><a class="active">${i }</a></li></c:when>
					<c:otherwise>
						<li><a href="/mypage/umypage/${authUser.userno}/buy?ppage=${i}">${i }</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${ppageInfo.page>=ppageInfo.maxPage }">
					<li><a id="next">>></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="/mypage/umypage/${authUser.userno}/buy?ppage=${ppageInfo.page+1}" id="next">>></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
    <div>
		<c:import url='/WEB-INF/views/includes/footer.jsp' />
	</div>		
	<script>
		/* 개인판매 거래취소 */
		$('.cancel3').click(function(){
			var index = $('.sellList').index($(this).parent().parent());
			var ino = $(".card2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/cancelDeal",
				data : {ino:ino},
				success : function(data) {
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});
	
		})
		
		/* 무료나눔 거래취소 */
		$('.cancel4').click(function(){
			var index = $('.sharingList').index($(this).parent().parent());
			var sno = $(".sharingcard2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/cancelSharingDeal",
				data : {sno:sno},
				success : function(data) {
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});

		})
		
		/* 개인판매 거래완료 */
		$('.complete3').click(function(){
			var index = $('.sellList').index($(this).parent().parent());
			$(this).attr("disabled", true);
			var ino = $(".card2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/completeSeDeal",
				data : {ino:ino},
				success : function(data) {
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});

		})
		
		/* 무료나눔 거래완료 */
		$('.complete4').click(function(){
			var index = $('.sharingList').index($(this).parent().parent());
			$(this).attr("disabled", true);
			var sno = $(".sharingcard2:eq("+index+")").attr("data-sno");
			$.ajax({
				type : "get",
				url : "/completeShDeal",
				data : {sno:sno},
				success : function(data) {
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});

		})
		var star = 0;
		$(".star").mouseover(function(){
			for(var j=0;j<=4;j++){
				document.getElementsByClassName('star')[j].classList.remove('yellow');
			}
			var starindex = $(".star").index(this);
			for(var j=0;j<=starindex;j++){
				document.getElementsByClassName('star')[j].classList.add('yellow');
			}
			star = starindex + 1;
		})
		var star1 = 0;
		$(".star1").mouseover(function(){
			for(var j=0;j<=4;j++){
				document.getElementsByClassName('star1')[j].classList.remove('yellow');
			}
			var starindex = $(".star1").index(this);
			for(var j=0;j<=starindex;j++){
				document.getElementsByClassName('star1')[j].classList.add('yellow');
			}
			star1 = starindex+1;
		})
		
		/* 개인판매 거래후기 */
		var ino=0;
		$('.review1').click(function(){
			var reviewIndex = $('.sellList').index($(this).parent().parent());
			ino = $(".card2:eq("+reviewIndex+")").attr("data-sno");
			$('#demo-modal').css('visibility','visible');
			$('#demo-modal').css('opacity','1');
			$('#demo-modal').css('z-index','2');
		})
		
		$('.modal__close').click(function(e){
			e.preventDefault();
			$('#demo-modal').css('visibility','hidden');
			$('#demo-modal').css('opacity','0');
			$('#demo-modal').css('z-index','0');
			$('#demo-modal2').css('visibility','hidden');
			$('#demo-modal2').css('opacity','0');
			$('#demo-modal2').css('z-index','0');
		})
		
		$("#reviewcomplete1").click(function(){
			var content = $('.content1').val();
			$.ajax({
				type : "get",
				url : "/sendIReview",
				data : {star:star1,
						content:content,
						ino:ino},
				success : function(data) {
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});
		})
		
		/* 무료나눔 거래후기 */
		var sno=0;
		$('.review').click(function(){
			var reviewIndex = $('.sharingList').index($(this).parent().parent());
			sno = $(".sharingcard2:eq("+reviewIndex+")").attr("data-sno");
			$('#demo-modal2').css('visibility','visible');
			$('#demo-modal2').css('opacity','1');
			$('#demo-modal2').css('z-index','2');
		})
		
		$("#reviewcomplete").click(function(){
			var content = $('.content').val();
			$.ajax({
				type : "get",
				url : "/sendSReview",
				data : {star:star,
						content:content,
						sno:sno},
				success : function(data) {
					location.reload();
				},
				error : function(err) {
					console.log(err);
				}
			});
		})
		
		$('.sharingCards1').hide();
		$('.sharingPage1').hide();
		
		$(".buyProductSelect").change(function() {
			var sect = $(".buyProductSelect option:selected").val();
			if(sect == "개인 판매") {
				$('.sharingCards1').hide();
				$('.sharingPage1').hide();
				
				$('.sellCards1').show();
				$('.sellPage1').show();
				
			} else {
				$('.sellCards1').hide();
				$('.sellPage1').hide();
				
				$('.sharingCards1').show();
				$('.sharingPage1').show();
			}
		})
		var height = document.scrollHeight;
		$('.firstmodal').css('height',height);
	</script>
</body>
</html>