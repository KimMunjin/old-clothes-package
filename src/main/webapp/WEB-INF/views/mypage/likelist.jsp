<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<link href="<c:url value="/static/css/selectoption.css"/>" rel='stylesheet'/>
<link href="<c:url value="/static/css/likelist.css"/>" rel='stylesheet'/>
<link href="<c:url value="/static/css/mypage.css"/>" rel='stylesheet' />
<link href="<c:url value="/static/css/datepicker.min.css"/>" rel='stylesheet' type="text/css" media="all"/>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<title>Insert title here</title>
</head>
<body>
	<c:import url='/WEB-INF/views/mypage/mypage.jsp' />
	<div id="full">
		<h2 class="likelistSub"><strong>찜 목록</strong></h2>
		<div id="cate" class="select" >
			<div id="selectbox">
				<select id="catelist" onchange="catelist()">
					<option value="free">무료나눔</option>
					<option value="indi">개인판매</option>
					<option value="busi">업체판매</option>
				</select>
			</div>
		</div>
		<div id="likeContainer">
			<div id="list" class="list">
				
			</div>
		</div>
	<!-- 헌옷 수거 신청 모달창 -->
		<div id="lmodal" class="lmodal">
   
		    <div class="lmodal_content">
		    	<ul>
		    		<li class="litem" id="litem">		    			 
				            
				             <div class="ltitle">
				                 <strong id="lstrong">[ 헌옷 수거 신청 ]</strong>
				            	 <p id="p">※접수가 완료되면 업체가 확인 후 연락드리겠습니다.</p>
				       	     </div>
				         <div class="allapply">
			         	 	<form class="form" id="form" action="apply" method="POST" onsubmit="return Valids();">
			         	 		<div class="cont">
						              <input type="text" id="lname" name="aname" placeholder="이름">
						              <div class="adre">
						                  <input type="text" id="laddress" name="aaddress" placeholder="주소(동 까지만 입력)" readonly>
						                  <input type="button" id="lad_bt" name="aad_bt" readonly onclick="findAddr()" value="주소검색">
						              </div>
							              <input type="text" id="lphone" name="aphone" placeholder="휴대폰 번호(- 제외하고 입력 )">
							              <input type="text" id="lpickup" name="apickup" placeholder="수거 희망 날짜[시간]" readonly>
						              <div class="wei">
						                  <input type="text" id="lweight" name="weight" placeholder="옷 무게">
						                  <p id="kg">kg</p>
						              </div>
						          </div>	
						          <hr class="hr2">		         	 	
			         	 	<div class="likebtn">			         	 	  
				              <button type="button" id="lmodal_close_btn">Close</button>
				              <button type="submit" form="form" id="lapplycloth">수거신청</button>
				           </div>
				           	  <input type="hidden" id="bno" name="bno" value="">
			         	 	</form>
				         </div>
		    		</li>
		    	</ul>
		    </div>
		    
		    <div class="lmodal_layer"></div>
		</div>	
	</div>
    <div>
		<c:import url='/WEB-INF/views/includes/footer.jsp' />
	</div>	
