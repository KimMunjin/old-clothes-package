<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<!-- Link Swiper's CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.css" />
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.js"></script>
<script src="https://kit.fontawesome.com/5231ffc51c.js" crossorigin="anonymous"></script>
<title>무료나눔 상세</title>
<link href="<c:url value="/static/css/sharing.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/modal.css"/>" rel='stylesheet' />
</head>
<body>
<header>
	<c:import url='/WEB-INF/views/includes/header.jsp' />
</header>
	<div id="viewcontainer">
		<div id="demo-modal" class="firstmodal">
      		<div class="modal__content">
      			<form action="smessage" method="post" id="messageform">
      				<input type="hidden" name="recvUserno" value="${uservo.userno }">
      				<input type="hidden" name="sno" value="${sharing.sno }">
      				<h5>받는 사람: ${uservo.nickname }</h5> 
	      			<div>
		      			<label class="mcontext" for="mtitle">제목 </label>
		      			<input type="text" class="form-control" name="mtitle" id="mtitle" /><br>
	      			</div>
	      			<div>
		      			<label class="mcontext" for="mcontent">내용 </label>
		      			<textarea class="form-control" rows="3" cols="50" name="mcontent" id="mcontent"> </textarea><br>
	      			</div>
	      			<input type="submit" class="btn btn-warning center" value="보내기" />
      			</form>
				<a href="#" class="modal__close">&times;</a>
      		</div>
  		</div>
    <section class="content_main">
      <section id="content_left">
			<div id="myModal" class="modal">
			  <span class="close">&times;</span>
			  <img class="modal-content" id="img01">
			  <div id="caption"></div>
			</div>
        <!-- Swiper -->
        <div class="swiper mySwiper">
          <div class="swiper-wrapper">
          	<c:choose>
          		<c:when test="${!empty files }">
					<c:forEach var="sfileids" items="${files }">
		            	<div class="swiper-slide">
		                	<img id="myImg" src="/upload/${sfileids}" alt="무료나눔 옷">
		        		</div>
		        	</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="swiper-slide">
						<img src="/static/image/logo3.png" alt="하우헌옷"/>
					</div>
				</c:otherwise>        	
        	</c:choose>           
          </div>
          <div class="swiper-button-next"></div>
          <div class="swiper-button-prev"></div>
          <div class="swiper-pagination"></div>
        </div>
      </section>
      <section id="content_right">
        <h4>${sharing.stitle}</h4>
        <input type="hidden" name="sno" id="sno" value="${sharing.sno }">
        <div class="letterAndHeart" id="sharingname">
          <img src="/static/image/yellowuser.png" class="userprofile"><span class="sharingnick">${sharing.sname }</span>
	        <c:choose>
	        	<c:when test="${empty authUser }">
	        		<div class="letterAndHeart">
		          		<img src="/static/image/letter.png" id="letter_img" alt="쪽지">
		          		<img src="/static/image/heart.png" id="heart_img" alt="찜신청전">
	        		</div>
	        	</c:when>
	        	<c:otherwise>
	        		<c:choose>
	        			<c:when test="${authUser.sect eq 'users' }">
	        				<c:if test="${authUser.userno ne sharing.userno }">
				          		<div class="letterAndHeart">	
					          		<a href="#demo-modal">
					          			<img src="/static/image/letter.png" id="letter_img" alt="쪽지">
					          		</a>
					          			<c:choose>
					          				<c:when test="${likes eq 1}">
					          					<img src="/static/image/redheart.png" id="heart_img" alt="찜신청후">
					          				</c:when>
					          				<c:otherwise>
					          					<img src="/static/image/heart.png" id="heart_img" alt="찜신청전">
			        						</c:otherwise>
			        					</c:choose>
			        			</div>
			        		</c:if>
	        			</c:when>
	        			<c:otherwise>
	        				<div class="letterAndHeart">	
				          		<a href="#demo-modal">
				          			<img src="/static/image/letter.png" id="letter_img" alt="쪽지">
				          		</a>
			          			<c:if test="${authUser.sect eq 'users' and authUser.userno ne sharing.userno}">
		          					<img src="/static/image/redheart.png" id="heart_img" alt="찜신청후">
		          					<img src="/static/image/heart.png" id="heart_img" alt="찜신청전">
			          			</c:if>		
			        		</div>
	        			</c:otherwise>
	        			
	        		</c:choose>
	        		
	        		
	         	</c:otherwise>
			</c:choose>
        </div>
        		<div class="applyfirst">
			        <span class="applyBox">신청 인원</span> 
			        <span class="applyCounts">${sharing.applycount } 명 </span>
		        </div>
		        <div id="sbtn">
					<c:choose>
						<c:when test="${empty authUser }">		        
				        	<a href="/mypage/umypage/${sharing.userno }/sell">
				        		<input type="button" id="openclothes" class="buttoncontent" value="옷장열기" />
				        	</a>
				        	<input type="button" id="wapply" class="buttoncontent" value="나눔신청" />
				        </c:when>
				        <c:otherwise>
					        <c:choose>
								<c:when test="${authUser.sect eq 'users' }">
			        				<c:if test="${authUser.userno ne sharing.userno }">	
			        					<a href="/mypage/umypage/${sharing.userno }/sell">
			        						<input type="button" id="openclothes" class="buttoncontent" value="옷장열기" />
			        					</a>
			        					<input type="button" id="wapply" class="buttoncontent" value="나눔신청" />
			        				</c:if>
			        				<c:if test="${authUser.userno eq sharing.userno }">
			        					<a href="/mypage/umypage/${authUser.userno}/sell">
			        						<input type="button" id="openclothes" class="buttoncontent" value="나의옷장" />
			        					</a>
			        					<c:if test="${fn:length(users) > 0}">
			        						<div class="sharingApplyList">
				        						<input type="button" class="buttoncontent" value="나눔 신청 목록" style="width:7em;" onclick="window.open('/sharingapplyList/${sharing.sno }', '_blank', 
                       'top=140, left=300, width=500, height=600, menubar=no, toolbar=no, location=no, directories=no, status=no, scrollbars=yes, copyhistory=no, resizable=no');">
					        				</div>
				        				</c:if>
			        				</c:if>
			        			</c:when>
			        			<c:otherwise>
			        				<a href="/mypage/umypage/${sharing.userno }/sell">
			        					<input type="button" id="openclothes" class="buttoncontent" value="옷장열기" />
			        				</a>
			        			</c:otherwise>
			        		</c:choose>	
		        		</c:otherwise>
		        	</c:choose>				
        		</div>
        <!-- Swiper JS -->
      </section>
    </section>
      <div class="scontent">
        <div id="modifydelete">
        	<h3>상품정보</h3>
        	<c:if test="${authUser.sect eq 'users'}">
	        	<c:if test="${authUser.userno eq sharing.userno}">    
		        	<div id="modifydelete">
						<a href="/sharingModifyForm?sno=${sharing.sno }">	        	
		          			<img src="/static/image/edit.png" id="edit" alt="수정">
	    	    		</a>
						<a href="javascript:void(0);" onclick="removeSharing();">	        	
		          			<img src="/static/image/viewdelete.png" id="viewdelete" alt="삭제">
	        			</a>
	        		</div>
	        	</c:if>	
	        </c:if>	
        </div>
        <div id=sdetail>${sharing.scontent}</div>
      </div>
            <%--댓글 리스트 --%>
            <!--  댓글  -->
            <div class="commentWrap">
	            <label class="blabel" for="content">댓글</label>
			    <input type="hidden" name="cno" id="cno" value="${comment.cno }">
	            <c:if test="${authUser ne null }">                 
	            	<c:choose>
	                <c:when test="${authUser.sect eq 'users' }">
						<input type="hidden" class="userno" name="userno" id="userno" value="${authUser.userno }">
					    <div class="commentContainer">				        
					        <form class="commentInsertForm" name="commentInsertForm" onsubmit="return check();">
					            <div class="commentBox">
				               	   <input type="hidden" name="sno" id="sno" value="${sharing.sno }">  
					               <div class="commentContent">
						               <input type="text" class="ccontent" id="ccontent" name="ccontent" placeholder="댓글을 작성해주세요.">
						               <div class="commentbtn">
						                    <button id="ubtn" class="buttoncontent" type="button" name="commentInsertBtn">등록</button>
						               </div>
					              </div>
					            </div>  
					        </form>
					    </div>
				    </c:when>
				    <c:otherwise>
					    <div class="commentContainer">				        
					        <form class="commentInsertForm" name="commentInsertForm">
					            <div class="commentBox">
				               	   <input type="hidden" name="sno" id="sno" value="${sharing.sno }">  
					               <div class="commentContent">
						               <input type="text" class="ccontent" id="ccontent" name="ccontent" placeholder="개인 회원으로 로그인 후 이용하시기 바랍니다." readonly>
						               <div class="commentbtn">
						                    <button id="bbtn" class="buttoncontent" type="button" name="bcommentInsertBtn">등록</button>
						               </div>
					               </div>
					              </div>
					        </form>
					    </div>
				    </c:otherwise>
				    </c:choose>
			    </c:if>
			 	<c:if test="${empty authUser }">
				    <div class="commentContainer">				        
				        <form class="commentInsertForm" name="commentInsertForm">
				            <div class="commentBox">
			               	   <input type="hidden" name="sno" id="sno" value="${sharing.sno }">  
				               <div class="commentContent">
								   <input type="text" class="ccontent" id="ccontent" name="ccontent" placeholder="로그인 후 댓글 작성이 가능합니다." readonly />
					               <div class="commentbtn">
					                    <button id="bbtn" class="buttoncontent" type="button" name="bcommentInsertBtn">등록</button>
					               </div>
				               </div>
				              </div>
				        </form>
				    </div>			 	
				</c:if>
			    <div class="commentContainer">
			        <div class="commentList">
	
			        </div>
			    </div>
			</div>			        
    
    
    <%--무료나눔 댓글 --%>
    <%--<div id="commentcontainer">
	    <label for="content" >Comment</label>
	    <br><br>
	    <input type="hidden" name="cno" id="cno" value="${comment.cno }">
	    <c:if test="${authUser.sect eq 'users' }">
					<input type="hidden" class="userno" name="userno" id="userno" value="${authUser.userno }">
		</c:if>
		<c:choose>
			<c:when test="${authUser.sect eq 'users' }">
			     <div class="commentcontainer">				        
			        <form name="commentInsertForm">
			            <div class="input-group">
			               <input type="hidden" name="sno" id="sno" value="${sharing.sno }">  
			               <input type="text" class="form-control" id="ccontent" name="ccontent" placeholder="댓글을 작성해주세요.">
			               <span class="input-group-btn">
			                    <button class="btn btn-default" type="button" name="commentInsertBtn">등록</button>
			               </span>
			              </div>
			        </form>
			    </div>
	    	</c:when>
		    <c:otherwise>
		    	<div class="commentcontainer">
		    		<form name="commentInsertForm">
		            	<div class="input-group">                
		               		<input type="text" class="form-control" id="ccontent" name="ccontent" placeholder="로그인 및 사용자로그인 후 가능합니다." readonly>              
		            	</div>
		        	</form>
		        </div>
		    </c:otherwise>
	    </c:choose>
	    <br><br> 
		    <div class="commentcontainer">
		        <div class="commentList"></div>
		    </div>
		</div>	 --%>	    
    </div>
    <div>
		<c:import url='/WEB-INF/views/includes/footer.jsp' />
	</div>	
