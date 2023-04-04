<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>자유게시판 글보기</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    
	<link href="<c:url value="/static/css/free.css"/>" rel='stylesheet' /> 
	<style>
.deletecmt{
	position: relative;
    width: 42px;
    left: 1099px;
    bottom: 100px;
    height: 26px;
}

.viewcont {
    position: relative;
    height: auto;
    border-bottom: 2px solid navy;
}
.viewcont p {
	margin-left: 2em;
	height: auto;
}
#freeview {
	width: 1100px;
	margin: 0 auto;
	padding: 10px;
}
.buttoncontent {
    width: 6em;
    height: 2.5em;
    border: none;
    background: #5bc1ac;
    color: #fff;
    text-decoration: none;
    border-radius: 9px;
    font-size: 1em;
    margin: 5% 3%;
}
.buttoncontent:hover {
	opacity: 0.8;
}
.cccontent {
	display: block;
    padding: 0.375rem 0.75rem;
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.5;
    color: #212529;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid rgba(128, 128, 128, 0.2);
    appearance: none;
    border-radius: 0.375rem;
    transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out;
}
.commentmodifybtn {
    display: flex;
    justify-content: flex-end;
    align-items: center;
}
.commentbtn2 {
	margin-left: 7px;
}
.cancelbtn {
	background-color: var(--bs-gray-500);
}