<script>
$(document).ready(function(){
	let category = "free";
	let totalData; //총 데이터 수
	let dataPerPage=7; //한 페이지에 나타낼 글 수
	let pageCount=5; //페이징에 나타낼 페이지 수
	let globalCurrentPage=1; //현재 페이지
	let pageHtml="";
	
	var line = "";
	 $.ajax({
		type: 'post',
		url: 'likelistaj',
		async:false,
		dataType: 'json',
		data: JSON.stringify({		
			"category" : category
		}),
		contentType: "application/json",
		success: function(data){
			$('#list').empty();
			totalData= data.length;
			
			//목록 표시 호출
			displayData(1,dataPerPage);
			//페이징 표시 호출
			paging(totalData,dataPerPage, pageCount, 1);
			
			
			
			//현재 페이지(currentPage)와 페이지당 글 개수(dataPerPage) 반영 
			function displayData(currentPage, dataPerPage){
				let chartHtml="";
				
				//Number로 변환하지 않으면 아래에서 +를 할 경우 스트링 결합이 된다
				currentPage=Number(currentPage);
				dataPerPage=Number(dataPerPage);
				//var i = (currentPage-1)*dataPerPage
				chartHtml +='<div class="user-list">';
				data.forEach(function(data,i){
					if(i>=((currentPage-1)*dataPerPage+dataPerPage)){
						return false;
					}
					if(i>=(currentPage-1)*dataPerPage){
						if(category=="free"){
							chartHtml +='<div class="user">';
							chartHtml +='<div class="eachuser">';
							if(data.sfileids==null){
								chartHtml +='<div class="img-wrap">';
								chartHtml +='<img class="productimg" src="/static/image/logo1.png" />';
								chartHtml +='</div>';
							}else{
								chartHtml +='<div class="img-wrap">';
								chartHtml +='<img src="/upload/'+data.sfileids+'" alt="무료나눔 옷" class="productimg">';
								chartHtml +='</div>';
							}
							chartHtml +='<div class="info">';
							chartHtml +='<span class="name"><a href="/sharingView/'+data.sno+'">'+data.stitle+'</a></span>';
							chartHtml +='<span class="date">'+data.regDate+'</span>';
							chartHtml +='<button type="button" id="delete" class="sdelete" data-value="'+data.sno+'"><img class="remove" src="/static/image/close.png" /></button>';
							chartHtml +='</div>';
							chartHtml +='</div>';
							chartHtml +='<div class="expand"></div>';
							chartHtml +='</div>';
						}
					}
					i++
				})
				chartHtml +='<div id="paging">'
				chartHtml +='<ul id="pagingul"></ul></div>'
				$("#list").html(chartHtml);
				$("#pagingul").html(pageHtml);
			}
			function paging(totalData, dataPerPage, pageCount, currentPage){
				
				let totalPage= Math.ceil(totalData/dataPerPage);//총페이지수
				if(totalPage<pageCount){
					pageCount = totalPage;
				}
				let pageGroup = Math.ceil(currentPage/pageCount);//페이지 그룹
				let last = pageGroup*pageCount;//화면에 보여질 마지막 페이지 번호
				
				if(last>totalPage){
					last = totalPage;
				}
				let first = last - (pageCount-1);//화면에 보여질 첫번째 페이지 번호
				let next = last + 1;
				let prev = first -1;
				pageHtml ="";
				if(prev>0){
					pageHtml += "<li><a href='#' id='prev'><<</a></li>";
				}
				for(var i = first; i<=last; i++){
					if(currentPage == i){
						pageHtml +="<li class='on'><a href='#' class='active' id='"+i+"'>"+i+"</a></li>";
					}else{
						pageHtml +="<li><a href='#' id='"+i+"'>"+i+"</a></li>";
					}	
				}
				if(last<totalPage){
					pageHtml +="<li><a href='#' id='next'>>></a></li>";
				}
				$("#pagingul").html(pageHtml);
				
				
				//페이징 번호 클릭 이벤트
				$(document).on("click","#pagingul li a",function(){
					let $id = $(this).attr("id");
					selectedPage=$(this).text();
					if($id=="next"){
						selectedPage = next;
					}if($id=="prev"){
						selectedPage = prev;
					}
					//전역변수에 선택한 페이지 번호를 담는다
					globalCurrentPage = selectedPage;
					//페이징 표시 재호출
					paging(totalData, dataPerPage, pageCount, selectedPage);
					//글 목록 표시 재호출
					displayData(selectedPage, dataPerPage);
				})
				
			}
			$(document).off("click","#delete").on("click","#delete",function(e){
				let category = "";
				var targetElement = e.target.parentElement;
				let deleteno = targetElement.getAttribute('data-value');
				let deleteclass = targetElement.getAttribute('class');
				if(deleteclass=="sdelete"){
					category = "free"
				}else if(deleteclass=="idelete"){
					category = "indi"
				}else if(deleteclass=="bdelete"){
					category = "busi"
				}
				$.ajax({
					type: 'post',
					url: 'deletelike',
					dataType: 'json',
					async:false,
					data: JSON.stringify({	
						"category" : category,
						"deleteno" : deleteno
					}),
					contentType: "application/json",
					success: function(result){
						if(result){
							alert("좋아요 취소되었습니다");
							refreshlist(category,globalCurrentPage);
						}else{
							alert("에러입니다");
						}
					},
					error:function(){alert("에러입니다");}
				})
			})
			
		},
		error:function(){alert("에러입니다.");}
	})
})

