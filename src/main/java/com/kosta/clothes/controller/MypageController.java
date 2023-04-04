package com.kosta.clothes.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosta.clothes.bean.Apply;
import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.MessageVO;
import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.bean.Review;
import com.kosta.clothes.bean.Sell;
import com.kosta.clothes.bean.Sharing;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.service.ApplyService;
import com.kosta.clothes.service.BusinessService;
import com.kosta.clothes.service.MessageService;
import com.kosta.clothes.service.MypageService;
import com.kosta.clothes.service.ReviewService;
import com.kosta.clothes.service.SellService;
import com.kosta.clothes.service.SharingService;

@Controller
public class MypageController {

	@Autowired 
	MessageService messageService;
	@Autowired
	SharingService sharingService;
	@Autowired
	SellService sellService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	MypageService mypageService;
	@Autowired
	ApplyService applyService;
	@Autowired
	BusinessService businessService;
	@Autowired
	HttpSession session;
	
	/*
	 * @GetMapping ("/mypage") String main() { return "/mypage/mypage"; }
	 */
	@GetMapping ("/mypage/bmypage/{bno}")
	String main(@PathVariable("bno") Integer bno,Model model) {
		List<Apply> apply = null;
		try {
			if(session.getAttribute("authUser")!=null) {//사용자가 로그인 했을 때 
	              if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
	                 Business business = messageService.mypageBusiness(bno);
	                  model.addAttribute("business",business);
	                  
	                  //신청목록개수
	                  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	                  
	                  //수거상태개수
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("ccount",ccount);
	                  
	              }else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {//사업자가 로그인 했을 때
	                  Business business = messageService.mypageBusiness(bno);
	                  model.addAttribute("business",business);
	                  
	                  //사용자->사업자 신청
	                  apply = applyService.getBapply(bno);
	                  model.addAttribute("apply",apply);
	                  //사업자 신청목록개수
	                  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	              
	                  //사업자개수개수
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer dcount= (Integer) astatuscount.get("신청중");
	                  Integer ecount= (Integer) astatuscount.get("신청완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("dcount",dcount);
	                  model.addAttribute("ecount",ecount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("ccount",ccount);
	                  
	              }else {//로그인 안했을 때
	               Business business = messageService.mypageBusiness(bno);
	               model.addAttribute("business",business);  
	               
	               Integer applycount = applyService.applycount(bno);
	               model.addAttribute("applycount",applycount);
	               
	               //사업자 개수
	               Map astatuscount= applyService.astatuscount(bno);
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("ccount",ccount);
	              }
	         }else {
	            Business business = messageService.mypageBusiness(bno);
	             model.addAttribute("business",business);

                 //신청목록
                 Integer applycount = applyService.applycount(bno);
                 model.addAttribute("applycount",applycount);
                 
                 //수거상태
                 Map astatuscount= applyService.astatuscount(bno);
                 Integer acount= (Integer) astatuscount.get("후기등록완료");
                 Integer bcount= (Integer) astatuscount.get("신청거절");
                 Integer ccount= (Integer) astatuscount.get("수거완료");
                 model.addAttribute("acount",acount);
                 model.addAttribute("bcount",bcount);
                 model.addAttribute("ccount",ccount);
                 
	         }
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/mypage/bmypage";
	}
	//사업자 신청목록 탭
	@GetMapping ("/mypage/bmypage/{bno}/apply")
	String bmypageapply(@PathVariable("bno") Integer bno, Model model,
			@RequestParam(value = "bapage", required = false, defaultValue = "1") Integer bapage) {
		PageInfo bapageInfo = new PageInfo();
		List<Apply> apply = null;
		try {
			if(session.getAttribute("authUser")!=null) {//사용자가 로그인 했을 때 
	              if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
	            	  //페이징
	            	  apply = applyService.bgetApplyList(bno, bapage, bapageInfo);
	            	  model.addAttribute("apply",apply);
	                  model.addAttribute("bapageInfo", bapageInfo);
	                  
	                 Business business = messageService.mypageBusiness(bno);
	                  model.addAttribute("business",business);
	                  
	                  //신청목록
	                  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	                  
	                  //수거상태
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("ccount",ccount);
	                  
	                  
	              }else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {//사업자가 로그인 했을 때
	            	  //페이징
	            	  apply = applyService.bgetApplyList(bno, bapage, bapageInfo);
	            	  model.addAttribute("apply",apply);
	                  model.addAttribute("bapageInfo", bapageInfo);
	                	
	                  Business business = messageService.mypageBusiness(bno);
	                  model.addAttribute("business",business);
	                  
	                 
	                  //신청목록
	                  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	                  
	                  //수거상태
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer dcount= (Integer) astatuscount.get("신청중");
	                  Integer ecount= (Integer) astatuscount.get("신청완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("dcount",dcount);
	                  model.addAttribute("ecount",ecount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("ccount",ccount);
	                      
	              }else {//로그인 안했을 때
	            	  //페이징
	            	  apply = applyService.bgetApplyList(bno, bapage, bapageInfo);
	            	  model.addAttribute("apply",apply);
	                  model.addAttribute("bapageInfo", bapageInfo);
	                	            	  
		              Business business = messageService.mypageBusiness(bno);
		              model.addAttribute("business",business);  
	               

	                  //신청목록
	                  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	                  
	                  //수거상태
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("ccount",ccount);
	                  
	              }
	         }else {
	        	  //페이징
           	     apply = applyService.bgetApplyList(bno, bapage, bapageInfo);
           	  	 model.addAttribute("apply",apply);
                 model.addAttribute("bapageInfo", bapageInfo);
               
                 
                 
	             Business business = messageService.mypageBusiness(bno);
	             model.addAttribute("business",business);

                 //신청목록
                 Integer applycount = applyService.applycount(bno);
                 model.addAttribute("applycount",applycount);
                 
                 //수거상태
                 Map astatuscount= applyService.astatuscount(bno);
                 Integer acount= (Integer) astatuscount.get("후기등록완료");
                 Integer bcount= (Integer) astatuscount.get("신청거절");
                 Integer ccount= (Integer) astatuscount.get("수거완료");
                 model.addAttribute("acount",acount);
                 model.addAttribute("bcount",bcount);
                 model.addAttribute("ccount",ccount);
                 
	         }
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/mypage/bapplylist";
	}
	//사업자 거래 후기 탭
	//마이페이지 거래후기(사업자)
	@GetMapping ("/mypage/bmypage/{bno}/review")
	String bmypagereview(@PathVariable("bno") Integer bno, Model model,
			@RequestParam(value = "rpage", required = false, defaultValue = "1") Integer rpage) {
		List<Review> reviewList = null;
		PageInfo rpageInfo = new PageInfo();
		try {
			if(session.getAttribute("authUser")!=null) {//사용자가 로그인 했을 때 
	              if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
	                  Business business = messageService.mypageBusiness(bno);
	                  model.addAttribute("business",business);
	         
	                  //신청목록
	                  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	                  
	                  //거래후기
	                  reviewList = reviewService.getBReviewList(bno, rpage, rpageInfo);
	                  model.addAttribute("reviewList", reviewList);
	                  model.addAttribute("rpageInfo", rpageInfo);
	                  
	                  //수거상태
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("ccount",ccount);
	                  
	              }else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {//사업자가 로그인 했을 때
	                  Business business = messageService.mypageBusiness(bno);
	                  model.addAttribute("business",business);

	                  //신청목록
	                  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	                  
	                  //거래후기
	                  reviewList = reviewService.getBReviewList(bno, rpage, rpageInfo);
	                  model.addAttribute("reviewList", reviewList);
	                  model.addAttribute("rpageInfo", rpageInfo);
	           
	                  //수거상태
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer dcount= (Integer) astatuscount.get("신청중");
	                  Integer ecount= (Integer) astatuscount.get("신청완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("dcount",dcount);
	                  model.addAttribute("ecount",ecount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("ccount",ccount);
	                  
	              }else {//로그인 안했을 때
	            	  Business business = messageService.mypageBusiness(bno);
	            	  model.addAttribute("business",business);  
	            	  
	            	  //신청목록
	            	  Integer applycount = applyService.applycount(bno);
	                  model.addAttribute("applycount",applycount);
	                  
	                  //거래후기
	                  reviewList = reviewService.getBReviewList(bno, rpage, rpageInfo);
	                  model.addAttribute("reviewList", reviewList);
	                  model.addAttribute("rpageInfo", rpageInfo);
	                  
	                  //수거상태
	                  Map astatuscount= applyService.astatuscount(bno);
	                  Integer acount= (Integer) astatuscount.get("후기등록완료");
	                  Integer bcount= (Integer) astatuscount.get("신청거절");
	                  Integer ccount= (Integer) astatuscount.get("수거완료");
	                  model.addAttribute("acount",acount);
	                  model.addAttribute("bcount",bcount);
	                  model.addAttribute("ccount",ccount);
	              }
	         }else {
                 //거래후기
                 reviewList = reviewService.getBReviewList(bno, rpage, rpageInfo);
                 model.addAttribute("reviewList", reviewList);
                 model.addAttribute("rpageInfo", rpageInfo);
                 
                 Map astatuscount= applyService.astatuscount(bno);
                 Integer acount= (Integer) astatuscount.get("후기등록완료");
                 Integer bcount= (Integer) astatuscount.get("신청거절");
                 Integer ccount= (Integer) astatuscount.get("수거완료");
                 model.addAttribute("acount",acount);
                 model.addAttribute("bcount",bcount);
                 model.addAttribute("ccount",ccount);
                 
                 Integer applycount = applyService.applycount(bno);
                 model.addAttribute("applycount",applycount);
	         }
			Business business = messageService.mypageBusiness(bno);
			model.addAttribute("business",business);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/mypage/breview";
	}
	//사용자 마이페이지 이동
	   @GetMapping ("mypage/umypage/{userno}")
	   String umypage(@PathVariable("userno") Integer userno,Model model) {
	      try {
	         if(session.getAttribute("authUser")!=null) {
	            if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
            	 //users 값을 가져온다
	             Users users = mypageService.getMypage(userno);
	             model.addAttribute("users",users);	            	
	             
	             Users users1 = (Users) session.getAttribute("authUser");
	            //상품등록
	             Integer sharingcount = sharingService.sharingcount(userno);
	             Integer sellcount = sellService.sellcount(userno);
	             Integer totalcount = sharingcount + sellcount;
	             model.addAttribute("totalcount",totalcount);

	             //거래후기
	             Integer reviewcount = reviewService.reviewcount(userno);
	             model.addAttribute("reviewcount",reviewcount);       
	            	             
	            }else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {
	            	//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
	            	
	            	//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);

	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       

	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
	               
	            }else {
	            	//users 값을 가져온다
		             Users users = mypageService.getMypage(userno);
		             model.addAttribute("users",users);

		           //상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);

	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       

	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);

	            }
	         }else {

	        	 //users 값을 가져온다
	        	 Users users = mypageService.getMypage(userno);
	        	 model.addAttribute("users",users);
	            //상품등록
	             Integer sharingcount = sharingService.sharingcount(userno);
	             Integer sellcount = sellService.sellcount(userno);
	             Integer totalcount = sharingcount + sellcount;
	             model.addAttribute("totalcount",totalcount);

	             //거래후기
	             Integer reviewcount = reviewService.reviewcount(userno);
	             model.addAttribute("reviewcount",reviewcount);       
	             //거래완료
	            Integer statuscount = sharingService.statuscount(userno);
	            statuscount +=sellService.statuscount(userno);
	            model.addAttribute("statuscount",statuscount);

	         }
	      }catch(Exception e) {
	         e.printStackTrace();
	      }
	      return "/mypage/usermypage";
	   }
	   //사용자 거래 후기 탭 

	//마이페이지 거래후기(개인)
	@GetMapping ("mypage/umypage/{userno}/review")
	public String umypage(@PathVariable("userno") Integer userno, Model model,
			@RequestParam(value = "rpage", required = false, defaultValue = "1") Integer rpage) {
		PageInfo rpageInfo = new PageInfo();
		List<Review> reviewList;
		try {
			if(session.getAttribute("authUser")!=null) {
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					reviewList = reviewService.getReviewList(userno, rpage, rpageInfo);
					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);

	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       
	                model.addAttribute("rpageInfo", rpageInfo);
	                model.addAttribute("reviewList", reviewList);					

	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);

				}else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					reviewList = reviewService.getReviewList(userno, rpage, rpageInfo);
					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);

	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       
	                model.addAttribute("rpageInfo", rpageInfo);
	                model.addAttribute("reviewList", reviewList);	

	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
				}
			}else {
				//users 값을 가져온다
	            Users users = mypageService.getMypage(userno);
	            model.addAttribute("users",users);
				
				reviewList = reviewService.getReviewList(userno, rpage, rpageInfo);
				//상품등록
                Integer sharingcount = sharingService.sharingcount(userno);
                Integer sellcount = sellService.sellcount(userno);
                Integer totalcount = sharingcount + sellcount;
                model.addAttribute("totalcount",totalcount);

                //거래후기
                Integer reviewcount = reviewService.reviewcount(userno);
                model.addAttribute("reviewcount",reviewcount);       
                model.addAttribute("rpageInfo", rpageInfo);
                model.addAttribute("reviewList", reviewList);		

                //거래완료
                Integer statuscount = sharingService.statuscount(userno);
                statuscount +=sellService.statuscount(userno);
                model.addAttribute("statuscount",statuscount);

			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/mypage/review";
	}
	//사용자 판매목록 탭
	@GetMapping ("mypage/umypage/{userno}/sell")
	public String umypagesell(@PathVariable("userno") Integer userno, Model model,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "spage", required = false, defaultValue = "1") Integer spage) {
		PageInfo pageInfo = new PageInfo();
		PageInfo spageInfo = new PageInfo();
		try {
			/*판매 상품(개인판매)*/
			List<Sell> sellList;
			/* 판매 상품(무료나눔) */
			List<Sharing> sharingList;
			if(session.getAttribute("authUser")!=null) {
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
					Users users1 = (Users) session.getAttribute("authUser");
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					sellList = mypageService.getSellList(userno,page,pageInfo);
					for (int i = 0; i < sellList.size(); i++) {
						/* 주소 띄우기 */
						String addr = sellList.get(i).getIaddress();
						String[] addChange = addr.split(" ");
						if(addChange[2].matches("^.*.구$")) {
							String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
							sellList.get(i).setIaddress(join1);
						} else {
							String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
							sellList.get(i).setIaddress(join2);
						}
						if (sellList.get(i).getIfileids() != null) {
							sellList.get(i).setIfileids(sellList.get(i).getIfileids().split(",")[0]);
						}
					}
					

					

					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);
	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       
	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
					model.addAttribute("pageInfo", pageInfo);
					model.addAttribute("sellList", sellList);
					sharingList = mypageService.getSharingList(userno,spage,spageInfo);
					for (int i = 0; i < sharingList.size(); i++) {
						/* 주소 띄우기*/
						String addr = sharingList.get(i).getSaddress();
						String[] addChange = addr.split(" "); //주소 공백으로 분
						if(addChange[2].matches("^.*.구$")) { //세 번째 단어에서 '구'로 끝나면 동까지 입력 
							String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
							sharingList.get(i).setSaddress(join1);
						} else {
							String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
							sharingList.get(i).setSaddress(join2);
						}
						if (sharingList.get(i).getSfileids() != null) {
							sharingList.get(i).setSfileids(sharingList.get(i).getSfileids().split(",")[0]);
						}
					}
					model.addAttribute("spageInfo", spageInfo);
					model.addAttribute("sharingList", sharingList);
				} else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {
					 //users 값을 가져온다
		             Users users = mypageService.getMypage(userno);
		             model.addAttribute("users",users);
		             
					 sellList = mypageService.getSellList(userno,page,pageInfo);
					 for (int i = 0; i < sellList.size(); i++) {
						 /* 주소 띄우기 */
							String addr = sellList.get(i).getIaddress();
							String[] addChange = addr.split(" ");
							if(addChange[2].matches("^.*.구$")) {
								String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
								sellList.get(i).setIaddress(join1);
							} else {
								String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
								sellList.get(i).setIaddress(join2);
							}
							if (sellList.get(i).getIfileids() != null) {
								sellList.get(i).setIfileids(sellList.get(i).getIfileids().split(",")[0]);
							}
						}
					 	//상품등록
		                Integer sharingcount = sharingService.sharingcount(userno);
		                Integer sellcount = sellService.sellcount(userno);
		                Integer totalcount = sharingcount + sellcount;
		                model.addAttribute("totalcount",totalcount);
		                //거래후기
		                Integer reviewcount = reviewService.reviewcount(userno);
		                model.addAttribute("reviewcount",reviewcount);       
		                //거래완료
		                Integer statuscount = sharingService.statuscount(userno);
		                statuscount +=sellService.statuscount(userno);
		                model.addAttribute("statuscount",statuscount);
						model.addAttribute("pageInfo", pageInfo);
						model.addAttribute("sellList", sellList);
						sharingList = mypageService.getSharingList(userno,spage,spageInfo);
					 for (int i = 0; i < sharingList.size(); i++) {
							/* 주소 띄우기*/
							String addr = sharingList.get(i).getSaddress();
							String[] addChange = addr.split(" "); //주소 공백으로 분
							if(addChange[2].matches("^.*.구$")) { //세 번째 단어에서 '구'로 끝나면 동까지 입력 
								String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
								sharingList.get(i).setSaddress(join1);
							} else {
								String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
								sharingList.get(i).setSaddress(join2);
							}
							if (sharingList.get(i).getSfileids() != null) {
								sharingList.get(i).setSfileids(sharingList.get(i).getSfileids().split(",")[0]);
							}
						}
					 model.addAttribute("spageInfo", spageInfo);
					 model.addAttribute("sharingList", sharingList);
				}else {
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					sellList = mypageService.getSellList(userno,page,pageInfo);
					for (int i = 0; i < sellList.size(); i++) {
						/* 주소 띄우기 */
						String addr = sellList.get(i).getIaddress();
						String[] addChange = addr.split(" ");
						if(addChange[2].matches("^.*.구$")) {
							String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
							sellList.get(i).setIaddress(join1);
						} else {
							String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
							sellList.get(i).setIaddress(join2);
						}
						if (sellList.get(i).getIfileids() != null) {
							sellList.get(i).setIfileids(sellList.get(i).getIfileids().split(",")[0]);
						}
					}
					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);
	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       
	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
					model.addAttribute("pageInfo", pageInfo);
					model.addAttribute("sellList", sellList);
					sharingList = mypageService.getSharingList(userno,spage,spageInfo);
					for (int i = 0; i < sharingList.size(); i++) {
						/* 주소 띄우기*/
						String addr = sharingList.get(i).getSaddress();
						String[] addChange = addr.split(" "); //주소 공백으로 분
						if(addChange[2].matches("^.*.구$")) { //세 번째 단어에서 '구'로 끝나면 동까지 입력 
							String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
							sharingList.get(i).setSaddress(join1);
						} else {
							String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
							sharingList.get(i).setSaddress(join2);
						}
						if (sharingList.get(i).getSfileids() != null) {
							sharingList.get(i).setSfileids(sharingList.get(i).getSfileids().split(",")[0]);
						}
					}
					model.addAttribute("spageInfo", spageInfo);
					model.addAttribute("sharingList", sharingList);
				}
			}else {
				//users 값을 가져온다
	            Users users = mypageService.getMypage(userno);
	            model.addAttribute("users",users);
	            
				sellList = mypageService.getSellList(userno,page,pageInfo);
				for (int i = 0; i < sellList.size(); i++) {
					/* 주소 띄우기 */
					String addr = sellList.get(i).getIaddress();
					String[] addChange = addr.split(" ");
					if(addChange[2].matches("^.*.구$")) {
						String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
						sellList.get(i).setIaddress(join1);
					} else {
						String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
						sellList.get(i).setIaddress(join2);
					}
					if (sellList.get(i).getIfileids() != null) {
						sellList.get(i).setIfileids(sellList.get(i).getIfileids().split(",")[0]);
					}
				}
				//상품등록
                Integer sharingcount = sharingService.sharingcount(userno);
                Integer sellcount = sellService.sellcount(userno);
                Integer totalcount = sharingcount + sellcount;
                model.addAttribute("totalcount",totalcount);
                //거래후기
                Integer reviewcount = reviewService.reviewcount(userno);
                model.addAttribute("reviewcount",reviewcount);       
                //거래완료
                Integer statuscount = sharingService.statuscount(userno);
                statuscount +=sellService.statuscount(userno);
                model.addAttribute("statuscount",statuscount);
				model.addAttribute("pageInfo", pageInfo);
				model.addAttribute("sellList", sellList);
				sharingList = mypageService.getSharingList(userno,spage,spageInfo);
				for (int i = 0; i < sharingList.size(); i++) {
					/* 주소 띄우기*/
					String addr = sharingList.get(i).getSaddress();
					String[] addChange = addr.split(" "); //주소 공백으로 분
					if(addChange[2].matches("^.*.구$")) { //세 번째 단어에서 '구'로 끝나면 동까지 입력 
						String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
						sharingList.get(i).setSaddress(join1);
					} else {
						String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
						sharingList.get(i).setSaddress(join2);
					}
					if (sharingList.get(i).getSfileids() != null) {
						sharingList.get(i).setSfileids(sharingList.get(i).getSfileids().split(",")[0]);
					}
				}
				model.addAttribute("spageInfo", spageInfo);
				model.addAttribute("sharingList", sharingList);				
			}
			Users users = mypageService.getMypage(userno);
			 model.addAttribute("users",users);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/mypage/sellProduct";
	}
	//사용자 구매목록 탭
	@GetMapping ("mypage/umypage/{userno}/buy")
	public String umypagebuy(@PathVariable("userno") Integer userno,Model model, 
			@RequestParam(value = "bspage", required = false, defaultValue = "1") Integer bspage,
			@RequestParam(value = "ppage", required = false, defaultValue = "1") Integer ppage) {
		PageInfo bspageInfo = new PageInfo();
		PageInfo ppageInfo = new PageInfo();
		try {
			/* 구매 상품(개인판매) */
			List<Sell> buysellList;
			/* 구매 상품(무료나눔) */
			List<Sharing> buysharingList;
			if(session.getAttribute("authUser")!=null) {
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
					 //users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
				    Users users1 = (Users) session.getAttribute("authUser");
				//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);
	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       
	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
				 buysellList = mypageService.getBuySellList(userno,bspage,bspageInfo);
				 for (int i = 0; i < buysellList.size(); i++) {
					 /* 주소 띄우기 */
						String addr = buysellList.get(i).getIaddress();
						String[] addChange = addr.split(" ");
						if(addChange[2].matches("^.*.구$")) {
							String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
							buysellList.get(i).setIaddress(join1);
						} else {
							String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
							buysellList.get(i).setIaddress(join2);
						}
						if (buysellList.get(i).getIfileids() != null) {
							buysellList.get(i).setIfileids(buysellList.get(i).getIfileids().split(",")[0]);
						}
					}
				 model.addAttribute("bspageInfo", bspageInfo);
				 model.addAttribute("buysellList", buysellList);
				
				 buysharingList = mypageService.getBuySharingList(userno,ppage,ppageInfo);
				 for (int i = 0; i < buysharingList.size(); i++) {
						/* 주소 띄우기*/
						String addr = buysharingList.get(i).getSaddress();
						String[] addChange = addr.split(" "); //주소 공백으로 분
						if(addChange[2].matches("^.*.구$")) { //세 번째 단어에서 '구'로 끝나면 동까지 입력 
							String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
							buysharingList.get(i).setSaddress(join1);
						} else {
							String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
							buysharingList.get(i).setSaddress(join2);
						}
						if (buysharingList.get(i).getSfileids() != null) {
							buysharingList.get(i).setSfileids(buysharingList.get(i).getSfileids().split(",")[0]);
						}
					}
				 model.addAttribute("ppageInfo", ppageInfo);
				 model.addAttribute("buysharingList", buysharingList);
			
				} else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);
	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       
	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
				}else {
					//users 값을 가져온다
		             Users users = mypageService.getMypage(userno);
		             model.addAttribute("users",users);
		             
		           //상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);
	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       
	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
				}
			}else {
				//users 값을 가져온다
	             Users users = mypageService.getMypage(userno);
	             model.addAttribute("users",users);
	             
	           //상품등록
                Integer sharingcount = sharingService.sharingcount(userno);
                Integer sellcount = sellService.sellcount(userno);
                Integer totalcount = sharingcount + sellcount;
                model.addAttribute("totalcount",totalcount);
                //거래후기
                Integer reviewcount = reviewService.reviewcount(userno);
                model.addAttribute("reviewcount",reviewcount);       
                //거래완료
                Integer statuscount = sharingService.statuscount(userno);
                statuscount +=sellService.statuscount(userno);
                model.addAttribute("statuscount",statuscount);
			}			 
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "/mypage/buyProduct";
	}