<script>
/* 이미지 슬라이드 */
$(function() {	
	 var swiper = new Swiper(".mySwiper", {
	        cssMode: true,
	        navigation: {
	          nextEl: ".swiper-button-next",
	          prevEl: ".swiper-button-prev",
	        },
	        pagination: {
	          el: ".swiper-pagination",
	        },
	        mousewheel: true,
	        keyboard: true,
	      }); 
});

/* 사진 확대 기능(모달) */
//Get the modal
var modal = document.getElementById("myModal");

// Get the image and insert it inside the modal - use its "alt" text as a caption
var img = document.getElementById("myImg");
var modalImg = document.getElementById("img01");
var captionText = document.getElementById("caption");
if(img != null) {
	$(document).on("click","#myImg",function(){
		modal.style.display = "block";
		modalImg.src = this.src;
		captionText.innerHTML = this.alt;
	})
}
//var files = "${files[1]}";

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() { 
  modal.style.display = "none";
}

/* 찜 기능 */
$(function () {
	//로그인 확인
	$("#letter_img").on("click", function() {
		var logincheck = "<c:out value='${logincheck}'/>";
		if(logincheck == "false") {
			alert("로그인 후 이용해주세요.");
			location.href="/login";
		}
	})	
	$("#heart_img").on("click", function(e) {
		var logincheck = "${logincheck}";
		const sno =  $('#sno').val();
		if(logincheck == "false") {
			alert("로그인 후 이용해주세요.")
			location.href="/login";
		} else {
		}
		let sect = "${sect}";
		if(sect == 'users') {
			$.ajax({
				type: "post",
				url: "/sharingView/likes",
				data: {sno:sno},
				success: function(data) {
					if(data == 1) {
						$("#heart_img").attr("src", "/static/image/redheart.png");
					} else {
	                    $("#heart_img").attr("src", "/static/image/heart.png");
					}
				}, error: function() {
                    console.log('에러')
				}
			})
		}	
	})

});