function catelist(){
	let category = $("#catelist option:selected").val();
	let totalData; //총 데이터 수
	let dataPerPage=7; //한 페이지에 나타낼 글 수
	let pageCount=5; //페이징에 나타낼 페이지 수
	let globalCurrentPage=1; //현재 페이지
	let pageHtml="";
	
	var line = "";
	 $.ajax({
		type: 'post',
		url: 'likelistaj',
		async:false,
		dataType: 'json',
		data: JSON.stringify({		
			"category" : category
		}),
		contentType: "application/json",
			success: function(data){
			$('#list').empty();
			totalData= data.length;
			
			//목록 표시 호출
			displayData(1,dataPerPage);
			//페이징 표시 호출
			paging(totalData,dataPerPage, pageCount, 1);
			
			
			
			//현재 페이지(currentPage)와 페이지당 글 개수(dataPerPage) 반영 
			function displayData(currentPage, dataPerPage){
				let chartHtml="";
				
				//Number로 변환하지 않으면 아래에서 +를 할 경우 스트링 결합이 된다
				currentPage=Number(currentPage);
				dataPerPage=Number(dataPerPage);
				//var i = (currentPage-1)*dataPerPage
				chartHtml +='<div class="user-list">';
				data.forEach(function(data,i){
					if(i>=((currentPage-1)*dataPerPage+dataPerPage)){
						return false;
					}
					if(i>=(currentPage-1)*dataPerPage){
						if(category=="free"){
							chartHtml +='<div class="user">';
							chartHtml +='<div class="eachuser">';
							if(data.sfileids==null){
								chartHtml +='<div class="img-wrap">';
								chartHtml +='<img class="productimg" src="/static/image/logo1.png" />';
								chartHtml +='</div>';
							}else{
								chartHtml +='<div class="img-wrap">';
								chartHtml +='<img src="/upload/'+data.sfileids+'" alt="무료나눔 옷" class="productimg">';
								chartHtml +='</div>';
							}
							chartHtml +='<div class="info">';
							chartHtml +='<span class="name"><a href="/sharingView/'+data.sno+'">'+data.stitle+'</a></span>';
							chartHtml +='<span class="date">'+data.regDate+'</span>';
							chartHtml +='<button type="button" id="delete" class="sdelete" data-value="'+data.sno+'"><img class="remove" src="/static/image/close.png" /></button>';
							chartHtml +='</div>';
							chartHtml +='</div>';
							chartHtml +='<div class="expand"></div>';
							chartHtml +='</div>';
						}else if(category=="indi"){
								chartHtml +='<div class="user">';
								chartHtml +='<div class="eachuser">';
								if(data.ifileids==null){
									chartHtml +='<div class="img-wrap">';
									chartHtml +='<img class="productimg" src="/static/image/logo1.png" />';
									chartHtml +='</div>';
								}else{
									chartHtml +='<div class="img-wrap">';
									chartHtml +='<img src="/upload/'+data.ifileids+'" alt="개인판매 옷" class="productimg">';
									chartHtml +='</div>';
								}
								chartHtml +='<div class="info">';
								chartHtml +='<span class="name"><a href="/sellView/'+data.ino+'">'+data.ititle+'</a></span>';
								chartHtml +='<span class="date">'+data.regDate+'</span>';
								chartHtml +='<button type="button" id="delete" class="sdelete" data-value="'+data.ino+'"><img class="remove" src="/static/image/close.png" /></button>';
								chartHtml +='</div>';
								chartHtml +='</div>';
								chartHtml +='<div class="expand"></div>';
								chartHtml +='</div>';
						}else if(category=="busi"){
							chartHtml +='<div class="user">';
							chartHtml +='<div class="eachuser">';
							chartHtml +='<div class="img-wrap">';
							chartHtml +='<img src="/static/image/market.png" alt="업체로고" class="productimg">';
							chartHtml +='</div>';
							chartHtml +=' <div class="nameandadd">';
							chartHtml +=' <div class="name bname"><a href="/mypage/bmypage/'+data.bno+'">'+data.bname+'</a></div>';
							chartHtml +='<div id="address" class="address">'+data.baddress+' '+data.bdetailadd+'</div>'
							chartHtml +='</div>'
							chartHtml +='<span class="icons">'
							chartHtml +='<button class="buttonapply'+data.bno+'" id="applymodal" data-value="'+data.bno+'"><img class="apply" src="/static/image/apply.png" /></button>'; //신청서 작성 form [modal]
							chartHtml +='<div id="deletebu">'
							chartHtml +='<button id="delete" class="bdelete" data-value="'+data.bno+'"><img class="remove" src="/static/image/close.png" /></button>';
							chartHtml +='</span>'	
							chartHtml +='</div>'
							chartHtml +='<div class="expand"></div>';
							chartHtml +='</div></div>';	
						}
					}
					i++
				})
				chartHtml +='</div>'
				chartHtml += '<div id="paging">'
				chartHtml +='<ul id="pagingul"></ul></div>'
				$("#list").html(chartHtml);
				$("#pagingul").html(pageHtml);
			}
			function paging(totalData, dataPerPage, pageCount, currentPage){
				
				let totalPage= Math.ceil(totalData/dataPerPage);//총페이지수
				if(totalPage<pageCount){
					pageCount = totalPage;
				}
				let pageGroup = Math.ceil(currentPage/pageCount);//페이지 그룹
				let last = pageGroup*pageCount;//화면에 보여질 마지막 페이지 번호
				
				if(last>totalPage){
					last = totalPage;
				}
				let first = last - (pageCount-1);//화면에 보여질 첫번째 페이지 번호
				let next = last + 1;
				let prev = first -1;
				pageHtml ="";
				if(prev>0){
					pageHtml += "<li><a href='#' id='prev'><<</a></li>";
				}
				for(var i = first; i<=last; i++){
					if(currentPage == i){
						pageHtml +="<li class='on'><a href='#' id='"+i+"'>"+i+"</a></li>";
					}else{
						pageHtml +="<li><a href='#' id='"+i+"'>"+i+"</a></li>";
					}	
				}
				if(last<totalPage){
					pageHtml +="<li><a href='#' id='next'>>></a></li>";
				}
				$("#pagingul").html(pageHtml);
				
				
				//페이징 번호 클릭 이벤트
				$(document).on("click","#pagingul li a",function(){
					let $id = $(this).attr("id");
					selectedPage=$(this).text();
					
					if($id=="next"){
						selectedPage = next;
					}if($id=="prev"){
						selectedPage = prev;
					}
					//전역변수에 선택한 페이지 번호를 담는다
					globalCurrentPage = selectedPage;
					//페이징 표시 재호출
					paging(totalData, dataPerPage, pageCount, selectedPage);
					//글 목록 표시 재호출
					displayData(selectedPage, dataPerPage);
				})
			}
			$(document).off("click","#delete").on("click","#delete",function(e){
				let category = "";
				var targetElement = e.target.parentElement;
				let deleteno = targetElement.getAttribute('data-value');
				let deleteclass = targetElement.getAttribute('class');
				if(deleteclass=="sdelete"){
					category = "free"
				}else if(deleteclass=="idelete"){
					category = "indi"
				}else if(deleteclass=="bdelete"){
					category = "busi"
				}
				$.ajax({
					type: 'post',
					url: 'deletelike',
					dataType: 'json',
					async:false,
					data: JSON.stringify({	
						"category" : category,
						"deleteno" : deleteno
					}),
					contentType: "application/json",
					success: function(result){
						if(result){
							alert("좋아요 취소되었습니다");
							refreshlist(category,globalCurrentPage);
						}else{
							alert("에러입니다");
						}
					},
					error:function(){alert("에러입니다");}
				})
			})
			//모달 켜기
			$(document).off("click","#applymodal").on("click","#applymodal",function(m){
				var apclick = m.target.parentElement;
				var bno = apclick.getAttribute('data-value');
	           	document.getElementById("lmodal").style.display="block";
	           	document.getElementById("bno").value=bno;
			})
			
			/* $(".buttonapply"+data.bno+"").click(function(){
            	 document.getElementById("modal").style.display="block";
            	 var bno = $(".buttonapply"+data.bno+"").data('value');
            	 document.getElementById("bno").value=bno;
            	 
            }); */
		},
		error:function(){alert("에러입니다.");}
	})
}

