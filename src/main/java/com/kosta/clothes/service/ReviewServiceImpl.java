package com.kosta.clothes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.bean.Review;
import com.kosta.clothes.dao.ReviewDAO;
import com.kosta.clothes.dao.SharingDAO;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	ReviewDAO reviewDAO;
	
	@Override
	public Integer reviewcount(Integer userno) throws Exception {

		return reviewDAO.reviewcount(userno);
	}

	@Override
	public List<Review> getReviewList(Integer userno, int rpage, PageInfo rpageInfo) throws Exception {
		int rlistCount = reviewDAO.sReviewListCount(userno);
		int slistCount = reviewDAO.iReviewListCount(userno);
		int listCount = rlistCount + slistCount;
		System.out.print("ReviewRow:" + listCount);
		int maxPage = (int)Math.ceil((double)listCount/10);  
		int startPage = rpage/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}		
		//pageInfo에 데이터 전달
		rpageInfo.setPage(rpage); 
		rpageInfo.setListCount(listCount);
		rpageInfo.setMaxPage(maxPage);
		rpageInfo.setStartPage(startPage);
		rpageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		Integer row = (rpage - 1) * 10 + 1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("row", row);
		return reviewDAO.getReviewList(map);
	}
}