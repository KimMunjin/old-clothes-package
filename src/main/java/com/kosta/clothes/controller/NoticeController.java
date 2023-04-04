package com.kosta.clothes.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.clothes.bean.Notice;
import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.service.NoticeService;

@Controller
public class NoticeController {
	
	@Autowired
	ServletContext servletContext;

	@Autowired
	NoticeService noticeService;
	
	@Autowired
	HttpSession session;
	
	//공지사항 글 목록
	@GetMapping("noticeList")
	public ModelAndView noticeList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "kwd", required = false) String kwd) {
		ModelAndView mav = new ModelAndView();
		PageInfo pageInfo = new PageInfo();
		List<Notice> articleList;
		try {
			if (kwd != null && kwd != "") {
				articleList = noticeService.getNoticeList(kwd, page, pageInfo);
			} else {
			articleList = noticeService.getNoticeList(page,pageInfo);
			}
			mav.addObject("articleList", articleList);
			mav.addObject("pageInfo", pageInfo);
			mav.setViewName("/notice/noticeList");
		} catch(Exception e){
			e.printStackTrace();
		}
		return mav;
	}
	
	 //글 상세보기(조회수증가)
		@GetMapping("noticeView/{nno}")
		public ModelAndView freeView(@PathVariable("nno") Integer nno,
				@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model) {
			ModelAndView mav = new ModelAndView();
			try {				
				 noticeService.Noticehit(nno);
				Notice notice = noticeService.getNotice(nno);
				mav.addObject("article", notice);
				mav.addObject("page", page);
				mav.setViewName("/notice/noticeView");
				
			} catch (Exception e) {
				e.printStackTrace();		
			}
			return mav;
		}	
}
