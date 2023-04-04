<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사업자신청목록</title>
<link href="<c:url value="/static/css/bmypage.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/free.css"/>" rel='stylesheet' />
<style>
.center{
	position: relative;
    top: -400px
}
.item{	
	width:100%;
}
#bcards{
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
    width: 45%;
    text-align: center;
    font-size: 30px;
    line-height: 2;
    border-radius: 2px;
}
.applyList{
	position: relative;
    display: block;
}
.bapplyButtons {
	position: relative;
    bottom: 4px;
    display: flex;
    justify-content: space-around;
}
.btn1,
.btn {
	display: inline;
}
#bapplyBtn {
	display: inline;
	padding: 0;
	height: auto;
	border: 0;
	vertical-align: top;
}
#applying {
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

</style>
</head>
<body>
	<c:import url='/WEB-INF/views/mypage/bmypage.jsp' />   
         <c:if test="${authUser.sect eq 'business' }">
         <c:if test="${authUser.bno eq business.bno }">
         <div class="my_Tab">
               <ul class="tabs">
                  <li class="on"><a href ="/mypage/bmypage/${business.bno }/apply">신청 목록</a></li>            
                  <li><a href ="/mypage/bmypage/${business.bno }/review">거래 후기</a></li>
               </ul>         
         </div>
         </c:if>
         </c:if>

   <c:if test="${authUser.sect eq 'business' }">   
   <div id="total" style="width:1000px; margin:0 auto; margin-bottom: 25%;">
   <div id="bcards">      
   <c:forEach var="apply" items="${apply }">   
   <c:if test="${authUser.bno eq apply.bno }">       
      <div class="applyList">          
      <c:choose>           
            <c:when test="${'신청중' == apply.astatus}">
                      <div class="aindividualStatus">신청대기</div>
                </c:when>
                <c:when test="${apply.astatus == '신청완료'}">
                      <div class="aindividualStatus">수거중</div>
                </c:when>
                <c:when test="${apply.astatus == '수거완료'}">
                      <div class="aindividualStatus">수거완료</div>
                </c:when>                             

                <c:when test="${apply.astatus == '후기등록완료'}">
                      <div class="aindividualStatus">완료</div>
                </c:when>
                <c:otherwise>
                   <c:if test="${apply.astatus == '신청거절' }">
                      <div class="aindividualStatus">수거거절</div>
                   </c:if>
                </c:otherwise>
         </c:choose>     
               <div class="item" style="width:100%;">                  
               <strong>[ 헌옷 수거 신청 ]</strong>      
                  <div class="title"></div>                                                       
                   <div class="allapply" style="padding: 2%;">                      
                       <form name="bapply" method="post">
                          <div class="cont">
                              <div class="aname"><p>${apply.aname }</p></div>
                              <div class="aphone"><p>${apply.aphone }</p></div>                        
                              <div class="aadress"><p>${apply.aaddress }</p></div>
                              <div class="apickup"><p>${apply.apickup }</p></div>                                          
                              <div class="wei">
                                  <div class="weight"><p>${apply.weight }</p></div>                        
                                 <p>kg</p>
                              </div>   
                              <input type="hidden" id="astatus" name="astatus" value="${apply.astatus }">                       
                           </div>   
                            <div class="bapplyButtons">
                           <c:if test="${apply.astatus eq '신청중' }">
                             <div class="btn1">
                                <input type="hidden" id="userno" name="userno" value="${apply.userno }">                                                                                             
                                <input type="hidden" id="ano" name="ano" value="${apply.ano }">
                                <button type="button" id="apply_close_btn" name="cancel" data-abtn4="${apply.astatus }" data-abtn5="${apply.userno }" data-abtn6="${apply.ano }">수거거절</button>
                             </div>                                            
                             <div class="btn" id="bapplyBtn">             
                                <input type="hidden" id="userno" name="userno" value="${apply.userno }">                                                                                             
                                <input type="hidden" id="ano" name="ano" value="${apply.ano }">
                                 <button type="submit" form="form" data-abtn1="${apply.astatus }" data-abtn2="${apply.userno }" data-abtn3="${apply.ano }" id="applying" name="applying" >수거승인</button>
                             </div>
                          </c:if>       
                          <c:if test="${apply.astatus eq '신청완료' }">                                               
                          <c:choose>
                             <c:when test="${apply.astatus eq '신청완료' }">
                                <div class="btn">            
                                   <input type="hidden" id="userno" name="userno" value="${apply.userno }">                                                                                             
                                   <input type="hidden" id="ano" name="ano" value="${apply.ano }">                              
                                    <button type="submit" form="form" data-abtn1="${apply.astatus }" data-abtn2="${apply.userno }" data-abtn3="${apply.ano }" id="applyend" name="applyend">수거완료</button>
                                 </div>
                              </c:when>                              
                           </c:choose>                               
                           </c:if>
                           <c:choose>
                           <c:when test="${apply.astatus eq '수거완료' }">
                                 <div class="btn">            
                                   <input type="hidden" id="userno" name="userno" value="${apply.userno }">                                                                                             
                                   <input type="hidden" id="ano" name="ano" value="${apply.ano }">                                    
                                 </div>
                              </c:when>
                           </c:choose>
                           </div>
                       </form>                   
                    </div>                                    
           </div>              
       </div>   
       </c:if>       
   </c:forEach>      		
