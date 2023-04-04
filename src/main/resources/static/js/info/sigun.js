/*
시, 구 , 동 불러오기.
*/

jQuery(document).ready(function () {
			//sido option 추가
			jQuery.each(hangjungdong.sido, function (idx, code) {
				//append를 이용하여 option 하위에 붙여넣음
				jQuery('#sido').append(fn_option(code.sido, code.codeNm));
			});

			//sido 변경시 시군구 option 추가
			jQuery('#sido').change(function () {
				jQuery('#sigugun').show();
				jQuery('#sigugun').empty();
				jQuery('#sigugun').append(fn_option('', '선택')); //
				jQuery.each(hangjungdong.sigugun, function (idx, code) {
					if (jQuery('#sido > option:selected').val() == code.sido)
						jQuery('#sigugun').append(fn_option(code.sigugun, code.codeNm));
						jQuery('#sigugun option:eq("")').attr('selected', 'selected');
				});

				//세종특별자치시 예외처리
				//옵션값을 읽어 비교
				if (jQuery('#sido option:selected').val() == '36') {
					jQuery('#sigugun').hide();
					//index를 이용해서 selected 속성(attr)추가
					//기본 선택 옵션이 최상위로 index 0을 가짐
					jQuery('#sigugun option:eq(1)').attr('selected', 'selected');
					//trigger를 이용해 change 실행
					jQuery('#sigugun').trigger('change');
				}
			});

			//시군구 변경시 행정동 옵션추가
			jQuery('#sigugun').change(function () {
				var sido = jQuery('#sido option:selected');
				var sigugun = jQuery('#sigugun option:selected');
				
				//option중 선택을 기본으로 선택
				
				var dongName = sido.text() + ' '+ sigugun.text(); // 시도/시군구 이름
				document.getElementById('dongName').value=dongName;	
				
				
				
			});
/*
			jQuery('#dong').change(function () {
				var sido = jQuery('#sido option:selected');
				var sigugun = jQuery('#sigugun option:selected');
				var dong = jQuery('#dong option:selected');

				var dongName = sido.text() + ' '+ sigugun.text(); // 시도/시군구/읍면동 이름
				document.getElementById('dongName').value=dongName;	
				
				
				console.log(dongName);
				console.log(sido.text());
				console.log(sigugun.text());
				console.log(dong.text());
			});*/
		});

		function fn_option(code, name) {
			return '<option value="' + code + '">' + name + '</option>';
		}
		
		
		
		