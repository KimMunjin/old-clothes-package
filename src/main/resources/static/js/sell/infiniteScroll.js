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
			url: 'sellInfiniteScrollDown',
			dataType: 'json',
			data: JSON.stringify({
				ino: lastsno,
				keyword: keyword
			}),
			contentType: "application/json",
			success: function(data){
				let str="";
				if(data!=""){
					$(data).each(
						function(){
							str += "<a href="+"'sellView/"+this.ino+"' id='sellListCard'>"
							str	+= "<div class="+"'card'"+" data-sno='"+this.ino+"'>";
							str += "<div class='individualCard-header'>"+this.idealType+"</div>";
		          			str	+= "<div class="+"'card-image'"+">";
		          			if(this.istatus == '거래 완료'){
								str += "<div class="+"'individualStatus'"+">"+this.istatus+"</div>";
							}
		          			if(this.ifileids!=null && this.ifileids !=""){
								str	+= "<img src="+"'upload/"+this.ifileids+"' alt="+"'무료나눔 옷'"+">";
							} else {
								str	+= "<img src="+"'image/logo3.png'"+" alt="+"'로고'"+">";
							}
		          			str	+= "</div>";
		          			str	+= "<div class="+"'card-body'"+">";
		              		str	+= "<div class="+"'priceAndDate'"+"><span class="+"'price'"+">"+this.price+"원</span><span class="+"'date'"+">"+this.regDate+"</span></div>";
		              		str	+= "<h2 class="+"'sharingTitle'"+">"+this.ititle+"</h2>";
		              		str += "<div class='individualDealNick'><p><img src='/image/greenuser.png'>"+this.nickname+"</p></div>";
		          			str	+= "</div>";
							str	+= "<div class="+"'card-footer'"+">";
							const address = this.iaddress.split(" ");
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

	