//개인페이지 신청목록
	@GetMapping ("mypage/umypage/{userno}/apply")
	public String umypageapply(@PathVariable("userno") Integer userno, Model model,
			@RequestParam(value = "apage", required = false, defaultValue = "1") Integer apage) {
		PageInfo apageInfo = new PageInfo();
		
		List<Apply> wapply = null;
		 
        try {
			if(session.getAttribute("authUser")!=null) {
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					wapply = applyService.getApplyList(userno, apage, apageInfo);
					
					//개인이 신청한 목록 리스트
					//wapply = applyService.getUapply(userno);
					model.addAttribute("wapply",wapply);
					
					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);

	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       

	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
	                model.addAttribute("apageInfo", apageInfo);
				}else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")) {
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					wapply = applyService.getApplyList(userno, apage, apageInfo);
					
					//개인이 신청한 목록 리스트
					//wapply = applyService.getUapply(userno);
					model.addAttribute("wapply",wapply);
					
					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);

	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       

	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
	                model.addAttribute("apageInfo", apageInfo);
				}else {
					//users 값을 가져온다
		            Users users = mypageService.getMypage(userno);
		            model.addAttribute("users",users);
		            
					wapply = applyService.getApplyList(userno, apage, apageInfo);
					
					//개인이 신청한 목록 리스트
					//wapply = applyService.getUapply(userno);
					model.addAttribute("wapply",wapply);
						
					
					//상품등록
	                Integer sharingcount = sharingService.sharingcount(userno);
	                Integer sellcount = sellService.sellcount(userno);
	                Integer totalcount = sharingcount + sellcount;
	                model.addAttribute("totalcount",totalcount);

	                //거래후기
	                Integer reviewcount = reviewService.reviewcount(userno);
	                model.addAttribute("reviewcount",reviewcount);       

	                //거래완료
	                Integer statuscount = sharingService.statuscount(userno);
	                statuscount +=sellService.statuscount(userno);
	                model.addAttribute("statuscount",statuscount);
	                model.addAttribute("apageInfo", apageInfo);
				}
			}else {
				//users 값을 가져온다
	            Users users = mypageService.getMypage(userno);
	            model.addAttribute("users",users);
	            
				wapply = applyService.getApplyList(userno, apage, apageInfo);
				
				//개인이 신청한 목록 리스트
				//wapply = applyService.getUapply(userno);
				model.addAttribute("wapply",wapply);
					
				
				//상품등록
                Integer sharingcount = sharingService.sharingcount(userno);
                Integer sellcount = sellService.sellcount(userno);
                Integer totalcount = sharingcount + sellcount;
                model.addAttribute("totalcount",totalcount);

                //거래후기
                Integer reviewcount = reviewService.reviewcount(userno);
                model.addAttribute("reviewcount",reviewcount);       

                //거래완료
                Integer statuscount = sharingService.statuscount(userno);
                statuscount +=sellService.statuscount(userno);
                model.addAttribute("statuscount",statuscount);
                model.addAttribute("apageInfo", apageInfo);
			}
			 Users users = mypageService.getMypage(userno);
			 model.addAttribute("users",users);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/mypage/uapplylist";
	}	
	
	 //개인 쪽지함
	 @GetMapping ("/mypage/umessage/{userno}")
	   String myuMessage(@RequestParam(value = "rpage", required = false, defaultValue = "1") Integer rpage,
	         @RequestParam(value = "spage", required = false, defaultValue = "1") Integer spage, Model model,
	         @RequestParam(value = "select", required = false, defaultValue = "0") Integer select,
	         @RequestParam(value = "submitcheck", required = false, defaultValue = "") String submitcheck,
	         @PathVariable("userno") Integer userno) { 
	      List<MessageVO> rmessageList = new ArrayList<>();
	      List<MessageVO> smessageList = new ArrayList<>();
	      Map<String, Object> map = new HashMap<String, Object>();
	      PageInfo rpageInfo = new PageInfo();
	      PageInfo spageInfo = new PageInfo();      
	      try {
	            String sect;
	            Users uauthuser = new Users();
	            if(session.getAttribute("authUser")!=null) {
		            if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
		               uauthuser = (Users) session.getAttribute("authUser");
		               map.put("recvUserno", uauthuser.getUserno());
		               map.put("page", rpage);
		               map.put("pageInfo", rpageInfo);
		               rmessageList = messageService.uRecvMessage(map);//사용자의 받은편지함
		               smessageList = messageService.uSendMessage(uauthuser.getUserno(),spage,spageInfo);//사용자의 보낸편지함
		               //
		               Users users = mypageService.getMypage(userno);
		               //상품등록
		               Integer sharingcount = sharingService.sharingcount(userno);
		               Integer sellcount = sellService.sellcount(userno);
		               Integer totalcount = sharingcount + sellcount;
		               model.addAttribute("totalcount",totalcount);
	
		               //거래후기
		               Integer reviewcount = reviewService.reviewcount(userno);
		               model.addAttribute("reviewcount",reviewcount);       
	
		               //거래완료
		               Integer statuscount = sharingService.statuscount(userno);
		               statuscount +=sellService.statuscount(userno);
		               model.addAttribute("statuscount",statuscount);
	
		               model.addAttribute("recvmessage", rmessageList);
		               model.addAttribute("sendmessage", smessageList);
		               model.addAttribute("rpageInfo", rpageInfo);
		               model.addAttribute("spageInfo", spageInfo);
		               model.addAttribute("select", select);
		               model.addAttribute("submitcheck", submitcheck);
		               model.addAttribute("users", users);
		               model.addAttribute("userno", userno);
		            } else {
	
		            }
	            }
	      }catch(Exception e) {
	         e.printStackTrace();
	      }
	      return "/mypage/message";
	   }
	 
	 //사업자 쪽지함
	 @GetMapping ("/mypage/bmessage/{bno}")
	   String mybMessage(@RequestParam(value = "rpage", required = false, defaultValue = "1") Integer rpage,
	         @RequestParam(value = "spage", required = false, defaultValue = "1") Integer spage, Model model,
	         @RequestParam(value = "select", required = false, defaultValue = "0") Integer select,
	         @RequestParam(value = "submitcheck", required = false, defaultValue = "") String submitcheck,
	         @PathVariable("bno") Integer bno) {
		 	List<MessageVO> rmessageList = new ArrayList<>();
			List<MessageVO> smessageList = new ArrayList<>();
			Map<String, Object> map = new HashMap<String, Object>();
			PageInfo rpageInfo = new PageInfo();
			PageInfo spageInfo = new PageInfo();   
	      try {
	    	  	Business bauthuser = new Business();
	          	String sect;
				if(session.getAttribute("authUser")!=null) {
		               bauthuser = (Business) session.getAttribute("authUser");
		               map.put("recvUserno", bauthuser.getBno());
		               map.put("page", rpage);
		               map.put("pageInfo", rpageInfo);
		               rmessageList = messageService.bRecvMessage(map);
		               smessageList = messageService.bSendMessage(bauthuser.getBno(),spage,spageInfo);
		               model.addAttribute("recvmessage", rmessageList);
		               model.addAttribute("sendmessage", smessageList);
		               model.addAttribute("rpageInfo", rpageInfo);
		               model.addAttribute("spageInfo", spageInfo);
		               model.addAttribute("select", select);
		               model.addAttribute("submitcheck", submitcheck);
		               
		               Business business = messageService.mypageBusiness(bno);
		                  model.addAttribute("business",business);

		                  //신청목록개수
		                  Integer applycount = applyService.applycount(bno);
		                  model.addAttribute("applycount",applycount);

		                  //수거상태
		                  Map astatuscount= applyService.astatuscount(bno);
		                  Integer dcount= (Integer) astatuscount.get("신청중");
		                  Integer ecount= (Integer) astatuscount.get("신청완료");
		                  Integer bcount= (Integer) astatuscount.get("신청거절");
		                  Integer acount= (Integer) astatuscount.get("후기등록완료");
		                  Integer ccount= (Integer) astatuscount.get("수거완료");
		                  model.addAttribute("dcount",dcount);
		                  model.addAttribute("ecount",ecount);
		                  model.addAttribute("bcount",bcount);
		                  model.addAttribute("acount",acount);
		                  model.addAttribute("ccount",ccount);
				}
	      }catch(Exception e) {
	    	  e.printStackTrace();
	      }
	      return "/mypage/message";
	   }	
	 
	//쪽지 보내기
	@PostMapping("/mypage/smessage")
    public ModelAndView submitMessage(@ModelAttribute MessageVO message, Model model, RedirectAttributes r) {
      ModelAndView mav = new ModelAndView();
      try {
         String sect = "";
         if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
            Users users = (Users) session.getAttribute("authUser");
            message.setSendUserno(users.getUserno());
            sect = users.getSect();
         } else {
            Business bauthuser = (Business) session.getAttribute("authUser");
            message.setSendBno(bauthuser.getBno());
            sect = bauthuser.getSect();
         }
         String submitcheck = messageService.submitMessage(message, sect);
         if(submitcheck.equals("true")) {
            r.addAttribute("submitcheck", "true"); //쪽지가 정상적으로 보내졌으면 전송완료
         }else {
            r.addAttribute("submitcheck", "false");
         }
         if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
        	 Users users = (Users) session.getAttribute("authUser");
        	 Integer userno = users.getUserno();
        	 
        	 mav.setViewName("redirect:/mypage/umessage/"+userno);
         } else {
             Business bauthuser = (Business) session.getAttribute("authUser");
        	 Integer bno = bauthuser.getBno();
             mav.setViewName("redirect:/mypage/bmessage/"+bno);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
      return mav;
      
   }

	
	@GetMapping ("/mypage/message")
	String myMessage(@RequestParam(value = "rpage", required = false, defaultValue = "1") Integer rpage,
			@RequestParam(value = "spage", required = false, defaultValue = "1") Integer spage, Model model,		
			@RequestParam(value = "select", required = false, defaultValue = "0") Integer select,
			@RequestParam(value = "submitcheck", required = false, defaultValue = "") String submitcheck) {
		List<MessageVO> rmessageList = new ArrayList<>();
		List<MessageVO> smessageList = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		PageInfo rpageInfo = new PageInfo();
		PageInfo spageInfo = new PageInfo();
		try {
	         Business bauthuser = new Business();
	         String sect;
	         Users uauthuser = new Users();
	         if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
	            uauthuser = (Users) session.getAttribute("authUser");
	            map.put("recvUserno", uauthuser.getUserno());	            
	            map.put("page", rpage);
	            map.put("pageInfo", rpageInfo);	            
	            rmessageList = messageService.uRecvMessage(map);//사용자의 받은편지함
	            smessageList = messageService.uSendMessage(uauthuser.getUserno(),spage,spageInfo);//사용자의 보낸편지함
	            model.addAttribute("recvmessage", rmessageList);
	            model.addAttribute("sendmessage", smessageList);
	            model.addAttribute("rpageInfo", rpageInfo);
	            model.addAttribute("spageInfo", spageInfo);
	            model.addAttribute("select", select);
	            model.addAttribute("submitcheck", submitcheck);
	         } else {
	            bauthuser = (Business) session.getAttribute("authUser");
	            map.put("recvUserno", bauthuser.getBno());
	            map.put("page", rpage);
	            map.put("pageInfo", rpageInfo);
	            rmessageList = messageService.bRecvMessage(map);
	            smessageList = messageService.bSendMessage(bauthuser.getBno(),spage,spageInfo);
	            model.addAttribute("recvmessage", rmessageList);
	            model.addAttribute("sendmessage", smessageList);	            
	            model.addAttribute("rpageInfo", rpageInfo);
	            model.addAttribute("spageInfo", spageInfo);
	            model.addAttribute("select", select);
	            model.addAttribute("submitcheck", submitcheck);
	         }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/mypage/message";
	}
   
	//받은 쪽지함 쪽지 내용 확인
   @ResponseBody
   @GetMapping("/viewRecvMessage")
   public MessageVO viewRecvMessage(@RequestParam("mno") Integer mno) { 
      MessageVO message = new MessageVO();
      try {
           Business bauthuser = new Business();
            Users uauthuser = new Users();
            if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
               uauthuser = (Users) session.getAttribute("authUser");
               message = messageService.uRecvViewMessage(mno);
            } else {
               bauthuser = (Business) session.getAttribute("authUser");
               message = messageService.bRecvViewMessage(mno);
            }         
      }catch(Exception e) {
         e.printStackTrace();
      }
      return message;
   }
   
   //보낸 쪽지함 쪽지 내용 확인
   @ResponseBody
   @GetMapping("/viewSendMessage")
   public MessageVO viewSendMessage(@RequestParam("mno") Integer mno) { 
      MessageVO message = new MessageVO();
      try {
           Business bauthuser = new Business();
            Users uauthuser = new Users();
            if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
               uauthuser = (Users) session.getAttribute("authUser");
               message = messageService.uSendViewMessage(mno);
            } else {
               bauthuser = (Business) session.getAttribute("authUser");
               message = messageService.bSendViewMessage(mno);
            }         
      }catch(Exception e) {
         e.printStackTrace();
      }
      return message;
   }
   
   //받은 쪽지함에서 쪽지 삭제   
   @ResponseBody
   @GetMapping("/recvDelete")
   public void recvDelete(@RequestParam("mno") Integer mno) {
      try {
         messageService.deleteMessage(mno);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   //보낸 쪽지함에서 쪽지 삭제
   @ResponseBody
   @GetMapping("/sendDelete")
   public void sendDelete(@RequestParam("mno") Integer mno) { //보낸 쪽지함에서 쪽지 삭제 
      try {
         messageService.deleteMessage(mno);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   //마이페이지 자기소개란 수정
   @ResponseBody
   @PostMapping("/mypage")
   public String introduce(@RequestParam("introduce") String introduce) {
      String introtext="";
      try { 
            Business bauthuser = new Business();
               Users uauthuser = new Users();
               if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
                  uauthuser = (Users) session.getAttribute("authUser");
                  introtext=mypageService.uintroduce(introduce,uauthuser.getUserno());
               } else {
                  bauthuser = (Business) session.getAttribute("authUser");
                  introtext=mypageService.bintroduce(introduce,bauthuser.getBno());
               }         
         System.out.println("introtext"+introtext);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return introtext;
   }
   
   @ResponseBody
   @GetMapping("/viewsellapply")
   public List<Users> viewsellapply(@RequestParam("ino") Integer ino) {
      List<Users> sellapplylist = null;
      try {
         sellapplylist = mypageService.getSellapplylist(ino);
      }catch(Exception e) {
         e.printStackTrace();
      }
      return sellapplylist;
   }
   
   @ResponseBody
   @GetMapping("/viewsharingapply")
   public List<Users> viewsharingapply(@RequestParam("sno") Integer sno) {
      List<Users> sharingapplylist = null;
      try {
         sharingapplylist = mypageService.getSharingapplylist(sno);
      }catch(Exception e) {
         e.printStackTrace();
      }
      return sharingapplylist;
   }
   
   @GetMapping("/selectSellApply")
   public String selectSellApply(@RequestParam("radio-group") Integer list, @RequestParam("ino") Integer ino) {
      try {
         mypageService.selectSellApply(list,ino);
         sellService.alterStatus(list, ino);
      }catch(Exception e) {
         e.printStackTrace();
      }
      return "redirect:/sellapplyList/"+ino;
   }
   
   @GetMapping("/selectSharingApply")
   public String selectSharingApply(@RequestParam("radio-group") Integer list, @RequestParam("sno") Integer sno) {
      try {
         mypageService.selectSharingApply(list,sno);
         sharingService.alterStatus(list,sno);
      }catch(Exception e) {
         e.printStackTrace();
      }
      return "redirect:/sharingapplyList/"+sno;
   }
   
   @ResponseBody
   @GetMapping("/cancelDeal")
   public void cancelDeal(@RequestParam("ino") Integer ino) {
      try {
         mypageService.cancelDeal(ino);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/cancelSharingDeal")
   public void cancelSharingDeal(@RequestParam("sno") Integer sno) {
      try {
         mypageService.cancelSharingDeal(sno);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/completeSellDeal")
   public void completeDeal(@RequestParam("ino") Integer ino) {
      try {
         mypageService.completeDeal(ino);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/completeSeDeal")
   public void completeSeDeal(@RequestParam("ino") Integer ino) {
      try {
         mypageService.completeSeDeal(ino);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/completeSharingDeal")
   public void completeSharingDeal(@RequestParam("sno") Integer sno) {
      try {
         mypageService.completeSharingDeal(sno);
      }catch(Exception e) {
         e.printStackTrace();
      }
   } 
   
   @ResponseBody
   @GetMapping("/completeShDeal")
   public void completeShDeal(@RequestParam("sno") Integer sno) {
      try {
         mypageService.completeShDeal(sno);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/sendIReview")
   public void sendIReview(@RequestParam("star") Integer star, @RequestParam("content") String content,
		   @RequestParam("ino") Integer ino) {
      try {
    	  Users users = (Users) session.getAttribute("authUser");
    	  Integer userno = users.getUserno();
    	  Map<String, Object> map = new HashMap<String, Object>();
    	  map.put("star", star);
    	  map.put("content", content);
    	  map.put("ino", ino);
    	  map.put("userno", userno);
         mypageService.sendIReview(map);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/sendSReview")
   public void sendSReview(@RequestParam("star") Integer star, @RequestParam("content") String content,
		   @RequestParam("sno") Integer sno) {
      try {
    	  Users users = (Users) session.getAttribute("authUser");
    	  Integer userno = users.getUserno();
    	  Map<String, Object> map = new HashMap<String, Object>();
    	  map.put("star", star);
    	  map.put("content", content);
    	  map.put("sno", sno);
    	  map.put("userno", userno);
         mypageService.sendSReview(map);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/sendUapplyReview")
   public void sendUapplyReview(@RequestParam("star") Integer star, @RequestParam("content") String content,
		   @RequestParam("bno") Integer bno, @RequestParam("ano") Integer ano) {
      try {
    	  Users users = (Users) session.getAttribute("authUser");
    	  Integer userno = users.getUserno();
    	  Map<String, Object> map = new HashMap<String, Object>();
    	  map.put("star", star);
    	  map.put("content", content);
    	  map.put("bno", bno);
    	  map.put("userno", userno);
    	  map.put("ano", ano);
         mypageService.sendUapplyReview(map);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @ResponseBody
   @GetMapping("/uapplyReviewStatus")
   public void uapplyReviewStatus(@RequestParam("ano") Integer ano) {
      try {
         mypageService.uapplyReviewStatus(ano);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   	
	@GetMapping("/mypage/likelist/{userno}")
	public String likelist(@PathVariable("userno") Integer userno, Model model) {
		try {
			Users users = mypageService.getMypage(userno);
			model.addAttribute("users", users);
			 //상품등록
            Integer sharingcount = sharingService.sharingcount(userno);
            Integer sellcount = sellService.sellcount(userno);
            Integer totalcount = sharingcount + sellcount;
            model.addAttribute("totalcount",totalcount);
            
            //거래후기
            Integer reviewcount = reviewService.reviewcount(userno);
            model.addAttribute("reviewcount",reviewcount);       
           
            //거래완료
            Integer statuscount = sharingService.statuscount(userno);
            statuscount +=sellService.statuscount(userno);
            model.addAttribute("statuscount",statuscount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/mypage/likelist";
	}
	@ResponseBody
	@PostMapping("/mypage/likelist/likelistaj")
	public List<?> likelistaj (@RequestBody Map<String,Object> params){
		String category = (String) params.get("category");
		List<Sharing> sList = new ArrayList<Sharing>();
		List<Sell> iList = new ArrayList<Sell>();
		List<Business> bList = new ArrayList<Business>();
		Users users = (Users)session.getAttribute("authUser");
		Integer userno = users.getUserno();
		try {
			if("free".equals(category)) {
				sList = mypageService.getLikeSharingList(userno);
				for (int i = 0; i < sList.size(); i++) {
					if (sList.get(i).getSfileids() != null) {
						sList.get(i).setSfileids(sList.get(i).getSfileids().split(",")[0]);
					}
				}
				return sList;
			}else if("indi".equals(category)) {
				iList = mypageService.getLikeSellList(userno);
				for (int i = 0; i < iList.size(); i++) {
					if (iList.get(i).getIfileids() != null) {
						iList.get(i).setIfileids(iList.get(i).getIfileids().split(",")[0]);
					}
				}
				return iList;
			}else {
				bList = mypageService.getLikeBusinessList(userno);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return bList;
	}

	//수거 승인
		@GetMapping("/bapply/{ano}/{userno}")
		@ResponseBody
		public String bapply(@PathVariable("ano") Integer ano, 
								   @PathVariable("userno") Integer userno,
								   @RequestParam("astatus") String astatus) {
			try {			
				if(astatus.equals("신청중")) {
					astatus = "신청완료";	
					
				}else if(astatus.equals("신청완료")) {
					astatus = "수거완료";
					
				}
				applyService.updateApply(ano,userno,astatus);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return astatus;
		}

		
		//수거 거절
		@GetMapping("/bapplydelete/{ano}/{userno}")
		@ResponseBody
		public String bapplydel(@PathVariable("ano") Integer ano, 
								   @PathVariable("userno") Integer userno,
								   @RequestParam("astatus") String astatus) {
			try {			
				if(astatus.equals("신청중")) {
					astatus = "신청거절";
				}
				applyService.deleteApply(ano,userno,astatus);
							
			}catch(Exception e) {
				e.printStackTrace();
			}
			return "신청 거절 하였습니다.";
		}
		
			//신청 취소
			@GetMapping("/wapplydelete/{ano}/{userno}")
			@ResponseBody
			public String wapplydel(@PathVariable("ano") Integer ano,
									   @PathVariable("userno") Integer userno) {
				try {							
					applyService.deletewApply(ano,userno);
								
				}catch(Exception e) {
					e.printStackTrace();
				}
				return "신청 취소 하였습니다.";
			}

	@ResponseBody
	@PostMapping("/mypage/likelist/deletelike")
	public boolean deletelikle(@RequestBody Map<String,Object> params) {
		Integer no = Integer.parseInt((String)params.get("deleteno"));
		String category = (String) params.get("category");
		Users users = (Users)session.getAttribute("authUser");
		Integer userno = users.getUserno();
		try {
			if("free".equals(category)) {
				Integer sno = no;
				mypageService.deletesLike(userno, sno);
				return true;
			}else if("indi".equals(category)) {
				Integer ino = no;
				mypageService.deleteiLike(userno, ino);
				return true;
			}else if("busi".equals(category)) {
				Integer bno = no;
				mypageService.deletebLike(userno, bno);
				return true;
			}
			
		}catch(Exception e) {
			
		}
		return false;
	}
	
	//신청 작성하기 
		@PostMapping("/mypage/likelist/apply")
		public String apply(@ModelAttribute Apply apply, RedirectAttributes re) {
				Users users = (Users) session.getAttribute("authUser");
				Integer userno = users.getUserno();
			try {
				apply.setUserno(users.getUserno());
				if(apply.getAstatus() == null) {
					apply.setAstatus("신청중");
				}
				
				businessService.registapply(apply);
				re.addFlashAttribute("msg", "수거 신청이 완료되었습니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
			return "redirect:/mypage/likelist/"+userno;
		}
	

}

