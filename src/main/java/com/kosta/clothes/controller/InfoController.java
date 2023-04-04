package com.kosta.clothes.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kosta.clothes.bean.Apply;
import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.Donation;
import com.kosta.clothes.bean.Likes;
import com.kosta.clothes.bean.Trash;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.service.BusinessService;
import com.kosta.clothes.service.DonationService;
import com.kosta.clothes.service.LikesService;
import com.kosta.clothes.service.TrashService;

//헌옷수거함+기부업체 정보 안내 컨트롤러
@Controller
public class InfoController {
	@Autowired
	TrashService trashService;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	DonationService donationService;

	@Autowired
	BusinessService businessService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	LikesService likesService;

	// 정보 안내(기부업체)
	// 판매업체
		@ResponseBody
		@PostMapping("/donationlist")
		public List<Donation> donationlist(@RequestBody Map<String,Object> params,Model model) {
			List<Donation> Donation = null;	
			
			//ajax로 데이터 받아온 것
			String sido = (String) params.get("sido");		
			String sigungu = (String) params.get("sigugun");
						
			try {				
				Donation = donationService.allDonationInfo1(sido,sigungu);				
			}catch(Exception e) {
				e.printStackTrace();
			}
			return Donation;
		}
	//기부업체 이동
	@GetMapping("/information")
	ModelAndView alladdress(Model model) {
		ModelAndView mav = new ModelAndView();
		try {
			//온라인 메인 로고에 사용
			List<Donation> dona = donationService.allDonationInfo();
			//List<Donation> donation = donationService.DonationInfo(store,good,story);
			mav.addObject("donation",dona);
			mav.setViewName("/info/donationmap");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	//헌옷수거함 페이지 오픈

	@GetMapping("/clothingbin")
	public String clothingBin() {
		return "info/clothingbin";
	}
	//csv 파일을 bean과 매칭
	@ResponseBody
	@PostMapping("/csvToBean")
	public List<String> csvToBean(@RequestBody Map<String,Object> params , Model model) {
		//ajax로 데이터 받아온 것
		String sido = (String) params.get("sido");
		String sigungu = (String) params.get("sigugun");
		String myeondong = (String) params.get("dong");
		Trash trash = null;
		List<String> list = new ArrayList<String>();
		//csv 파일 경로
		String path = "/home/ubuntu/app/oldclothes/csv/";
//		String path = servletContext.getRealPath("/csv/");
		path += "oldclothesutf.csv";
		List<Trash> data = trashService.readCsvToBean(path); //읽어온 것을 data에 담는다
		
		Iterator<Trash> it = data.iterator(); //Iterator생성
		while(it.hasNext()) {
			trash = (Trash)it.next();
			//받아온 selectbox 데이터를 조건으로 그와 일치하는 리스트 검색
			if(trash.getSido().contains(sido)&&trash.getSigungu().contains(sigungu)&&trash.getMyeondong().contains(myeondong)) {
			
			list.add(trash.getOldcloaddress());
			}

		
		}
		model.addAttribute("trash", list);
		
		return list;
	}
	

	//판매 업체 jsp 넘겨주는것	
	@GetMapping("/businessinfo")
	public String businessinfo(Model model) {
		try {
	         Business bauthuser=new Business();
	         String sect;
	         Integer userno =0;
	         Users uauthuser=new Users();	  
				//사용자가 로그인 했을 때 
	         if(session.getAttribute("authUser")!=null) {
		         if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
		            uauthuser = (Users) session.getAttribute("authUser");
		            sect = uauthuser.getSect(); 
		            userno = uauthuser.getUserno();
		         } else{//그 외라면~
		        	 bauthuser = (Business) session.getAttribute("authUser");
			         sect = bauthuser.getSect();
			            //userno = bauthuser.getBno();
		         }
	         }else {
	        	 model.addAttribute("logincheck","false");
	         }
			 //Business business = (Business) session.getAttribute("bauthUser");			 
			if(userno!=null || userno !=0) { //개인이 로그인 했을떄 	likescheck , bno 가져오기 like테이블에서									
				List<Likes> list =  likesService.getbno(userno);
				model.addAttribute("list",list);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "info/businessinfo";
	}
	
	// 판매업체
	@ResponseBody
	@PostMapping("/businesslist")
	public List<Business> businesslist(@RequestBody Map<String,Object> params,Model model) {
		List<Business> business = null;	
		List<Long> likescheck1= null;
		//ajax로 데이터 받아온 것
		String sido = (String) params.get("sido");		
		String sigungu = (String) params.get("sigugun");
		Integer userno =0;
        Users uauthuser=new Users();			
		try {
			//사용자가 로그인 했을 때 
			if(session.getAttribute("authUser")!=null) {
				//사용자가 로그인했을 때 
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){				
		            uauthuser = (Users) session.getAttribute("authUser");	            
		            userno = uauthuser.getUserno();	            		            
		            business = businessService.allBusinessInfo(sido,sigungu);		            
//		            System.out.println("kakaoID" + business.get(0).getBkakaoid());
		            
		            for(int i=0; i<business.size();i++) {
		            	business.get(i).setLikescheck((businessService.likecheck(business.get(i).getBno(),userno)));
		            }
		            
				}else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")){
					//업체가 로그인 했을 때 
					business = businessService.allBusinessInfo(sido,sigungu);
				}
			}else {// 그외
				business = businessService.allBusinessInfo(sido,sigungu);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return business;
	}

	//판매업체 좋아요


		@ResponseBody
		@PostMapping("/businessinfo/likes")
		public Long registLikes(@RequestParam("bno") Integer bno) {
			Likes likes = new Likes();
			Long likescheck = null;
			Users uauthuser=new Users();
			Integer userno=0;
			try {
				//개인이 로그인 했을 때 
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
					uauthuser = (Users) session.getAttribute("authUser");
					userno = uauthuser.getUserno();
				}
				likes.setBno(bno);
				likes.setUserno(userno);
				
				likescheck = likesService.getBlikescheck(likes); //likescheck를 가져와(지금 무슨 상태죠?)
				if(likescheck == null) { //처음 눌렀을 때
					likesService.registBlikes(likes);
					likesService.upBusinessLikes(likes);
				}else{
					likes.setLikescheck(likesService.getBlikescheck(likes));
					likesService.updateBlikes(likes);				
				}
				likescheck = likesService.getBlikescheck(likes);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return likescheck;
		}
		
	
	
	//카테코리 검색 결과 
	@ResponseBody
	@PostMapping("/categoryb")
	public List<Business> categoryb(@RequestBody Map<String,Object> params,Model model) {
		List<Business> business = null;		
		//ajax로 데이터 받아온 것
		String sido = (String) params.get("sido");		
		String sigungu = (String) params.get("sigugun");
		String category = (String) params.get("category");
		Integer userno =0;
        Users uauthuser=new Users();
		try {
			//사용자가 로그인 했을 때 
			if(session.getAttribute("authUser")!=null) {
				//사용자가 로그인 했을 때
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
		            uauthuser = (Users) session.getAttribute("authUser");	            
		            userno = uauthuser.getUserno();	            
		            business = businessService.cateusers(sido, sigungu, category);
		            for(int i=0; i<business.size();i++) {
		            	business.get(i).setLikescheck((businessService.likecheck(business.get(i).getBno(),userno)));
		            }
				}else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")){
					//업체가 로그인 했을 때 
					business = businessService.cateusers(sido,sigungu,category);
				}
			}else {// 그외
				business = businessService.cateusers(sido,sigungu,category);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return business;
	}
	
	//신청 작성하기 
	@PostMapping("/apply")
	public String apply(@ModelAttribute Apply apply, RedirectAttributes re) {
		
		try {
			Users userno = (Users) session.getAttribute("authUser");
			apply.setUserno(userno.getUserno());
			if(apply.getAstatus() == null) {
				apply.setAstatus("신청중");
			}
			
			businessService.registapply(apply);
			re.addFlashAttribute("msg", "수거 신청이 완료되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/businessinfo";
	}		
}
