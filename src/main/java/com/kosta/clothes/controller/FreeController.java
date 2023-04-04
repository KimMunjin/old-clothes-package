package com.kosta.clothes.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.Comments;
import com.kosta.clothes.bean.Free;
import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.service.CommentService;
import com.kosta.clothes.service.FreeService;

@Controller
public class FreeController {
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	FreeService freeService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	HttpSession session;
	
	// 자유게시판 글 목록
	@GetMapping("/freeList")
	public ModelAndView freeList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "kwd", required = false) String kwd) {
		ModelAndView mav = new ModelAndView();
		PageInfo pageInfo = new PageInfo();
		List<Free> articleList;
		try {
			if (kwd != null && kwd != "") {
				articleList = freeService.getFreeList(kwd, page, pageInfo);
			} else {
			articleList = freeService.getFreeList(page,pageInfo);
			}
			mav.addObject("articleList", articleList);
			mav.addObject("kwd", kwd);
			mav.addObject("pageInfo", pageInfo);
			mav.setViewName("/free/freeList");
		} catch(Exception e){
			e.printStackTrace();
		}
		return mav;
	}
	
	//글 작성 폼 띄우기
		@GetMapping("/cmtModify")
		public String cmtModify() {
			return "free/cmtModify";
		}				
	
	//글 작성 폼 띄우기
	@GetMapping("/freeRegistForm")
	public String freeRegistForm() {
		return "free/freeRegistForm";
	}
	
	//글 등록 동작
	@PostMapping("/freeInsert")
	public ModelAndView boardwrite(@ModelAttribute Free free,
			HttpSession session,Model model ) {//값을 전부 받아온다.
		ModelAndView mav = new ModelAndView(); // 뷰 데이터 동시 설정 가능함
		
		try {
			if (session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
				Users users = (Users) session.getAttribute("authUser");
				free.setUserno(users.getUserno());
				
				String nickname=users.getNickname();
				free.setFname(nickname);
			}else {
				Business user = (Business)session.getAttribute("authUser");
				free.setBno(user.getBno());
				String nickname=user.getBname();
				free.setFname(nickname);
			}
				
			freeService.registFree(free); // board에 저장된 값을 Service에 있는 registBoard에 넘겨준다
			mav.setViewName("redirect:/freeList"); // 아래로 간다 글 쓰고 페이지 목록 보여주기 위해서
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	
	   //글 상세보기(조회수증가)
	   @GetMapping("freeView/{fno}")
	   public ModelAndView freeView(@PathVariable("fno") Integer fno,@RequestParam(value = "kwd", required = false) String kwd,
	         @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model) {
	      ModelAndView mav = new ModelAndView();      
	      try {
	    	  if(session.getAttribute("authUser")!=null) {//사용자가 로그인 했을 때 
		         if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
			            Users users = (Users)session.getAttribute("authUser");
			            if(users==null) {
			               model.addAttribute("logincheck", "false");//로그인했는지 여부-?jsp로      
			            }			            
			            Free free1 = freeService.Freehit(fno);
				        Free free = freeService.getFree(fno);				        				        
				        mav.addObject("article", free);
				        mav.addObject("page", page);
				        mav.addObject("kwd", kwd);
				        mav.setViewName("/free/freeView");
		         }else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")){ //사업자가 로그인 했을 때 
			            Business bauthuser=new Business();			            
			            bauthuser = (Business)session.getAttribute("authUser");
			            Free free1 = freeService.Freehit(fno);
				        Free free = freeService.getFree(fno);				       
				        mav.addObject("article", free);
				        mav.addObject("page", page);
				        mav.addObject("kwd", kwd);
				        mav.setViewName("/free/freeView");
		         }else {
		        	    Free free1 = freeService.Freehit(fno);
				        Free free = freeService.getFree(fno);				     
				        mav.addObject("article", free);
				        mav.addObject("page", page);
				        mav.addObject("kwd", kwd);
				        mav.setViewName("/free/freeView");
		         }
	    	  }else {
	    		   Free free1 = freeService.Freehit(fno);
			        Free free = freeService.getFree(fno);			        
			        mav.addObject("article", free);
			        mav.addObject("page", page);
			        mav.addObject("kwd", kwd);
			        mav.setViewName("/free/freeView");
	    	  }
	    } catch (Exception e) {
	         e.printStackTrace();      
	      }
	      return mav;
	   }   
	
		
		
	//as CK에디터
	@ResponseBody
	@PostMapping("/upload")
	public Map<String, Object> fileupload(@RequestParam(value="upload") MultipartFile file) {
		String path = "/home/ubuntu/app/oldclothes/upload/";
//		String path = servletContext.getRealPath("/upload/");
		String filename = file.getOriginalFilename();
		File destfile = new File(path+filename);
		Map<String, Object> json = new HashMap<>();
		try {
			file.transferTo(destfile);
			json.put("uploaded", 1);
			json.put("filename", filename);
			json.put("url", "/fileview/"+filename);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	//as CK에디터
	@GetMapping("/fileview/{filename}")
	public void fileview(@PathVariable String filename, HttpServletResponse response) {
		String path = "/home/ubuntu/app/oldclothes/upload/";
//		String path=servletContext.getRealPath("/upload/");
		File file = new File(path+filename);
		FileInputStream fis = null;
		try {
			OutputStream out = response.getOutputStream();
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
			out.flush();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis!=null) fis.close();
			} catch(Exception e) {}
		}
	}
	
	//글 수정하기 폼 띄우기
	@GetMapping("/modifyform/{fno}")
	public ModelAndView freeModify(@PathVariable("fno") String fno) {
		ModelAndView mav = new ModelAndView();
		try {
			Free free = freeService.getFree(Integer.parseInt(fno));
			mav.addObject("article",free);
			mav.setViewName("/free/freeModify");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
				
	//글 수정하기 동작
	@PostMapping("/freeModify/{fno}")
	public ModelAndView freeModify(@ModelAttribute Free free,
			@RequestParam("fcontent") String fcontent,
			@PathVariable("fno") String fno) {
		ModelAndView mav = new ModelAndView();
		try {
			free.setFno(Integer.parseInt(fno));
			free.setFcontent(fcontent);
			freeService.modifyFree(free);			
			mav.setViewName("redirect:/freeView/"+free.getFno());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
	
	//글 삭제하기
	@ResponseBody
	@PostMapping("/freeDelete")
	public ModelAndView freeDelete(@RequestParam("fno") String fno,Model model){
		ModelAndView mav = new ModelAndView();
		try {
			freeService.freecmtDel(Integer.parseInt(fno));
			freeService.freeDelete(Integer.parseInt(fno));
			mav.setViewName("redirect:/freeList");
		}catch(Exception e) {
			e.printStackTrace();
			mav.addObject("err",e.getMessage());
		}
		return mav;
	}
	

	//자유게시판 댓글
	//댓글 등록하기[통합]
			@PostMapping("/freeView/{fno}/{num}")
			public ModelAndView registcomments(@PathVariable("fno") Integer fno,
					@PathVariable("num") Integer no,
					@ModelAttribute Comments comments,Model model) {
				ModelAndView mav = new ModelAndView();
				try {
					if(session.getAttribute("authUser")!=null) {
						if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
							Integer userno = no;
							Users users = (Users)session.getAttribute("authUser");
							comments.setFno(fno);
							comments.setUserno(userno);
							comments.setCsect(users.getSect());
							comments.setCname(users.getNickname());
							commentService.registUcomment(comments, fno);					
							mav.setViewName("redirect:/freeView/"+fno);
						}else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")){
							Integer bno = no;
							Business business = (Business)session.getAttribute("authUser");
							String sect = business.getSect();
							comments.setFno(fno);
							comments.setBno(bno);
							comments.setCsect(sect);
							comments.setCname(business.getBname());
							commentService.registBcomment(comments, fno);			
							mav.setViewName("redirect:/freeView/"+fno);
						}
					}else {
						mav.setViewName("redirect:/login");
						return mav;
					}
				}catch(Exception e) {
					e.printStackTrace();
					
				}
				return mav;	
				
			}
	
	
	
	
	
	
	/*//댓글 등록하기[사용자]
		@PostMapping("/ufreeView/{fno}/{userno}")
		public ModelAndView comments(@PathVariable("fno") Integer fno,
				@PathVariable("userno") Integer userno,
				@ModelAttribute Comments comments,Model model) {
			ModelAndView mav = new ModelAndView();
			System.out.println("U댓글");
			try {
				Users users = (Users)session.getAttribute("authUser");
				comments.setFno(fno);
				comments.setUserno(userno);
				comments.setCsect(users.getSect());
				comments.setCname(users.getNickname());
				commentService.registUcomment(comments);					
				mav.setViewName("redirect:/freeView/"+fno);
			}catch(Exception e) {
				e.printStackTrace();
				
			}
			return mav;	
			
		}
		
		
		//댓글 등록하기[사업자]
		@PostMapping("/bfreeView/{fno}/{bno}")
		public ModelAndView bcomments(@PathVariable("fno") Integer fno,
				@PathVariable("bno") Integer bno,
				@ModelAttribute Comments comments,Model model) {
			ModelAndView mav = new ModelAndView();
			System.out.println("B댓글");
			try {
				Business business = (Business)session.getAttribute("authUser");
				System.out.println("sect : " + business.getSect());
				String sect = business.getSect();
				comments.setFno(fno);
				comments.setBno(bno);
				comments.setCsect(sect);
				comments.setCname(business.getBname());
				commentService.registBcomment(comments);			
				mav.setViewName("redirect:/freeView/"+fno);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return mav;
			
		}
		*/	
		
		
	//댓글 수정하기 동작	
	@PostMapping("/update/{cno}/{fno}")
	@ResponseBody
	public ModelAndView cmtModifys(@ModelAttribute Comments comments,
								   @RequestParam("ccontent") String ccontent,
								   @PathVariable("fno") Integer fno,
								   @PathVariable("cno") Integer cno) {
		ModelAndView mav = new ModelAndView();
	    try {
	    	comments.setFno(fno);
	    	comments.setCno(cno);
			comments.setCcontent(ccontent);	
			commentService.modifyCmt(comments);
			mav.setViewName("redirect:/freeView/"+comments.getFno());
				
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
		 
		return mav;
		}
		
	
	//댓글 삭제하기
	@ResponseBody
	@PostMapping("/cmtDelete/{cno}/{fno}")
	public ModelAndView cmtDelete(@PathVariable("cno") Integer cno,
			@PathVariable("fno") Integer fno,Model model){
		ModelAndView mav = new ModelAndView();
		try {
			commentService.CmtDelete(cno,fno);
			mav.setViewName("redirect:/freeView/"+fno);
		}catch(Exception e) {
			e.printStackTrace();			
		}
		return mav;
	}
	
	//댓글 리스트
	@RequestMapping("/list/{fno}") //댓글 리스트
    @ResponseBody
    private List<Comments> mCommentServiceList(@PathVariable("fno") Integer fno, Model model) throws Exception{
        return commentService.selectComments(fno);
    }
	//대댓글
	@PostMapping("/replycomment/{fno}/{num}/{cno}")
	@ResponseBody
	public boolean replycomment(@PathVariable("fno")Integer fno, @PathVariable("num") Integer no, @PathVariable("cno")Integer cno, @ModelAttribute Comments comments) throws Exception{
		if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
			Users users = (Users)session.getAttribute("authUser");
			comments.setCname(users.getNickname());
			comments.setCsect(users.getSect());
		}else if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Business")){
			Business business = (Business)session.getAttribute("authUser");
			comments.setCname(business.getBname());
			comments.setCsect(business.getSect());
		}
		return commentService.replycommentfree(fno, no, cno, comments);
	}
	
}
