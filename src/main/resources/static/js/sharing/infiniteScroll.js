// 스크롤 전 좌표
let loading = false;
let lastScrollTop = 0;

$(window).scrollTop = 0;

$(window).scroll(function(){
	//스크롤 후 좌표
	let currentScrollTop = $(window).scrollTop();
	//다운 스크롤
	if(currentScrollTop + $(window).height() + 100 >=$(document).height()){
		surveyList();
		
		
		
	}
})

function surveyList(){
	if(!loading){
		loading = true;
		
		let lastsno = $(".card:last").attr("data-sno");
		let keyword = document.getElementById('keyword').value;
		$.ajax({
			type: 'post',
			url: 'infiniteScrollDown',
			dataType: 'json',
			data: JSON.stringify({
				sno: lastsno,
				keyword: keyword
			}),
			contentType: "application/json",
			success: function(data){
				let str="";
				if(data!=""){
					$(data).each(
						function(){
							str += "<a href="+"'sharingView/"+this.sno+"' id='sharingListCard'>"
							str	+= "<div class="+"'card'"+" data-sno='"+this.sno+"'>";
							str += "<div class='sharingCard-header'>"+this.sdealType+"</div>"
		          			str	+= "<div class="+"'card-image'"+">";
		          			if(this.sstatus == '거래 완료'){
								str += "<div class="+"'sharingStatus'"+">"+this.sstatus+"</div>";
							}
		          			if(this.sfileids!=null && this.sfileids !=""){
								str	+= "<img src="+"'upload/"+this.sfileids+"' alt="+"'무료나눔 옷'"+">";
							} else {
								str	+= "<img src="+"'image/logo3.png'"+" alt="+"'로고'"+">";
							}
		          			str	+= "</div>";
		          			str	+= "<div class="+"'card-body'"+">";
		              		str	+= "<span class="+"'date'"+">"+this.regDate+"</span>";
		              		str	+= "<h2 class="+"'sharingTitle'"+">"+this.stitle+"</h2>";
		              		str += "<div class='sharingDealNick'><p><img src='/image/yellowuser.png'>"+this.nickname+"</p></div>";
		          			str	+= "</div>";
							str	+= "<div class="+"'card-footer'"+">";
							const address = this.saddress.split(" ");
		              		if(address[2].match("^.*.구$")!=null){
								str+="<img src='/image/pin.png'><p>"+address[0]+" "+address[1]+" "+address[2]+" "+address[3]+"</p>";
							}
		              		else{
								str+="<img src='/image/pin.png'><p>"+address[0]+" "+address[1]+" "+address[2]+"</p>";
							}
							str	+= "</div>";
		          			str	+= "</div>";
		          			str	+= "</div>";
		          			str += "</a>";
						});
						$(".card-list").append(str);
						loading = false;
				}
			}
		});
	}
}

	