/* 삭제 처리 */
function removeSharing() {
	var result = confirm("삭제하시겠습니까? 삭제 후 취소가 불가능합니다.");
	var sno =  $('#sno').val();
	if(result) {
		$.ajax({
			type : "post",
			url : "/sharingDelete",
			data : {sno:sno},
			success : function(data) {
				alert("삭제가 완료되었습니다.");
				location.href="/sharingList";
			},
			error : function(err) {
				console.log(err);
			}
		});
	}
	
}


/* 쪽지 확인 */
var submitcheck = "<c:out value='${submitcheck}'/>";
if(submitcheck == "true"){
	alert("메시지가 성공적으로 발송되었습니다.");
} else if(submitcheck =="false"){
	alert("메시지 발송에 실패하였습니다.");
} else{
}

/* 신청하기 */
$("#wapply").on("click", function() {
	var status = "${sharing.sstatus}";
	if(status != '등록완료'){
		alert(status+'이므로 신청할 수 없습니다.');
		return false;
	}
	var apply="";
	var logincheck = "<c:out value='${logincheck}'/>";
	const sno =  $('#sno').val();
	let sect = "${authUser.sect}";
	if(logincheck == "false") {
		alert("로그인 후 이용해주세요.");
		location.href="/login";
	} else if(sect == 'users'){
		$.ajax({
			type: "post",
			url: "/sharingView/wapply",
			data: {sno:sno},
			success: function(data) {
				apply = data;
				if(apply == "true") {
					alert("신청이 완료되었습니다.");
					location.reload(true);
				} else {
					alert("신청할 수 없습니다.");
				}
			}, error: function() {
                console.log('에러')
			}
		})
	

	}		
})

