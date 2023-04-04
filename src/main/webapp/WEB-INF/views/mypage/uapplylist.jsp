<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자신청목록</title>
<link href="<c:url value="/static/css/bmypage.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/modal.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/reviewmodal.css"/>" rel='stylesheet' />
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<link href="<c:url value="/static/css/free.css"/>" rel='stylesheet' />
<style>
.center{
	position: relative;
}
.item{	
	width:100%;
}
#ucards{
	display: flex;    
    flex-wrap: wrap;
    justify-content: center;
}
.aindividualStatus {
    position: absolute;
    left: 51%;
    top: 27%;
    transform: translate(-50%, -50%);
    background: black;
    opacity: 0.4;
    color: white;
    width: 51%;
    text-align: center;
    font-size: 30px;
    line-height: 2;
    border-radius: 2px;
}
.applyList{
	position: relative;
    display: block;
}
.applying1{
	width: 6em;
    height: 2.5em;
    border: none;
    background: #5BC1AC;
    color: #fff;
    text-decoration: none;
    border-radius: 9px;
    font-size: 1em;
    cursor: pointer;
    margin: 5% auto;
}
#applying1{
	position: relative;
    bottom: 96%;
	width: 6em;
    height: 2.5em;
    border: none;
    background: #5BC1AC;
    color: #fff;
    text-decoration: none;
    border-radius: 9px;
    font-size: 1em;
    cursor: pointer;
    margin: 5% 3%;
}
#applying1{    
     width: 6em;
    height: 2.5em;
    border: none;
    background: #5BC1AC;
    color: #fff;
    text-decoration: none;
    border-radius: 9px;
    font-size: 1em;
    margin: 5% auto;
    cursor: pointer;
    bottom: 2px;
}
/* Hover state animation applied here */
.applying1:hover { 
  -webkit-animation: hover 1200ms linear 2 alternate;
  animation: hover 1200ms linear 2 alternate;
}
/* Active state animation applied here */
#applying1:active {
  -webkit-animation: active 1200ms ease 1 alternate;
  animation: active 1200ms ease 1 alternate; 
  background: #5F9BE0;
}
/* Active state animation keyframes below */
@keyframes active { 
  0% {transform: scale(1,1);}
  90% {transform: scale(.9,.88);}
  100% {transform: scale(.92,.9);}
}
/* Hover state animation keyframes below */
@keyframes hover { 
  0% { -webkit-transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  1.8% { -webkit-transform: matrix3d(1.016, 0, 0, 0, 0, 1.037, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.016, 0, 0, 0, 0, 1.037, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  3.5% { -webkit-transform: matrix3d(1.033, 0, 0, 0, 0, 1.094, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.033, 0, 0, 0, 0, 1.094, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  4.7% { -webkit-transform: matrix3d(1.045, 0, 0, 0, 0, 1.129, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.045, 0, 0, 0, 0, 1.129, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  5.31% { -webkit-transform: matrix3d(1.051, 0, 0, 0, 0, 1.142, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.051, 0, 0, 0, 0, 1.142, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  7.01% { -webkit-transform: matrix3d(1.068, 0, 0, 0, 0, 1.158, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.068, 0, 0, 0, 0, 1.158, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  8.91% { -webkit-transform: matrix3d(1.084, 0, 0, 0, 0, 1.141, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.084, 0, 0, 0, 0, 1.141, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  9.41% { -webkit-transform: matrix3d(1.088, 0, 0, 0, 0, 1.132, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.088, 0, 0, 0, 0, 1.132, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  10.71% { -webkit-transform: matrix3d(1.097, 0, 0, 0, 0, 1.107, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.097, 0, 0, 0, 0, 1.107, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  12.61% { -webkit-transform: matrix3d(1.108, 0, 0, 0, 0, 1.077, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.108, 0, 0, 0, 0, 1.077, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  14.11% { -webkit-transform: matrix3d(1.114, 0, 0, 0, 0, 1.067, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.114, 0, 0, 0, 0, 1.067, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  14.41% { -webkit-transform: matrix3d(1.115, 0, 0, 0, 0, 1.067, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.115, 0, 0, 0, 0, 1.067, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  16.32% { -webkit-transform: matrix3d(1.119, 0, 0, 0, 0, 1.077, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.119, 0, 0, 0, 0, 1.077, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  18.12% { -webkit-transform: matrix3d(1.121, 0, 0, 0, 0, 1.096, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.121, 0, 0, 0, 0, 1.096, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  18.72% { -webkit-transform: matrix3d(1.121, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.121, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  20.02% { -webkit-transform: matrix3d(1.121, 0, 0, 0, 0, 1.113, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.121, 0, 0, 0, 0, 1.113, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  21.82% { -webkit-transform: matrix3d(1.119, 0, 0, 0, 0, 1.119, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.119, 0, 0, 0, 0, 1.119, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  24.32% { -webkit-transform: matrix3d(1.115, 0, 0, 0, 0, 1.11, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.115, 0, 0, 0, 0, 1.11, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  25.53% { -webkit-transform: matrix3d(1.113, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.113, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  29.23% { -webkit-transform: matrix3d(1.106, 0, 0, 0, 0, 1.089, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.106, 0, 0, 0, 0, 1.089, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  29.93% { -webkit-transform: matrix3d(1.105, 0, 0, 0, 0, 1.09, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.105, 0, 0, 0, 0, 1.09, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  35.54% { -webkit-transform: matrix3d(1.098, 0, 0, 0, 0, 1.105, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.098, 0, 0, 0, 0, 1.105, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  36.64% { -webkit-transform: matrix3d(1.097, 0, 0, 0, 0, 1.106, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.097, 0, 0, 0, 0, 1.106, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  41.04% { -webkit-transform: matrix3d(1.096, 0, 0, 0, 0, 1.099, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.096, 0, 0, 0, 0, 1.099, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  44.04% { -webkit-transform: matrix3d(1.096, 0, 0, 0, 0, 1.097, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.096, 0, 0, 0, 0, 1.097, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  51.45% { -webkit-transform: matrix3d(1.099, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.099, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  52.15% { -webkit-transform: matrix3d(1.099, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.099, 0, 0, 0, 0, 1.102, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  58.86% { -webkit-transform: matrix3d(1.101, 0, 0, 0, 0, 1.099, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.101, 0, 0, 0, 0, 1.099, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  63.26% { -webkit-transform: matrix3d(1.101, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.101, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  66.27% { -webkit-transform: matrix3d(1.101, 0, 0, 0, 0, 1.101, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.101, 0, 0, 0, 0, 1.101, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  73.77% { -webkit-transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  81.18% { -webkit-transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  85.49% { -webkit-transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  88.59% { -webkit-transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  96% { -webkit-transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); }
  100% { -webkit-transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); transform: matrix3d(1.1, 0, 0, 0, 0, 1.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); } 
}

#uapplyListContainer {
	margin-bottom: 25%;
}
#uapplyPaging {
	margin-top: 10%;
}
.uapplybtn {
	bottom: 12px;
}
#uapplybtn {
	display: block;
	border: none;
	cursor: default;
}
#applying{
	    width: 6em;
    height: 2.5em;
    border: none;
    background: #5BC1AC;
    color: #fff;
    text-decoration: none;
    border-radius: 9px;
    font-size: 1em;
    margin: 5% 3%;
    cursor: pointer;
}
#apply_close_btn {
	
}
</style>
</head>
<body>   
   <c:import url='/WEB-INF/views/mypage/usermypage.jsp' />
   <c:if test="${authUser.sect eq 'users' }">
      <c:if test="${authUser.userno eq users.userno }">
         <div class="my_Tab">
               <ul class="tabs">
                  <li><a href ="/mypage/umypage/${users.userno }/sell">판매 상품</a></li>
                  <li><a href ="/mypage/umypage/${users.userno }/buy">구매 상품</a></li>
                  <li class="on"><a href ="/mypage/umypage/${users.userno }/apply">신청 목록</a></li>
                  <li><a href ="/mypage/umypage/${users.userno }/review">거래 후기</a></li>
               </ul>
         </div>
      </c:if>
   </c:if>
   <div id="uapplyListContainer">
      <c:if test="${authUser.sect eq 'users' }">
         <div id="total" style="width:1000px; margin:0 auto;">
            <div id="ucards">
               <c:forEach var="wapply" items="${wapply }">
               <input type="hidden" id="bno" name="bno" value="${wapply.bno }" data-sno="${wapply.bno }">
               <c:if test="${authUser.userno eq wapply.userno }">     
                  <div class="applyList">
                     <c:choose>           
                        <c:when test="${'신청중' != wapply.astatus}">
                                  <div class="aindividualStatus">${wapply.astatus }</div>
                            </c:when>
                            <c:when test="${wapply.astatus == '신청중'}">
                                  <div class="aindividualStatus">신청중</div>
                            </c:when>
                     </c:choose>                
                           <div class="item" style="width:100%;">                  
                           <strong>[ ${wapply.bname } ]</strong>      
                              <div class="title"></div>                                                       
                               <div class="allapply" style="padding: 2%;">                      
                                   <div class="cont">   
                                      <input type="hidden" class="bnoContent" name="bnoContent" value="${wapply.bno }" data-sno="${wapply.bno }">
                                      <input type="hidden" class="anoContent" name="anoContent" value="${wapply.ano }" data-sno="${wapply.ano }">                       
                                       <div class="aname"><p>${wapply.aname }</p></div>
                                       <div class="aphone"><p>${wapply.aphone }</p></div>                        
                                       <div class="aadress"><p>${wapply.aaddress }</p></div>
                                       <div class="apickup"><p>${wapply.apickup }</p></div>                                          
                                       <div class="wei">
                                           <div class="weight"><p>${wapply.weight }</p></div>                        
                                          <p>kg</p>
                                       </div>                          
                                    </div>
                                 
                                   
                                   <c:choose>                     
                                   <c:when test="${wapply.astatus eq '신청중' }">
                                       <div class="btn uapplybtn" id="uapplybtn">
                                          <input type="hidden" id="userno" name="userno" value="${wapply.userno }">                                                                                             
                                           <input type="hidden" id="ano" name="ano" value="${wapply.ano }">                              
                                          <button type="button" id="apply_close_btn" name="cancel" data-abtn2="${wapply.userno }" data-abtn3="${wapply.ano }">신청취소</button>
                                      </div>
                                   </c:when>
                                   <c:when test="${wapply.astatus eq '신청완료' }">
                                      <div class="btn  uapplybtn">                           
                                        <!-- 버튼 사라짐 -->
                                      </div>                   
                                   </c:when>                       
                                      
                                   </c:choose>
                                   <c:choose> 
                                   <c:when test="${wapply.astatus eq '수거완료' }">
                                         <div class="btn uapplybtn" id="uapplybtn">                           
                                             <button type="submit" form="form" id="applying1" class="complete">후기작성</button>
                                         </div>                   
                                   </c:when>
                                   <c:otherwise>
                                      <c:if test="${wapply.astatus eq '신청거절' }">
                                    <!-- 버튼 안 나옴 거절 당했을 시 -->
                                 </c:if>
                                   </c:otherwise>   
                                   </c:choose> 
                                   <c:if test="${wapply.astatus eq '후기대기' }">
                                   <div class="btn uapplybtn" id="uapplybtn">
                                       <button type="button" class="applying1">후기작성</button>
                                   </div>
                                   </c:if>                    
                                </div>                                    
                          </div>              
                      </div>
                      <div id="modal" class="modal">
                           <div class="modal_content">
                           
                           
                              <div class="modal_layer"></div>
                           </div>
                        </div>
                         
                   </c:if> 
               </c:forEach>    
            </div>
         </div>
   </c:if>
   
   
      <div id="demo-modal2" class="firstmodal">
               <div class="modal__content7" id="modal__content7">
               <strong>[ 거래후기 ]</strong>
                     <div class="stars7">
                        <button class="star">1</button>
                        <button class="star">2</button>
                        <button class="star">3</button>
                        <button class="star">4</button>
                        <button class="star">5</button>                     
                     </div>
                     <textarea style= "padding: 3%; height: 58%; width: 100%; resize:none;" name="content" class="content" placeholder="거래 후기를 입력해 주세요."></textarea>
                     <button id="uapplyReviewcomplete7">작성완료</button>   
               <a href="#" class="modal__close">&times;</a>
               </div>
        </div>
   
       <div class="center" id="uapplyPaging">
          <ul class="pagination apaging">
            <c:choose>
               <c:when test="${apageInfo.page<=1}">
                  <li><a id="prev"><<</a></li>
               </c:when>
               <c:otherwise>
                  <li><a href="/mypage/umypage/${users.userno }/apply?apage=${apageInfo.page-1}&select=1" id="prev"><<</a></li>&nbsp;
               </c:otherwise>
            </c:choose>
            <c:forEach var="i" begin="${apageInfo.startPage }" end="${apageInfo.endPage }">
               <c:choose>
                  <c:when test="${apageInfo.page==i }"><li><a class="active">${i }</a></li></c:when>
                  <c:otherwise>
                     <li><a href="/mypage/umypage/${users.userno }/apply?apage=${i}&select=1">${i }</a></li>
                  </c:otherwise>
               </c:choose>
            </c:forEach>
            <c:choose>
               <c:when test="${apageInfo.page>=apageInfo.maxPage }">
                  <li><a id="next">>></a></li>
               </c:when>
               <c:otherwise>
                  <li><a href="/mypage/umypage/${users.userno }/apply?apage=${apageInfo.page+1}&select=1" id="next">>></a></li>
               </c:otherwise>
            </c:choose>
         </ul>
       </div>
    

    
    </div>
    <div>
		 <c:import url='/WEB-INF/views/includes/footer.jsp' />
   </div>
   <script>
   var bnoo = $('#bno').val();
   //모달 켜기
	var cano;
   var cuserno;
   //수거 거절
   $('[name=cancel]').click(function(e){ //수거승인 등록 버튼 클릭시        
       var targetElement = e.target;       
       cuserno = targetElement.getAttribute("data-abtn2");
       cano = targetElement.getAttribute("data-abtn3");       
        wapplydelete(); //삭제함수 호출
   });
     
   
   function wapplydelete(){      
          $.ajax({
              url : '/wapplydelete/'+cano+'/'+cuserno,
              type : 'get',              
              success : function(data){                                    
               alert(data);            
               location.reload();
              },
              error :function(){
                 alert("수거 거절 오류");
              }
          });
      }
   var bno;
   var uapplyIndex;
   $('#applying1').click(function(){
      $('.firstmodal').css('visibility','visible');
      $('.firstmodal').css('opacity','1');
      $('.firstmodal').css('z-index','2');
      uapplyIndex = $('.applyList').index($(this).parent().parent().parent().parent());
      var ano = $('.anoContent:eq('+uapplyIndex+')').attr("data-sno");
      $('.modal__close').click(function(){
         $.ajax({
            type : "get",
            url : "/uapplyReviewStatus",
            data : {ano:ano},
            success : function(data) {
               location.reload();
            },
            error : function(err) {
               console.log(err);
            }
         });
      })
   })
   
   $('.applying1').click(function(){
      $('.firstmodal').css('visibility','visible');
      $('.firstmodal').css('opacity','1');
      $('.firstmodal').css('z-index','2');
      uapplyIndex = $('.applyList').index($(this).parent().parent().parent());
      var ano = $('.anoContent:eq('+uapplyIndex+')').attr("data-sno");
   })
   
   $('.modal__close').click(function(e){
      e.preventDefault();
      $('.firstmodal').css('visibility','hidden');
      $('.firstmodal').css('opacity','0');
      $('.firstmodal').css('z-index','0');
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
      
   $("#uapplyReviewcomplete7").click(function(){
         bno = $('.bnoContent:eq('+uapplyIndex+')').attr("data-sno");
         var ano = $('.anoContent:eq('+uapplyIndex+')').attr("data-sno");
         var content = $('.content').val();
         $.ajax({
            type : "get",
            url : "/sendUapplyReview",
            data : {star:star,
                  content:content,
                  bno:bno,
                  ano:ano},
            success : function(data) {
               location.reload();
            },
            error : function(err) {
               console.log(err);
            }
         });
      })
   
	
	</script>
<script src="<c:url value='/static/js/free/paging.js'/>"></script>	
</body>
</html>