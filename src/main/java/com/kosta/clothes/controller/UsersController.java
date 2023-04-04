package com.kosta.clothes.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.security.Auth;
import com.kosta.clothes.service.CertificationService;
import com.kosta.clothes.service.UsersService;
@Controller
public class UsersController {
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	CertificationService certificationService;
	

	
	//joinform.jsp 페이지로 이동
	@GetMapping("/joinform")
	public String joinform() {
		return "/user/joinform";
	}


	@Autowired
	HttpSession session;
	
	@Autowired
	UsersService usersService;
	

	//회원가입[개인]
	@PostMapping("/personnal")
	public String personnal(@ModelAttribute Users users, RedirectAttributes re) {
	
		try {
			certificationService.insertUsers(users); //사용자가 입력한 정보를 DB에 전달[Service에]
			re.addFlashAttribute("msg", "개인회원가입이 완료되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/login";
	}
	
	

	//회원가입[업체]
	@PostMapping("/businesss")
	public String business(@ModelAttribute Business business, RedirectAttributes re) {
		try {
			certificationService.insertBusiness(business); //사용자가 입력한 정보를 DB에 전달[Service에]
			re.addFlashAttribute("msg", "업체회원가입이 완료되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/login";
	}
	
	//동일 번호로 아이디 3개 이상 여부 체크
	/* 인증번호 */
	//본인 인증 !
    @ResponseBody
    @GetMapping("/main/execute")
    public String sendSMS(String phone) {
    		String numStr = "";
    	try {
			if(usersService.countPN(phone)) {//아이디 3개 이상일 때
				return "true";
				
			}else {//아이디 3개 이하일 때
				// 5자리 인증번호 만들기
		        Random random  = new Random();
		        for(int i=0; i<5; i++) {
		            String ranNum = Integer.toString(random.nextInt(10));   // 0부터 9까지 랜덤으로 숫자를 뽑는다.
		            numStr += ranNum;   // 랜덤으로 나온 숫자를 하나씩 누적해서 담는다.
		        }
		        // 문자 보내기
		        certificationService.certifiedPhoneNumber(phone , numStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} return numStr;
    }	

    
    //닉네임 중복 확인[개인]
    @ResponseBody
    @PostMapping("/nickname")
    public String checknick(Model model, @RequestParam("nickname") String nickname) {
    	try {
    		if(certificationService.checkId(nickname)) {
    			return "true"; //닉네임이 중복이라면 true값을 가져온다.
    		}else if(certificationService.checkBname(nickname)) {
    			return "true"; //업체쪽 닉네임도 조회(m)
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return "false";
    }

    //닉네임 중복 확인[업체]
    @ResponseBody
    @PostMapping("/bname")
    public String bname(Model model, @RequestParam("bname") String bname) {
    	try {
    		if(certificationService.checkBname(bname)) {
    			return "true"; //닉네임이 중복이라면 true값을 가져온다.
    		} else if(certificationService.checkId(bname)) {
    			return "true"; //개인쪽 닉네임도 조회(m)
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return "false";
    }

    
    //아이디 중복 확인[개인]
    @ResponseBody
    @PostMapping("/checkuserid")
    public String checkuserid(Model model, @RequestParam("checkuserid") String checkuserid) {
    	try {
    		if(certificationService.checkuserid(checkuserid)) {
    			return "true"; //아이디가 중복이라면 true값을 가져온다.
    		} else if(certificationService.businessidCheck(checkuserid)) {
    			return "true"; //업체쪽 아이디와도 중복체크(m)
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return "false";
    }

    //아이디 중복 확인[업체]
    @ResponseBody
    @PostMapping("/businessid")
    public String buserid(Model model, @RequestParam("businessid") String businessid) {
    	try {
    		if(certificationService.businessidCheck(businessid)) {
    			return "true"; //아이디가 중복이라면 true값을 가져온다.
    		} else if(certificationService.checkuserid(businessid)) {
    			return "true";//개인쪽 아이디와도 중복체크(m)
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return "false";
    }


    //로그인창
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
    	String referer = request.getHeader("Referer");
    	request.getSession().setAttribute("redirectURI", referer);
    	return "user/loginform";
    }
    //로그인 실패
    @GetMapping("/loginfail")
    public String loginfail(RedirectAttributes re) {
    	re.addFlashAttribute("result", "fail");
    	return "redirect:/login";
    }
    @PostMapping("/loginaction")
    public void loginaction() {
    	
    }
    @GetMapping("/logout")
    public void logout() {
    	
    }
    //로그인
//    @PostMapping("/login")
//    public String login(@RequestParam(value="id",required = true,defaultValue = "")String id, 
//    					@RequestParam(value="password",required = true,defaultValue = "")String password,
//    					Model model,
//    					HttpServletRequest request) {
//    	String url ="redirect:/";
//    	String reurl =(String)request.getSession().getAttribute("redirectURI");
//    	try {
//    		Users authUser=null;
//    		Business bauthUser = null;
//    		String userid = id;
//    		System.out.println("id:"+userid);
//    		System.out.println("password:"+password);
//    		authUser = usersService.login(userid,password);
//    		
//    		System.out.println("너냐11 : " +authUser);
//    		if(authUser == null) { //개인이 아닐 경우
//    			String businessid = id;
//    			String bpassword = password;
//    			System.out.println("bid:"+businessid);
//        		System.out.println("password:"+bpassword);
//        		bauthUser = usersService.blogin(businessid,bpassword);
//    		}
//    		if(authUser == null && bauthUser==null) { //개인 업체 둘 다 아닐 경우
//    			model.addAttribute("result", "fail");
//				return "/user/loginform";
//    		}else if(authUser != null){
//    			session.setAttribute("authUser", authUser);
//    		}else {
//    		session.setAttribute("authUser", bauthUser);
//    		}
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    	}
//    	if(reurl==null) {
//    		return url;
//    	}
//    	return "redirect:"+reurl;
//    }
  //로그아웃
//  @RequestMapping(value="/logout",method = RequestMethod.GET)
//  public String logout(HttpSession session) {
//	  session.removeAttribute("authUser");
//	  return "redirect:/";
//  }
  //아이디찾기창
  @GetMapping("/searchid")
  public String searchId() {
	  return "user/searchid";
  }
  //아이디찾기
  @PostMapping("/searchid")
  public String searchId(@RequestParam("ph")String ph, Model model) {
	  try {
//		 List<List<String>> idList = new ArrayList<>();;
//		 List<Map<String, Object>> fuId = new ArrayList<>();
//		 List<Map<String, Object>> fbId = new ArrayList<>();
		 List<String> fuId = new ArrayList<>();
		 List<String> fbId = new ArrayList<>();
		 String phone = ph; 
		 fuId = usersService.findUserId(phone); //개인 아이디 검색
		 model.addAttribute("user", fuId);
		 String bphone = ph;
		 fbId = usersService.findBusinessId(bphone); //업체 아이디 검색
		 model.addAttribute("business", fbId);
		 
		 if(fuId.size()==0&&fbId.size()==0) { // 검색 결과 둘 다 없을 때
			 model.addAttribute("msg", "정보와 일치하는 아이디가 없습니다.");
			 return "user/searchid";
		 }
		  
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
	  return "user/searchidresult";
  }
  //아이디와 전화번호 체크창
  @GetMapping("/checkidnphone")
  public String checkIdnPhone() {
	  return "user/checkidnphone";
  }
  //아이디와 전화번호 체크
  @PostMapping("/checkidnphone")
  public String checkIdnPhone(@RequestParam("id")String id,@RequestParam("phone")String ph, Model model) {
	  try {
		  String userid = null;
		  String businessid = null;
		  String phone = null;
		  String bphone = null;
		  String cbid = null;
		  userid = id;
		  phone = ph;
		  String cuid = usersService.checkUserIdnPhone(userid, phone); //아이디와 전화번호로 일치하는 회원 찾기
		  if(cuid!=null) {
			  model.addAttribute("id", cuid);
		  } else if(cuid==null) {
			  businessid = id;
			  bphone = ph;
			  cbid = usersService.checkBusinessIdnPhone(businessid, bphone);  //아이디와 전화번호로 일치하는 회원 찾기
			  model.addAttribute("id", cbid);
		  }
		  if(cuid==null&&cbid==null) {
			  model.addAttribute("msg", "정보와 일치하는 회원이 없습니다.");
				 return "user/checkidnphone";
		  }
		  
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
	  return "user/changepass";
  }
  //새 비밀번호
  @PostMapping("/changepass")
  public String changePass(@RequestParam("id")String id, 
		  @RequestParam("password") String password, RedirectAttributes re) {
	  try { 
		  boolean cuserid = usersService.checkuserid(id); //개인 테이블 아이디 확인
		  boolean cbusinessid = usersService.businessidCheck(id); // 업체 테이블 아이디 확인
		  if(cuserid) {//개인 테이블에 아이디가 있을 때
		  usersService.changePass(id, password); //비밀번호 변경
	  	  }else if(cbusinessid) {//업체 테이블에 아이디가 있을 때
	  	  String bpassword = password;
	  	  usersService.changebPass(id, bpassword); // 비밀번호 변경
	  	  }
	  	  else {
	  		  re.addFlashAttribute("msg", "비밀번호 수정에 실패했습니다.");
	  		  return "redirect:/changepass";
	  	  }
		  
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
	  return "redirect:/login";
  }
  
//비밀번호 확인
  @GetMapping("/passcheck")
  public String passcheck(Model model) {
	if(session.getAttribute("authUser")==null) { //로그인 안되어있다면 로그인 페이지로!
		model.addAttribute("msg", "로그인이 필요합니다");
		return "user/loginform";
	}
  	return "user/checkpass";
  }
  
  //회원정보 수정 jsp이동
  @Auth
  @PostMapping("upasscheck")
  public String upasscheck(@RequestParam("pass") String pass ,Model model){
	 try {
		 Users uauthuser = (Users) session.getAttribute("authUser");
		 String id = uauthuser.getUserid();
		 Integer userno= uauthuser.getUserno();
		 uauthuser = usersService.selectuAll(userno);
		 uauthuser.setPassword(null);
		 boolean check = usersService.checkupass(id, pass);
		 if(check) {
			 model.addAttribute("Uauthuser", uauthuser);
		 }else {
			 model.addAttribute("msg","비밀번호가 일치하지 않습니다.");
			 return "user/checkpass";
		 }
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
	 return "user/modifyuser";
  }
  
  //개인회원정보 수정
  @PostMapping("modifyuser")
  public String modifyuser(@ModelAttribute Users user, RedirectAttributes re) {
	  try {
		  usersService.modifyuser(user);
		  session.removeAttribute("authUser");
		  re.addFlashAttribute("msg","회원정보 수정 완료!");
	  }catch (Exception e) {
		  e.printStackTrace();		  
	  }
	  return "redirect:/login";
  }
  



//업체 회원정보 수정 jsp이동
  @Auth
  @PostMapping("bpasscheck")
  public String bpasscheck(@RequestParam("pass") String pass ,Model model){
	 try {
		 Business bauthuser = (Business) session.getAttribute("authUser");
		 String id = bauthuser.getBusinessid();
		 Integer bno = bauthuser.getBno();
		 bauthuser = usersService.selectbAll(bno);
		 bauthuser.setBpassword(null);
		 boolean check = usersService.checkbpass(id, pass);//업체 패스워드 체크
		 if(check) {
			 model.addAttribute("Bauthuser", bauthuser);
		 }else {
			 model.addAttribute("msg","비밀번호가 일치하지 않습니다.");
			 return "user/checkpass";
		 }
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
	 return "user/modifybusiness";
  }
  
//업체회원정보 수정
  @PostMapping("modifybusiness")
  public String modifyuser(@ModelAttribute Business business, RedirectAttributes re) {
	  try {
		  usersService.modifybusiness(business); //업체 정보 수정
		  session.removeAttribute("authUser");
		  re.addFlashAttribute("msg","회원정보 수정 완료");
	  }catch (Exception e) {
		  e.printStackTrace();		  
	  }
	  return "redirect:/login";
  }
//업체회원 탈퇴
  
  @PostMapping("bretire")
  public String bretire(RedirectAttributes re) {
	  try {
		  Business bauthuser = (Business) session.getAttribute("authUser");
		  Integer bno = bauthuser.getBno();
		  usersService.deletebusiness(bno);
		  session.removeAttribute("authUser");
		  re.addFlashAttribute("msg","탈퇴 완료");
	  }catch (Exception e) {
		  e.printStackTrace();		  
	  }
	  return "redirect:/";
  }
  
 //개인회원 탈퇴
  
  @PostMapping("uretire")
  public String uretire(RedirectAttributes re) {
	  try {
		  Users uauthuser = (Users) session.getAttribute("authUser");
		  Integer userno = uauthuser.getUserno();
		  usersService.deleteuser(userno);
		  session.removeAttribute("authUser");
		  re.addFlashAttribute("msg","탈퇴 완료!!");
	  }catch (Exception e) {
		  e.printStackTrace();		  
	  }
	  return "redirect:/";
  }
}