function valid() {
	var title = document.getElementById("mtitle");
	var content = document.getElementById("mcontent");
	if(title.value == "") {
		alert("내용을 입력해주세요.");
		return false;
	}
	if(content.value == "") {
		alert("내용을 입력해주세요.");
		return false;
	}
}

/* 댓글 */
var auth = "${authUser.sect}";
var userno = $('#userno').val();
var sno = $('#sno').val();
var cno = $('#cno').val();

//리스트 뿌려주기 
$(document).ready(function(){
	  commentList(); 
});
//사용자
$('[name=commentInsertBtn]').click(function(){ //댓글 등록 버튼 클릭시 
	    var insertData = $('[name=commentInsertForm]').serialize(); //commentInsertForm의 내용을 가져옴
	    if($('input[name=ccontent]').val() == '' ){
	    	alert("댓글을 입력해 주시기 바랍니다.");
	    	return false;
	    }
	    commentInsert(insertData); //Insert 함수호출(아래)
	});

//댓글 등록[사용자]
function commentInsert(insertData){
	 var userno = $('#userno').val();
	    $.ajax({
	        url : '/sharingView/'+sno+'/'+userno,
	        type : 'post',
	        data : insertData,
	        success : function(data){		            
	            commentList(); //댓글 작성 후 댓글 목록 reload
				$('[name=ccontent]').val('');
	            
	        }
	    });
	}


