package com.kosta.clothes.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
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
import com.kosta.clothes.bean.Sharing;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.bean.Wapply;
import com.kosta.clothes.service.ApplyService;
import com.kosta.clothes.service.CommentService;
import com.kosta.clothes.service.LikesService;
import com.kosta.clothes.service.MessageService;
import com.kosta.clothes.service.MypageService;
import com.kosta.clothes.service.ReviewService;
import com.kosta.clothes.service.SharingService;

@Controller
public class SharingController {

	@Autowired
	SharingService sharingService;

	@Autowired
	LikesService likesService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired 
	MessageService messageService;
	 
	@Autowired 
	ApplyService applyService;
	
	@Autowired
	MypageService mypageService;
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	ServletContext servletContext;

	@Autowired
	HttpSession session;

	@GetMapping("/sharingList")
	public ModelAndView main(HttpServletRequest request, @RequestParam(value = "kwd", required = false) String kwd) {
		ModelAndView mav = new ModelAndView();
		List<Sharing> sharingList;
		try {
			if (kwd != null && kwd != "") {
				sharingList = sharingService.getSharingList(kwd);
			} else {
				sharingList = sharingService.getSharingList();
			}
			for (int i = 0; i < sharingList.size(); i++) {
				if (sharingList.get(i).getSfileids() != null) {
					sharingList.get(i).setSfileids(sharingList.get(i).getSfileids().split(",")[0]);
				}
			}
			mav.addObject("sharingList", sharingList);
			mav.addObject("kwd", kwd);
			
			mav.setViewName("/sharing/sharingList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping("/sharingRegistForm")
	public String sharingRegistForm() {
		return "/sharing/sharingRegistForm";
	}
	
	//신청확인
	@GetMapping("/sharingapplyList/{sno}")
	public ModelAndView sharingapplyList(@PathVariable("sno") Integer sno, HttpServletRequest request, @RequestParam(value = "kwd", required = false) String kwd) {
		ModelAndView mav = new ModelAndView();
		try {
			mav.setViewName("/sharing/sharingapplyList");
			List<Users> users = mypageService.getSharingapplylist(sno);
			Sharing sharing = sharingService.viewSharing(sno);
			mav.addObject("users",users);
			mav.addObject("sharing",sharing);
			mav.addObject("sno",sno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@ResponseBody
	@PostMapping("/sharingRegist")
	public ModelAndView registSharing(@ModelAttribute Sharing sharing,
			@RequestParam("simageFile") MultipartFile[] files) {
		ModelAndView mav = new ModelAndView();
		try {
			Users users = (Users) session.getAttribute("authUser");
			if (users != null) {
				sharing.setUserno(users.getUserno());
				sharingService.registSharing(sharing, files);
			}
			mav.setViewName("/sharing/sharingList");
			mav.setViewName("redirect:/sharingList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping("/sharingView/{sno}")
	public ModelAndView viewSharing(@PathVariable("sno") Integer sno, Model model, @RequestParam(value = "submitcheck", required = false) String submitcheck) {
		ModelAndView mav = new ModelAndView();
		try {
			Sharing sharing = sharingService.viewSharing(sno);
			//Integer reviewcount = reviewService.reviewcount(sharing.getUserno()); //거래후기 개수 (현재 삭제)
			//model.addAttribute("reviewcount", reviewcount);
			if (sharing.getSfileids() != null) { //tfile에 있는 파일 번호가 개수 만큼 sfileid로 들어감 
				String[] fidArr = sharing.getSfileids().split(","); // 1,2,3이라는 문자열로 돼있으면 콤마로 잘라서 스트링 배열로 만들어줌
				mav.addObject("files", fidArr);
			}
			Business bauthuser=new Business();
			String sect;
			Users uauthuser=new Users();
			if(session.getAttribute("authUser") != null) {
				if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
					uauthuser = (Users) session.getAttribute("authUser");
					sect = uauthuser.getSect();
					model.addAttribute("sect", sect);
					List<Users> users = mypageService.getSharingapplylist(sno);
					mav.addObject("users",users);
					mav.setViewName("/sharing/sharingView");
				} else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")){
					bauthuser = (Business) session.getAttribute("authUser");
					sect = bauthuser.getSect();
					model.addAttribute("sect", sect);
					Users uservo = new Users();					
					uservo = sharingService.getSnickname(sno);
					model.addAttribute("uservo", uservo);
					model.addAttribute("submitcheck", submitcheck); //쪽지 발송 완료 확인
					mav.addObject("sharing", sharing);
					mav.setViewName("/sharing/sharingView");
				}else {
					
				}
				

			}else {
				Users uservo = new Users();				
				uservo = sharingService.getSnickname(sno);
				model.addAttribute("uservo", uservo);
				model.addAttribute("submitcheck", submitcheck);
				mav.addObject("sharing", sharing);
				mav.setViewName("/sharing/sharingView");
			}
			//개인
			if (uauthuser.getUserno() == null && bauthuser.getBno() == null) {
				model.addAttribute("logincheck", "false");
			}else if (uauthuser.getUserno() != null){
				Likes likevo = new Likes();
				likevo.setSno(sno);
				likevo.setUserno(uauthuser.getUserno());
				Long likeselect = likesService.getSlikescheck(likevo);
				if (likeselect != null) { //slikescheck로 좋아요 확인 (1이면 빨간 하트, 0이면 빈하트로 보이게)
					mav.addObject("likes", likeselect);
				}
			}else {
				
			}
			Users uservo = new Users();
			uservo = sharingService.getSnickname(sno);
			model.addAttribute("uservo", uservo);
			model.addAttribute("submitcheck", submitcheck);
			mav.addObject("sharing", sharing);
			mav.setViewName("/sharing/sharingView");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	//무료나눔 댓글등록
	//댓글 등록하기
	@PostMapping("/sharingView/{sno}/{userno}")
	public ModelAndView comments(@PathVariable("sno") Integer sno,
			@PathVariable("userno") Integer userno,
			@ModelAttribute Comments comments,Model model) {
		ModelAndView mav = new ModelAndView();
		try {
			Users users = (Users)session.getAttribute("authUser");
			comments.setSno(sno);
			comments.setUserno(userno);
			comments.setCname(users.getNickname());			
			commentService.registCommentshar(comments);
			mav.setViewName("redirect:/sharingView/"+sno);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
		
	}

	
	//댓글 리스트
	@RequestMapping("/Slist/{sno}") //댓글 리스트
    @ResponseBody
    private List<Comments> SCommentServiceList(@PathVariable("sno") Integer sno, Model model) throws Exception{
        return commentService.selectCommentsno(sno);
    }
	
	@GetMapping("/sharingModifyForm")
	public ModelAndView modifySharing(@RequestParam("sno") Integer sno) {
		ModelAndView mav = new ModelAndView();
		try {
			Sharing sharing = sharingService.viewSharing(sno);
			mav.addObject("sharing", sharing);
			mav.setViewName("/sharing/sharingModifyForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@PostMapping("/sharingModify")
	public ModelAndView modifySharing(@ModelAttribute Sharing sharing, @ModelAttribute FileVO fileVo,
			@RequestParam("simageFile") MultipartFile[] files) {		
		ModelAndView mav = new ModelAndView();
		try {
			sharingService.modifySharing(sharing);
			sharingService.modifySfileids(sharing, fileVo, files); // sharing테이블의 sfileids는 수정하고, tfile에는 새로운 첨부파일이 등록되도록
			// String[] fidArr = sharing.getSfileids().split(",");
			// mav.addObject("files", fidArr);
			mav.setViewName("redirect:/sharingView/" + sharing.getSno());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@ResponseBody
	@PostMapping("/sharingDelete")
	public ModelAndView deleteSharing(@RequestParam(value = "sno", required = false) Integer sno, Model model) {
		ModelAndView mav = new ModelAndView();
		try {
			sharingService.deleteSharing(sno); 
			mav.setViewName("redirect:/sharingList"); //삭제는 ajax로 확인 버튼을 눌렀을 때 삭제되도록
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@ResponseBody
	@PostMapping("/sharingView/likes")
	public Long registLikes(@RequestParam("sno") Integer sno) {
		Likes likes = new Likes();
		Long likescheck = null;
		try {
			likes.setSno(sno);
			if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
				Users user = (Users) session.getAttribute("authUser");
				likes.setUserno(user.getUserno());
			}
			Sharing sharing = new Sharing();
			sharing.setSno(sno);
			likescheck = likesService.getSlikescheck(likes); //likescheck를 가져와(지금 무슨 상태죠?)
			if(likescheck == null) { //처음 눌렀을 때(테이블에 값이 아예 없을 때)
				likesService.registSlikes(likes);
				sharingService.upSharingLikes(sharing);
			}else if(likescheck == 1) {
				likes.setLikescheck(likesService.getSlikescheck(likes)); //사용자가 해당 글번호에 좋아요를 눌렀는지 확인
				likesService.updateSlikes(likes); //likes 컬럼을 0으로 바꾸고
				sharingService.downSharingLikes(sharing); //sharing 테이블의 likescheck(총 좋아요를 받은 개수)를 -1 해줌
			}else { //likescheck가 0일 때 1로 올려주는
				likes.setLikescheck(likesService.getSlikescheck(likes));
				likesService.updateSlikes(likes);
				sharingService.upSharingLikes(sharing);
			}
			likescheck = likesService.getSlikescheck(likes);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return likescheck;
	}
	
	@PostMapping("/sharingView/smessage")
	public ModelAndView submitMessage(@ModelAttribute MessageVO message, Model model, @RequestParam("sno") Integer sno) {
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
			mav.setViewName("redirect:/sharingView/"+sno);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
		
	}
	
	@ResponseBody
	@PostMapping("/sharingView/wapply")
	public String sharingWapply(@RequestParam("sno") Integer sno, @ModelAttribute Sharing sharing, Model model,
			@RequestParam(value = "registcheck", required = false) String registcheck) {
		ModelAndView mav = new ModelAndView();
		try {
			Wapply apply = new Wapply();
			apply.setSno(sno);
			Map<String, Object> map = new HashMap<String, Object>();
			if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
				Users users = (Users) session.getAttribute("authUser");
				apply.setWuserno(users.getUserno());
				map.put("wuserno", apply.getWuserno());
				map.put("sno", apply.getSno());
				if(applyService.selectSwapply(map) == null) {
					registcheck = applyService.registSwapply(apply);
					sharingService.upApplycount(sharing); //신청인원 개수 올려줌
					if(registcheck == "true") { //신청되었는지 확인
						mav.addObject("registcheck", "true");
					} else {
						mav.addObject("registcheck", "false");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return registcheck;
	}

	/* commons에 필요한 애들 */
	@GetMapping("/img/{sfileids}")
	public void viewImage(@PathVariable Integer sfileids, HttpServletResponse response) {
		String path = "/home/ubuntu/app/oldclothes/upload/";
//		String path = servletContext.getRealPath("/upload/");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path + sfileids);
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(fis, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@GetMapping("/upload/{fileid}")
	public void imgView(@PathVariable String fileid, HttpServletResponse response) {
		String path = "/home/ubuntu/app/oldclothes/upload/";
//		String path = servletContext.getRealPath("/upload/");
		try {
			FileInputStream fis = new FileInputStream(path + fileid);
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(fis, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@ResponseBody
	@PostMapping("/infiniteScrollDown")
	public List<Sharing> infiniteScrollDown(@RequestBody Map<String, Object> params) {
		String keyword = (String) params.get("keyword");
		Integer sno = Integer.parseInt((String) params.get("sno"));
		Integer snoToStart = sno - 1;
		List<Sharing> sharingList = new ArrayList<>();
		try {
			if (keyword != null && keyword != "") {
				sharingList = sharingService.infiniteScrollDown(snoToStart, keyword);
			} else {
				sharingList = sharingService.infiniteScrollDown(snoToStart);
			}

			for (int i = 0; i < sharingList.size(); i++) {
				if (sharingList.get(i).getSfileids() != null) {
					sharingList.get(i).setSfileids(sharingList.get(i).getSfileids().split(",")[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sharingList;

	}
	
	//무료나눔 댓글 삭제하기
	@ResponseBody
	@PostMapping("/sharingcmtDelete/{cno}/{sno}")
	public ModelAndView cmtDelete(@PathVariable("cno") Integer cno,
			@PathVariable("sno") Integer sno,Model model){
		ModelAndView mav = new ModelAndView();
		try {
			commentService.sharingCmtDelete(cno,sno);
			mav.setViewName("redirect:/sharingView/"+sno);
		}catch(Exception e) {
			e.printStackTrace();			
		}
		return mav;
	}	
	
	//댓글 수정하기 동작
	@PostMapping("/sharingcmtModify/{cno}/{sno}")
	public ModelAndView cmtModifys(@ModelAttribute Comments comments,
								   @RequestParam("ccontent") String ccontent,
								   @PathVariable("sno") Integer sno,
								   @PathVariable("cno") Integer cno) {
		ModelAndView mav = new ModelAndView();
	    try {
	    	comments.setSno(sno);
	    	comments.setCno(cno);
			comments.setCcontent(ccontent);	
			commentService.modifysharingCmt(comments);
			mav.setViewName("redirect:/sharingView/"+comments.getSno());
				
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
		 
		return mav;
		}
}
