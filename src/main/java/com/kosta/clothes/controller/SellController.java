package com.kosta.clothes.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.Comments;
import com.kosta.clothes.bean.FileVO;
import com.kosta.clothes.bean.Likes;
import com.kosta.clothes.bean.MessageVO;
import com.kosta.clothes.bean.Sell;
import com.kosta.clothes.bean.Sharing;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.bean.Wapply;
import com.kosta.clothes.service.ApplyService;
import com.kosta.clothes.service.ChatRoomService;
import com.kosta.clothes.service.CommentService;
import com.kosta.clothes.service.LikesService;
import com.kosta.clothes.service.MessageService;
import com.kosta.clothes.service.MypageService;
import com.kosta.clothes.service.ReviewService;
import com.kosta.clothes.service.SellService;

@Controller
public class SellController {
	@Autowired
	SellService sellService;
	
	@Autowired 
	LikesService likesService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	ApplyService applyService;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	MypageService mypageService;
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	ChatRoomService chatRoomService;
	@Autowired
	HttpSession session;
	
	@GetMapping("/sellList")
	public ModelAndView main(HttpServletRequest request, @RequestParam(value = "kwd", required = false) String kwd) {
		ModelAndView mav = new ModelAndView();
		List<Sell> sellList;
		try {
			if (kwd != null && kwd != "") {
				sellList = sellService.getSellList(kwd);
			} else {
				sellList = sellService.getSellList();
			}
			for (int i = 0; i < sellList.size(); i++) {
				if (sellList.get(i).getIfileids() != null) {
					sellList.get(i).setIfileids(sellList.get(i).getIfileids().split(",")[0]);
				}
			}
			mav.addObject("sellList", sellList);
			mav.addObject("kwd", kwd);
			mav.setViewName("/sell/sellList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@GetMapping("/sellRegistForm")
	public String sharingRegistForm() {
		return "/sell/sellRegistForm";
	}

	@ResponseBody
	@PostMapping("/sellRegist")
	public ModelAndView registSell(@ModelAttribute Sell sell,
			@RequestParam("iimageFile") MultipartFile[] files) {
		ModelAndView mav = new ModelAndView();
		try {
			Users users = (Users) session.getAttribute("authUser");
			if (users != null) {
				sell.setUserno(users.getUserno());
				sellService.registSell(sell, files);
			}
			mav.setViewName("redirect:/sellList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@GetMapping("/sellView/{ino}")
	public ModelAndView viewSell(@PathVariable("ino") Integer ino, Model model,
			@RequestParam(value = "submitcheck", required = false) String submitcheck) {
		ModelAndView mav = new ModelAndView();
		try {
			Sell sell = sellService.viewSell(ino);
			Integer reviewcount = reviewService.reviewcount(sell.getUserno());
			model.addAttribute("reviewcount", reviewcount);
			if(sell.getIfileids() != null) {
				String[] fidArr = sell.getIfileids().split(",");
				mav.addObject("files", fidArr);
			}
			Business bauthuser = new Business();
			String sect;
			Users uauthuser = new Users();
			if(session.getAttribute("authUser") != null) {
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
					uauthuser = (Users) session.getAttribute("authUser");
					sect = uauthuser.getSect();					
					List<Users> users = mypageService.getSellapplylist(ino);
					mav.addObject("users",users);					
					model.addAttribute("sect", sect);					
					mav.setViewName("/sell/sellView");
				} else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")){
					bauthuser = (Business) session.getAttribute("authUser");
					sect = bauthuser.getSect();
					Users uservo = new Users();
					uservo = sellService.getInickname(ino);									
					model.addAttribute("sect", sect);					
					model.addAttribute("uservo", uservo);
					model.addAttribute("submitcheck", submitcheck);
					mav.addObject("sell", sell);					
					mav.setViewName("/sell/sellView");					
				}else {
					Users uservo = new Users();
					uservo = sellService.getInickname(ino);
					model.addAttribute("uservo", uservo);
					model.addAttribute("submitcheck", submitcheck);
					mav.addObject("sell", sell);
					mav.setViewName("/sell/sellView");
				}
				
			}else {
				Users uservo = new Users();
				uservo = sellService.getInickname(ino);				
				model.addAttribute("uservo", uservo);
				model.addAttribute("submitcheck", submitcheck);
				mav.addObject("sell", sell);
				mav.setViewName("/sell/sellView");				
			}
			//개인
			if(uauthuser.getUserno() == null && bauthuser.getBno() == null) {
				model.addAttribute("logincheck", "false");
			} else if(uauthuser.getUserno() != null) {
				Likes likevo = new Likes();
				likevo.setIno(ino);
				likevo.setUserno(uauthuser.getUserno());
				Long likeselect = likesService.getIlikescheck(likevo);
				if(likeselect != null) {
					mav.addObject("likes", likeselect);
				}
			} else {
				
			}
//			Users users = (Users) session.getAttribute("authUser");
//			if(users == null) {
//				model.addAttribute("logincheck", "false");
//			}else {
//				Likes likevo = new Likes();
//				likevo.setIno(ino);
//				likevo.setUserno(users.getUserno());
//				Long likeselect = likesService.getIlikescheck(likevo);
//				if(likeselect != null) {
//					mav.addObject("likes", likeselect);
//				}
//			}
			Users uservo = new Users();
			uservo = sellService.getInickname(ino);
			model.addAttribute("uservo", uservo);
			model.addAttribute("submitcheck", submitcheck);
			mav.addObject("sell", sell);
			mav.setViewName("/sell/sellView");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}	
	
	//신청확인
	@GetMapping("/sellapplyList/{ino}")
	public ModelAndView sharingapplyList(@PathVariable("ino") Integer ino, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("/sell/sellapplyList");
			List<Users> users = mypageService.getSellapplylist(ino);
			Sell sell = sellService.viewSell(ino);
			mav.addObject("users",users);
			mav.addObject("sell",sell);
			mav.addObject("ino",ino);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	//무료나눔 댓글등록
	//댓글 등록하기
	@PostMapping("/sellView/{ino}/{userno}")
	public ModelAndView comments(@PathVariable("ino") Integer ino,
			@PathVariable("userno") Integer userno,
			@ModelAttribute Comments comments) {
		ModelAndView mav = new ModelAndView();
		try {
			Users users = (Users)session.getAttribute("authUser");
			comments.setIno(ino);
			comments.setUserno(userno);
			comments.setCname(users.getNickname());
			commentService.registCommentsell(comments);
			mav.setViewName("redirect:/sellView/"+ino);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
		
	}
	
	@GetMapping("/sellModifyForm")
	public ModelAndView modifySharing(@RequestParam("ino") Integer ino) {
		ModelAndView mav = new ModelAndView();
		try {
			Sell sell = sellService.viewSell(ino);
			mav.addObject("sell", sell);
			mav.setViewName("/sell/sellModifyForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@PostMapping("/sellModify")
	public ModelAndView modifySharing(@ModelAttribute Sell sell, @ModelAttribute FileVO fileVo,
			@RequestParam("iimageFile") MultipartFile[] files) {
		ModelAndView mav = new ModelAndView();
		try {
			sellService.modifySell(sell);
			sellService.modifyIfileids(sell, fileVo, files);
			mav.setViewName("redirect:/sellView/" + sell.getIno());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@ResponseBody
	@PostMapping("/sellDelete")
	public ModelAndView deleteSharing(@RequestParam(value = "ino", required = false) Integer ino, Model model) {
		ModelAndView mav = new ModelAndView();
		try {
			if(session.getAttribute("authUser")!=null) {
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
					Users users = (Users) session.getAttribute("authUser");
					Integer sellerno = users.getUserno();
					chatRoomService.deleteChat(sellerno, ino);
				}
			}
			sellService.deleteSell(ino);
			mav.setViewName("redirect:/sellList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@ResponseBody
	@PostMapping("/sellView/likes")
	public Long registLikes(@RequestParam("ino") Integer ino) {
		Likes likes = new Likes();
		Long likescheck = null;
		try {
			likes.setIno(ino);
			if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
				Users user = (Users) session.getAttribute("authUser");
				likes.setUserno(user.getUserno());
			}
			Sell sell = new Sell();
			sell.setIno(ino);
			likescheck = likesService.getIlikescheck(likes);
			if(likescheck == null) {
				likesService.registIlikes(likes);
				sellService.upSellLikes(sell);
			}else if(likescheck == 1) {
				likes.setLikescheck(likesService.getIlikescheck(likes));
				likesService.updateIlikes(likes);
				sellService.downSellLikes(sell);
			}else {
				likes.setLikescheck(likesService.getIlikescheck(likes));
				likesService.updateIlikes(likes);
				sellService.upSellLikes(sell);
			}
			likescheck = likesService.getIlikescheck(likes);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return likescheck;
	}
	
	@PostMapping("/sellView/imessage")
	public ModelAndView submitMessage(@ModelAttribute MessageVO message, Model model, 
			@RequestParam("ino") Integer ino) {
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
			if(submitcheck == "true") {
				mav.addObject("submitcheck", "true");
			}else {
				mav.addObject("submitcheck", "false");
			}
			mav.setViewName("redirect:/sellView/"+ino);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
		
	}

	@ResponseBody
	@PostMapping("/sellView/wapply")
	public String sharingWapply(@RequestParam("ino") Integer ino, @ModelAttribute Sell sell, Model model,
			@RequestParam(value = "registcheck", required = false) String registcheck) {
		try {
			Wapply apply = new Wapply();
			apply.setIno(ino);
			Map<String, Object> map = new HashMap<String, Object>();
			if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
				Users users = (Users) session.getAttribute("authUser");
				apply.setWuserno(users.getUserno());
				map.put("wuserno", apply.getWuserno());
				map.put("ino", apply.getIno());
				if(applyService.selectIwapply(map) == null) {
					registcheck = applyService.registIwapply(apply);
					sellService.upApplycount(sell);
					if(registcheck == "true") {
						model.addAttribute("registcheck", "true");
					}else {
						model.addAttribute("registcheck", "false");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return registcheck;
	}
	
	@ResponseBody
	@PostMapping("/sellInfiniteScrollDown")
	public List<Sell> infiniteScrollDown(@RequestBody Map<String, Object> params) {
		String keyword = (String) params.get("keyword");
		Integer ino = Integer.parseInt((String) params.get("ino"));
		Integer inoToStart = ino - 1;
		List<Sell> sellList = new ArrayList<>();
		try {
			if (keyword != null && keyword != "") {
				sellList = sellService.infiniteScrollDown(inoToStart, keyword);
			} else {
				sellList = sellService.infiniteScrollDown(inoToStart);
			}

			for (int i = 0; i < sellList.size(); i++) {
				if (sellList.get(i).getIfileids() != null) {
					sellList.get(i).setIfileids(sellList.get(i).getIfileids().split(",")[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sellList;
	}
	
	
	//무료나눔 댓글 삭제하기
		@ResponseBody
		@PostMapping("/sellcmtDelete/{cno}/{ino}")
		public ModelAndView cmtDelete(@PathVariable("cno") Integer cno,
				@PathVariable("ino") Integer ino,Model model){
			ModelAndView mav = new ModelAndView();
			try {
				commentService.sellCmtDelete(cno,ino);
				mav.setViewName("redirect:/sellView/"+ino);
			}catch(Exception e) {
				e.printStackTrace();
				mav.addObject("err",e.getMessage());
			}
			return mav;
		}
		
		
			
		
		//댓글 수정하기 동작
		@PostMapping("/sellcmtModify/{cno}/{ino}")
		public ModelAndView cmtModifys(@ModelAttribute Comments comments,
									   @RequestParam("ccontent") String ccontent,
									   @PathVariable("ino") Integer ino,
									   @PathVariable("cno") Integer cno) {
			ModelAndView mav = new ModelAndView();
		    try {
		    	comments.setIno(ino);
		    	comments.setCno(cno);
				comments.setCcontent(ccontent);	
				commentService.modifysellCmt(comments);
				mav.setViewName("redirect:/sellView/"+comments.getIno());
					
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
			 
			return mav;
			}
		
		//댓글 리스트
		@RequestMapping("/Ilist/{ino}") //댓글 리스트
	    @ResponseBody
	    private List<Comments> ICommentServiceList(@PathVariable("ino") Integer ino, Model model) throws Exception{
	        return commentService.selectCommentino(ino);
	    }
	
}