</div>
</div>
</c:if>
   <div class="center">
    <ul class="pagination bapaging">
      <c:choose>
         <c:when test="${bapageInfo.page<=1}">
            <li><a id="prev"><<</a></li>
         </c:when>
         <c:otherwise>
            <li><a href="/mypage/bmypage/${business.bno }/apply?bapage=${bapageInfo.page-1}&select=1" id="prev"><<</a></li>&nbsp;
         </c:otherwise>
      </c:choose>
      <c:forEach var="i" begin="${bapageInfo.startPage }" end="${bapageInfo.endPage }">
         <c:choose>
            <c:when test="${bapageInfo.page==i }"><li><a class="active">${i }</a></li></c:when>
            <c:otherwise>
               <li><a href="/mypage/bmypage/${business.bno }/apply?bapage=${i}&select=1">${i }</a></li>
            </c:otherwise>
         </c:choose>
      </c:forEach>
      <c:choose>
         <c:when test="${bapageInfo.page>=bapageInfo.maxPage }">
            <li><a id="next">>></a></li>
         </c:when>
         <c:otherwise>
            <li><a href="/mypage/bmypage/${business.bno }/apply?bapage=${bapageInfo.page+1}&select=1" id="next">>></a></li>
         </c:otherwise>
      </c:choose>
   </ul>
    </div>
    <div>
      <c:import url='/WEB-INF/views/includes/footer.jsp' />
   </div>   
   <script>
   var anos;
   var usernos;   
   var status;
   
   var cano;
   var cuserno;
   var cstatus;
   //수거 승인 
   $('[name=applying]').click(function(e){ //수거승인 등록 버튼 클릭시 
       var updateData = $('[name=astatus]').val(); //bapply의 내용을 가져옴
       var targetElement = e.target;
       status = targetElement.getAttribute("data-abtn1");
       usernos = targetElement.getAttribute("data-abtn2");
       anos = targetElement.getAttribute("data-abtn3");
        applyupdate(status); //Insert 함수호출(아래)
   });
      
   
   function applyupdate(status){      
          $.ajax({
              url : '/bapply/'+anos+'/'+usernos,
              type : 'get',
              data : {"astatus":status},
              success : function(data){                                    
               if(data == '신청완료'){
                  alert("수거 승인 완료");
               }else if(data == '수거완료'){
                  alert("수거 완료");
               }
               
               location.reload();
              },
              error :function(){
                 alert("수거 승인 오류");
              }
          });
      }
   
   //수거 거절
   $('[name=cancel]').click(function(e){ //수거승인 등록 버튼 클릭시 
       var updateData = $('[name=astatus]').val(); //bapply의 내용을 가져옴
       var targetElement = e.target;
       cstatus = targetElement.getAttribute("data-abtn4");
       cuserno = targetElement.getAttribute("data-abtn5");
       cano = targetElement.getAttribute("data-abtn6");
        applydelete(cstatus); //Insert 함수호출(아래)
   });
     
   
   function applydelete(cstatus){      
          $.ajax({
              url : '/bapplydelete/'+cano+'/'+cuserno,
              type : 'get',
              data : {"astatus":cstatus},
              success : function(data){                                    
               alert(data);            
               location.reload();
              },
              error :function(){
                 alert("수거 거절 오류");
              }
          });
      }
   
   
   //수거완료
   $('[name=applyend]').click(function(e){ //수거승인 등록 버튼 클릭시 
       var updateData = $('[name=astatus]').val(); //bapply의 내용을 가져옴
       var targetElement = e.target;
       status = targetElement.getAttribute("data-abtn1");
       usernos = targetElement.getAttribute("data-abtn2");
       anos = targetElement.getAttribute("data-abtn3");
        applyend(status); //Insert 함수호출(아래)
   });
  
   function applyend(status){
	      
	      
          $.ajax({
              url : '/bapply/'+anos+'/'+usernos,
              type : 'get',
              data : {"astatus":status},
              success : function(data){                                    
               alert(data);      
               location.reload();
              },
              error :function(){
                 alert("수거 완료 오류");
              }
          });
      }
	</script>
	<script src="<c:url value='/static/js/free/paging.js'/>"></script>
</body>
</html>