//댓글 목록 
function commentList(){
    $.ajax({
        url : '/Slist/'+sno,
        type : 'get',
        data : {'sno':sno},
        success : function(data){
            var a =''; 
            $.each(data, function(key, value){ 
                a += '<div class="commentArea">';
                
                //a += '<div class="commentInfo'+value.cno+'">'+'[ 작성자 ] : '+value.cname;
	            a += '<div id="writer" class="commentInfo'+value.cno+'">'+'<a href="/mypage/umypage/'+value.userno +'/sell" >'+ value.cname +'</a><span class="commentregdate">'+ value.regdate +'</span></div>';	 
                a += '<div class="commentContent'+value.cno+'"> <p id="contentss">'+value.ccontent +'</p>';
                if(auth != ''){
                	if(auth == 'users' && userno == value.userno){
		                 a += '<div class="commenta"><a onclick="commentUpdate('+value.cno+',\''+value.ccontent+'\');"> 수정 </a>';
		                 a += '<a onclick="commentDelete('+value.cno+');"> 삭제 </a> </div>';
                	}
                }
                a += '</div></div>';
            });
            
            $(".commentList").html(a);
        }
    });
}


//댓글 삭제 
function commentDelete(cno){
	var result = confirm("삭제하시겠습니까? 삭제 후 취소가 불가능합니다.");
	if(result){
	     $.ajax({
	         url : "/sharingcmtDelete/"+cno + "/" + sno,
	         type : "post",
	         success : function(data){
	        	 alert("삭제가 완료되었습니다.");
	        	 commentList(sno);
	         }
	     });
	}
}

//댓글 수정 - 댓글 내용 출력을 input 폼으로 변경 
function commentUpdate(cno, ccontent){
    var a ='';
    
    a += '<div id="commentModifyBox" class="commentBox"><div class="commentContent">';
    a += '<input type="text" class="ccontent" id="ccontent" name="ccontent_'+cno+'" value="'+ccontent+'"/>';
    a += '<div class="commentmodifybtn"><span class="commentbtn"><button id="ubtn" class="buttoncontent" type="button" onclick="commentUpdateProc('+cno+');">수정</button> </span>';
    a += '<span class="commentbtn commentbtn2"><button id="ubtn" class="buttoncontent cancelbtn" type="button" onclick="commentList();">취소</button> </span></div>';
    a += '</div></div>';
    
    $('.commentContent'+cno).html(a);
    
}
//댓글 수정
function commentUpdateProc(cno){
    var updateContent = $('[name=ccontent_'+cno+']').val();
    var result = confirm("수정하시겠습니까?");
    if(result){
	     $.ajax({
	         url : "/sharingcmtModify/"+cno + "/" +sno,
	         type : "post",
	         data : {'ccontent' : updateContent, 'cno' : cno},
	         success : function(data){
	            
	             alert("수정이 완료되었습니다.");
	             commentList(sno);
	         }
	     });
    }
}

</script>
</body>
</html>