package com.kosta.clothes.controller;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kosta.clothes.service.CertificationService;
@Controller
public class UsersController {
	
	@Autowired
	CertificationService certificationService;
	
	
	//회원가입페이지 제작확인용입니다. 후에 joinform.jsp와 joinformview.jsp 합치고 삭제합니다!
	@GetMapping("/joinformview")
	public String joinForm() {
		return "/user/joinformview";
	}
	
	
	
	
	/* 인증번호 */
	//본인 인증 !
    @ResponseBody
    @GetMapping("/main/execute")
    public String sendSMS(String userPhoneNum) {
        // 5자리 인증번호 만들기
        Random random  = new Random();
        String numStr = "";
        for(int i=0; i<5; i++) {
            String ranNum = Integer.toString(random.nextInt(10));   // 0부터 9까지 랜덤으로 숫자를 뽑는다.
            numStr += ranNum;   // 랜덤으로 나온 숫자를 하나씩 누적해서 담는다.
        }
        // 확인용
        System.out.println("수신자 번호 : " + userPhoneNum);
        System.out.println("인증번호 : " + numStr);
        // 문자 보내기
        certificationService.certifiedPhoneNumber(userPhoneNum , numStr);
        return numStr;    // 인증번호 반환
    }
    
    //닉네임 중복 확인!
    @ResponseBody
    @PostMapping("/checknick")
    public String checknick(Model model, @RequestParam("checknick") String checknick) {
    	System.out.println("nickname" + checknick);
    	try {
    		if(certificationService.checkId(checknick)) {
    			return "true"; //닉네임이 중복이라면 true값을 가져온다.
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return "false";
    }
    
    //이메일 중복 확인!
    @ResponseBody
    @PostMapping("/checkemail")
    public String checkemail(Model model, @RequestParam("checkemail") String checkemail) {
    	System.out.println("nickname" + checkemail);
    	try {
    		if(certificationService.checkEmail(checkemail)) {
    			return "true"; //닉네임이 중복이라면 true값을 가져온다.
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return "false";
    }
}    