function refreshlist(category,globalCurrentPage){
	let totalData; //총 데이터 수
	let dataPerPage=7; //한 페이지에 나타낼 글 수
	let pageCount=5; //페이징에 나타낼 페이지 수
	let pageHtml="";
	 $.ajax({
		type: 'post',
		url: 'likelistaj',
		async:false,
		dataType: 'json',
		data: JSON.stringify({		
			"category" : category
		}),
		contentType: "application/json",
		success: function(data){
			$('#list').empty();
			totalData= data.length;
			
			//목록 표시 호출
			displayData(globalCurrentPage,dataPerPage);
			//페이징 표시 호출
			paging(totalData,dataPerPage, pageCount, globalCurrentPage);
			
			
			
			//현재 페이지(currentPage)와 페이지당 글 개수(dataPerPage) 반영 
			function displayData(currentPage, dataPerPage){
				let chartHtml="";
				
				//Number로 변환하지 않으면 아래에서 +를 할 경우 스트링 결합이 된다
				currentPage=Number(currentPage);
				dataPerPage=Number(dataPerPage);
				//var i = (currentPage-1)*dataPerPage
				data.forEach(function(data,i){
					if(i>=((currentPage-1)*dataPerPage+dataPerPage)){
						return false;
					}
					if(i>=(currentPage-1)*dataPerPage){
						if(category=="free"){
							chartHtml +='<div class="user">';
							chartHtml +='<div class="eachuser">';
							if(data.sfileids==null){
								chartHtml +='<div class="img-wrap">';
								chartHtml +='<img class="productimg" src="/static/image/logo1.png" />';
								chartHtml +='</div>';
							}else{
								chartHtml +='<div class="img-wrap">';
								chartHtml +='<img src="/upload/'+data.sfileids+'" alt="무료나눔 옷" class="productimg">';
								chartHtml +='</div>';
							}
							chartHtml +='<div class="info">';
							chartHtml +='<span class="name"><a href="/sharingView/'+data.sno+'">'+data.stitle+'</a></span>';
							chartHtml +='<span class="date">'+data.regDate+'</span>';
							chartHtml +='<button type="button" id="delete" class="sdelete" data-value="'+data.sno+'"><img class="remove" src="/static/image/close.png" /></button>';
							chartHtml +='</div>';
							chartHtml +='</div>';
							chartHtml +='<div class="expand"></div>';
							chartHtml +='</div>';
						}else if(category=="indi"){
								chartHtml +='<div class="user">';
								chartHtml +='<div class="eachuser">';
								if(data.ifileids==null){
									chartHtml +='<div class="img-wrap">';
 									chartHtml +='<img class="productimg" src="/static/image/logo1.png" />';
									chartHtml +='</div>';
								}else{
									chartHtml +='<div class="img-wrap">';
									chartHtml +='<img src="/upload/'+data.ifileids+'" alt="개인판매 옷" class="productimg">';
									chartHtml +='</div>';
								}
								chartHtml +='<div class="info">';
								chartHtml +='<span class="name"><a href="/sellView/'+data.ino+'">'+data.ititle+'</a></span>';
								chartHtml +='<span class="date">'+data.regDate+'</span>';
								chartHtml +='<button type="button" id="delete" class="sdelete" data-value="'+data.ino+'"><img class="remove" src="/static/image/close.png" /></button>';
								chartHtml +='</div>';
								chartHtml +='</div>';
								chartHtml +='<div class="expand"></div>';
								chartHtml +='</div>';
						}else if(category=="busi"){
							chartHtml +='<div class="user">';
							chartHtml +='<div class="eachuser">';
							chartHtml +='<div class="img-wrap">';
							chartHtml +='<img src="/static/image/market.png" alt="업체로고" class="productimg">';
							chartHtml +='</div>';
							chartHtml +=' <div class="nameandadd">';
							chartHtml +=' <div class="name bname"><a href="/mypage/bmypage/'+data.bno+'">'+data.bname+'</a></div>';
							chartHtml +='<div id="address" class="address">'+data.baddress+' '+data.bdetailadd+'</div>'
							chartHtml +='</div>'
							chartHtml +='<span class="icons">'
							chartHtml +='<button class="buttonapply'+data.bno+'" id="applymodal" data-value="'+data.bno+'"><img class="apply" src="/static/image/apply.png" /></button>'; //신청서 작성 form [modal]
							chartHtml +='<div id="deletebu">'
							chartHtml +='<button id="delete" class="bdelete" data-value="'+data.bno+'"><img class="remove" src="/static/image/close.png" /></button>';
							chartHtml +='</span>'	
							chartHtml +='</div>'
							chartHtml +='<div class="expand"></div>';
							chartHtml +='</div>';	
						}
					}
					i++
				})
				chartHtml +='</div>'
				chartHtml +='<div id="paging">'
				chartHtml +='<ul id="pagingul"></ul></div>'
				$("#list").html(chartHtml);
				$("#pagingul").html(pageHtml);
				
				/*  //모달 켜기
	            $(".buttonapply"+data.bno+"").click(function(){
	            	 document.getElementById("modal").style.display="block";
	            	 var bno = $(".buttonapply"+data.bno+"").data('value');
	            	 document.getElementById("bno").value=bno;
	            	 
	            }); */
	            $(document).off("click","#applymodal").on("click","#applymodal",function(m){
					var apclick = m.target.parentElement;
					var bno = apclick.getAttribute('data-value');
		           	document.getElementById("lmodal").style.display="block";
		           	document.getElementById("bno").value=bno;
				}) 
			}
			function paging(totalData, dataPerPage, pageCount, currentPage){
				
				let totalPage= Math.ceil(totalData/dataPerPage);//총페이지수
				if(totalPage<pageCount){
					pageCount = totalPage;
				}
				let pageGroup = Math.ceil(currentPage/pageCount);//페이지 그룹
				let last = pageGroup*pageCount;//화면에 보여질 마지막 페이지 번호
				
				if(last>totalPage){
					last = totalPage;
				}
				let first = last - (pageCount-1);//화면에 보여질 첫번째 페이지 번호
				let next = last + 1;
				let prev = first -1;
				pageHtml ="";
				if(prev>0){
					pageHtml += "<li><a href='#' id='prev'><<</a></li>";
				}
				for(var i = first; i<=last; i++){
					if(currentPage == i){
						pageHtml +="<li class='on'><a href='#' id='"+i+"'>"+i+"</a></li>";
					}else{
						pageHtml +="<li><a href='#' id='"+i+"'>"+i+"</a></li>";
					}	
				}
				if(last<totalPage){
					pageHtml +="<li><a href='#' id='next'>>></a></li>";
				}
				$("#pagingul").html(pageHtml);
				
				
				//페이징 번호 클릭 이벤트
				$(document).on("click","#pagingul li a",function(){
					let $id = $(this).attr("id");
					selectedPage=$(this).text();
					
					if($id=="next"){
						selectedPage = next;
					}if($id=="prev"){
						selectedPage = prev;
					}
					//전역변수에 선택한 페이지 번호를 담는다
					globalCurrentPage = selectedPage;
					//페이징 표시 재호출
					paging(totalData, dataPerPage, pageCount, selectedPage);
					//글 목록 표시 재호출
					displayData(selectedPage, dataPerPage);
				})
			}
			$(document).off("click","#delete").on("click","#delete",function(e){
				let category = "";
				var targetElement = e.target.parentElement;
				let deleteno = targetElement.getAttribute('data-value');
				let deleteclass = targetElement.getAttribute('class');
				if(deleteclass=="sdelete"){
					category = "free"
				}else if(deleteclass=="idelete"){
					category = "indi"
				}else if(deleteclass=="bdelete"){
					category = "busi"
				}
				$.ajax({
					type: 'post',
					url: 'deletelike',
					dataType: 'json',
					async:false,
					data: JSON.stringify({	
						"category" : category,
						"deleteno" : deleteno
					}),
					contentType: "application/json",
					success: function(result){
						if(result){
							alert("좋아요 취소되었습니다");
							refreshlist(category,globalCurrentPage);
						}else{
							alert("에러입니다");
						}
					},
					error:function(){alert("에러입니다");}
				})
			})
		},
		error:function(){alert("에러입니다.");}
	})
}
//모달 닫기
$("#lmodal_close_btn").click(function(){
	 document.getElementById("lmodal").style.display="none";
});
//날짜 
 $(function(){
$("#lpickup").datepicker({
	    language: 'ko',
	    timepicker: true,
	    timeFormat: "hh:ii AA"
	});	
 });
 $('#pagingul').on('click', 'a', function() { // listen for click on pagination link
	  if($(this).hasClass('active')) return false;

	  var active_elm = $('#pagingul a.active');

	  if(this.id == 'next'){
	    var _next = active_elm.parent().next().children('a');
	    if($(_next).attr('id') == 'next') {
	      this.removeClass('active');  
	      active_elm.addClass('active');

	      return; 
	    }
	    
	  }
	  else if(this.id == 'prev') {
		var _prev = active_elm.parent().prev().children('a');
	    if($(_prev).attr('id') == 'prev') {
			this.removeClass('active');
	    	active_elm.addClass('active');
	    };
	  } else {
	    $(this).addClass('active');
	  }
	  active_elm.removeClass('active');

	});
 
//주소 찾기
 function findAddr(){
 	new daum.Postcode({
         oncomplete: function(data) {
             // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
             // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
             // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
             var roadAddr = data.roadAddress; // 도로명 주소 변수
             
             // 우편번호와 주소 정보를 해당 필드에 넣는다.
            
             if(roadAddr !== ''){
                 document.getElementById("laddress").value = roadAddr;
                 
             } 
         }
     }).open();
 }
//msg 체크
	var msg ="${msg}";
	if(msg != ""){
		alert(msg);
	}
</script>
<script src="<c:url value='/static/js/info/datepicker.js'/>"></script>
<script src="<c:url value='/static/js/info/datepicker.ko.js'/>"></script>


</body>
</html>