</style>
</head>
<body>
	<div>
		<c:import url='/WEB-INF/views/includes/header.jsp' />
	</div>
    <div class="board_wrap">
		<section id="articleForm">
	        <div class="board_title">
	            <strong>자유게시판</strong>
	        </div>
	        <input type="hidden" id="page"value="${page }">
	        <input type="hidden" id="kwd"value="${kwd }">
	        <div class="bt_wrap">            
	            <a id="input4" href="/freeList?page=${page}&kwd=${kwd}" class="on">목록</a>
	            <c:if test="${authUser.sect eq 'users'}">                
	             <c:if test="${authUser.userno eq article.userno}">	                                  
	                <a id="freeModify" href="/modifyform/${article.fno }">수정</a>
	                <input id="freeDelete"  onclick="freeRemove()" type="submit" value="삭제">
	             </c:if>               
	            </c:if>
	            <c:if test="${authUser.sect eq 'business'}">                
	             <c:if test="${authUser.bno eq article.bno}">	                                  
	                <a href="/modifyform/${article.fno }">수정</a>
	                <input id="freeDelete"  onclick="freeRemove()" type="submit" value="삭제">
	             </c:if>               
	            </c:if>
	        </div>
	        <div class="board_view_wrap">
	            <div class="board_view">
	                  <div class="title">
	                   <section id="basicInfoArea">
	                   <dl class="dltitle">
	                        <dt class="dttitle">제목</dt>
	                        <dd id="ddtitleView" class="ddtitle">${article.ftitle }</dd>
	                    </dl>
	                         </section>
	                </div>
	                <div class="info">
	                    <dl class="dltitle">
	                        <dt id="dttitleView" class="dttitle">글번호</dt>
	                        <dd class="ddtitle">${article.fno }</dd>
	                    </dl>
	                    <dl class="dltitle">
	                        <dt class="dttitle">닉네임</dt>
	                        <dd class="ddtitle">${article.fname }</dd>
	                    </dl>
	                    <dl class="dltitle">
	                        <dt class="dttitle">작성일</dt>
	                        <dd class="ddtitle">${article.regdate }</dd>
	                    </dl>
	                    <dl class="dltitle">
	                        <dt class="dttitle">조회수</dt>
	                        <dd class="ddtitle">${article.freadcount}</dd>
	                    </dl>
	                </div>
	            </div>
	          </div>
		</section>
                <div class="viewcont" id="freeview">               
                  <p id="contentp">${article.fcontent }</p>                                                
                </div>                                
            <c:if test="${authUser.sect eq 'business' }">                            
				<input type="hidden" name="bno" id="bno" value="${authUser.bno }">				
			</c:if>                
			<c:if test="${authUser.sect eq 'users' }">
				<input type="hidden" class="userno" name="userno" id="userno" value="${authUser.userno }">
				
			</c:if>
			<input type="hidden" name="cno" id="cno" value="${comment.cno }">
			<input type="hidden" name="fno" id="fno" value="${article.fno }">   
			<input type="hidden" name="sect" id="sect" value="${authUser.sect }">            						

            <%--댓글 리스트 --%>
            <!--  댓글  -->
            <label class="blabel" for="content">댓글</label>
            <c:if test="${authUser ne null }">                 
			    <div class="commentContainer">				        
			        <form class="commentInsertForm" name="commentInsertForm" onsubmit="return check();">
			            <div class="commentBox">
			               <input type="hidden" name="fno" id="fno" value="${article.fno }">  
			               <div class="commentContent">
				               <input type="text" class="ccontent" id="ccontent" name="ccontent" placeholder="댓글을 작성해주세요.">
				               <div class="commentbtn">
				                    <button id="ubtn" class="buttoncontent" type="button" name="commentInsertBtn">등록</button>
				               </div>
			              </div>
			            </div>  
			        </form>
			    </div>
		    </c:if>
		    <c:if test="${empty authUser }">
				    <div class="commentContainer">				        
				        <form class="commentInsertForm" name="commentInsertForm">
				            <div class="commentBox">
			               	   <input type="hidden" name="fno" id="fno" value="${article.fno }">  
				               <div class="commentContent">
								   <input type="text" class="ccontent" id="ccontent" name="ccontent" placeholder="로그인 후 댓글 작성이 가능합니다." readonly />
					               <div class="commentbtn">
					                    <button id="bbtn" class="buttoncontent" type="button">등록</button>
					               </div>
				               </div>
				              </div>
				        </form>
				    </div>			 	
				</c:if>
            <%-- <c:if test="${authUser ne null }">                 
            	<c:choose>
                <c:when test="${authUser.sect eq 'users' }"> 	
				    <div class="commentContainer">				        
				        <form class="commentInsertForm" name="commentInsertForm" onsubmit="return check();">
				            <div class="commentBox">
				               <input type="hidden" name="fno" id="fno" value="${article.fno }">  
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
				               <input type="hidden" name="fno" id="fno" value="${article.fno }">  
				               <div class="commentContent">
					               <input type="text" class="ccontent" id="ccontent" name="ccontent" placeholder="댓글을 작성해주세요.">
					               <div class="commentbtn">
					                    <button id="bbtn" class="buttoncontent" type="button" name="bcommentInsertBtn">등록</button>
					               </div>
				               </div>
				              </div>
				        </form>
				    </div>
			    </c:otherwise>
			    </c:choose>
		    </c:if> --%>
		    <div class="commentContainer">
		        <div class="commentList">

		        </div>
		    </div>
    </div> 
    <div>
		<c:import url='/WEB-INF/views/includes/footer.jsp' />
	</div>	
            <%--댓글 수정창 --%>
            
   <script>   
   <%--댓글테스트--%>
   var fno = $('#fno').val();
   var auth = "${authUser.sect}";
   var userno = $('#userno').val();
   var bno = $('#bno').val();
   var cno = $('#cno').val();
  
 //리스트 미리 뿌려주기
   $(document).ready(function(){
	   windowopen=true;
	  commentList(); 
   });
   
   //사용자
   $('[name=commentInsertBtn]').click(function(){ //댓글 등록 버튼 클릭시 
	    var insertData = $('[name=commentInsertForm]').serialize(); //commentInsertForm의 내용을 가져옴
	    if($('input[name=ccontent]').val() == '' ){
	    	alert("댓글을 입력해 주시기 바랍니다.");
	    	return false;
	    }
	    var sect = $('#sect').val();
	    commentInsert(insertData, sect); //Insert 함수호출(아래)
	});
   
   /* //사업자
   $('[name=bcommentInsertBtn]').click(function(){ //댓글 등록 버튼 클릭시 
	    var insertData = $('[name=commentInsertForm]').serialize(); //commentInsertForm의 내용을 가져옴
	    if($('input[name=ccontent]').val() == '' ){
	    	alert("댓글을 입력해 주시기 바랍니다.");
	    	return false;
	    }
	    bcommentInsert(insertData); //Insert 함수호출(아래)
	}); */
   
	//댓글 등록 통합
	function commentInsert(insertData,sect){
		var num = "";
		if(sect=="users"){
			num = $('#userno').val();
		}else if(sect="business"){
			num = $('#bno').val();
		}
		$.ajax({
	        url : '/freeView/'+fno+'/'+num,
	        type : 'post',
	        data : insertData,
	        success : function(data){		            
	            commentList(); //댓글 작성 후 댓글 목록 reload
				$('[name=ccontent]').val('');
	            
	        }
	    });
		
	}
	
   
	 /* //댓글 등록[사용자]
	 function commentInsert(insertData){
		 var userno = $('#userno').val();
		 console.log("댓글 등록");
		    $.ajax({
		        url : '/ufreeView/'+fno+'/'+userno,
		        type : 'post',
		        data : insertData,
		        success : function(data){		            
		            commentList(); //댓글 작성 후 댓글 목록 reload
					$('[name=ccontent]').val('');
		            
		        }
		    });
		}
	 
	 //댓글 등록 [사업자]
	 function bcommentInsert(insertData){
		 console.log("댓글 등록");
		    $.ajax({
		        url : '/bfreeView/'+fno+'/'+bno,
		        type : 'post',
		        data : insertData,
		        success : function(data){
		            
		                commentList(); //댓글 작성 후 댓글 목록 reload
		                $('[name=ccontent]').val('');
		            
		        }
		    });
		} */
	 
	 //댓글 목록 
	 function commentList(){
	     $.ajax({
	         url : '/list/'+fno,
	         type : 'get',
	         data : {'fno':fno},
	         success : function(data){
	             var a =''; 
	             $.each(data, function(key, value){ 
	                 a += '<div class="commentArea" style="margin-left:'+(value.depth*3)+'%;">';

	                 if(value.csect =='users'){
	                	 a += '<div id="writer" class="commentInfo'+value.cno+'">'+'<a href="/mypage/umypage/'+value.userno +'/sell" >'+ value.cname +'</a><span class="commentregdate">'+ value.regdate +'</span></div>';	 
	                 }else if(value.csect == 'business'){
	                	 if(bno == value.bno){
	                 		a += '<div id="writer" class="commentInfo'+value.cno+'">'+'<a href="/mypage/bmypage/'+value.bno +'/apply">'+ value.cname +'</a><span class="commentregdate">'+ value.regdate +'</span></div>';
	                	 }else{
	                		 a += '<div id="writer" class="commentInfo'+value.cno+'">'+'<a href="/mypage/bmypage/'+value.bno +'/review">'+ value.cname +'</a><span class="commentregdate">'+ value.regdate +'</span></div>';
	                	 }
	                 }
	                 a += '<div class="commentContent'+value.cno+'"> <p id="contentss">'+value.ccontent +'</p>';
	                 if(auth != ''){
	                	 a += '<div class="commenta">'
	                	 a += '<a onclick="replywindow('+value.cno+');"> 답변 </a>'
	                	 if(auth =='users' && userno == value.userno){
			                 a += '<a onclick="commentUpdate('+value.cno+',\''+value.ccontent+'\');"> 수정 </a>';
			                 a += '<a onclick="commentDelete('+value.cno+');"> 삭제 </a>';
	                	 }else if(auth == 'business' && bno == value.bno){
	                		 a += '<a onclick="commentUpdate('+value.cno+',\''+value.ccontent+'\');"> 수정 </a>';
			                 a += '<a onclick="commentDelete('+value.cno+');"> 삭제 </a>';
	                	 }
	                	 a += '</div>'
	                 }
	                 
	                 //a += '';
	                 //a += '';
	                 a += '</div>';
	                 a += '</div>';
	             });
	             
	             $(".commentList").html(a);
	         }
	     });
	 }
	 //대댓글 등록창
	 function replywindow(cno){
		 if(windowopen){
		 windowopen=false;
		 var a = '';
		 
		 a += '<div class="commentContainer replycontainer">';				        
	     a += '<form class="commentInsertForm" name="replycommentInsertForm" onsubmit="return check();">';
	     a += '<div class="commentBox">';
	     a += '<div class="commentContent">';
	     a += '<input type="text" class="ccontent replyccontent" id="ccontent" name="ccontent" placeholder="댓글을 작성해주세요.">';
	     a += '<div class="commentbtn">';
	     a += '<div class="commentmodifybtn"><span class="commentbtn">'
	     a += '<button id="ubtn" class="buttoncontent" type="button" name="replycommentInsertBtn" data-value='+cno+'>등록</button></span>';
	     a += '<span class="commentbtn commentbtn2"><button id="ubtn" class="buttoncontent cancelbtn" type="button" onclick="commentList();">취소</button></span></div>';
	     a += '</div>';
	     a += '</div>';
	     a += '</div>'; 
	     a += '</form>';
	     a += '</div>';
		 }else{
			 $(".replycontainer").empty();
			 windowopen=true;
			 var a='';
		 }
	     $('.commentContent'+cno).append(a);
		 
	 }
	 //대댓글 등록 버튼 클릭 시
	 $(document).off("click").on("click","[name=replycommentInsertBtn]",function(e){
		 var targetElement = e.target;
		 var cno = targetElement.getAttribute("data-value");
		 
		 var insertData = $('[name=replycommentInsertForm]').serialize(); //commentInsertForm의 내용을 가져옴
		    if($('input[class=replyccontent]').val() == '' ){
		    	alert("댓글을 입력해 주시기 바랍니다.");
		    	return false;
		    }
		    var sect = $('#sect').val();
		    replycommentInsert(insertData, sect, cno); //Insert 함수호출(아래)
		 
	 } )
	 //대댓글 등록 함수
	 function replycommentInsert(insertData, sect, cno){
		 var num = "";
		 if(sect=="users"){
		 	num = $('#userno').val();
		 }else if(sect="business"){
		 	num = $('#bno').val();
		 }
		 $.ajax({
		        url : '/replycomment/'+fno+'/'+num+'/'+cno,
		        type : 'post',
		        data : insertData,
		        success : function(data){		            
		            commentList(); //댓글 작성 후 댓글 목록 reload
		            
		        },
		        error : function(){alert("에러입니다");}
		  });
		 
		 
	 }
	 
		
	//댓글 삭제 
	 function commentDelete(cno){
		var result = confirm("삭제하시겠습니까? 삭제 후 취소가 불가능합니다.");
		if(result){
		     $.ajax({
		         url : "/cmtDelete/"+cno + "/" + fno,
		         type : "post",
		         success : function(data){
		        	 alert("삭제가 완료되었습니다.");
		        	 commentList(fno);
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
		         url : "/update/"+cno + "/" +fno,
		         type : "post",
		         data : {'ccontent' : updateContent, 'cno' : cno},
		         success : function(data){
		            
		             alert("수정이 완료되었습니다.");
		             commentList(fno);
		         }
		     });
	     }
	 }
	
   <%--댓글테스트--%>
   
   
 
   
 
   function freeRemove() {
		var result = confirm("삭제하시겠습니까? 삭제 후 취소가 불가능합니다.");
		var fno =  $('#fno').val();
		var kwd =  $('#kwd').val();
		var page =  $('#page').val();
		if(result) {
			$.ajax({
				type : "post",
				url : "/freeDelete",
				data : {
					fno:fno,
					kwd:kwd,
					page:page
					},
				success : function(data) {
					alert("삭제가 완료되었습니다.");
					location.href="/freeList?page="+page+"&kwd="+kwd+""; 
				},
				error : function(err) {
					console.log(err);
				}
			});
		}	
	} 
   </script>
</body>
</html>