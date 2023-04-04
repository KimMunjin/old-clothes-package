package com.kosta.clothes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.bean.Sell;
import com.kosta.clothes.bean.Sharing;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.dao.MypageDAO;

@Service
public class MypageServiceImpl implements MypageService {
	
	@Autowired
	MypageDAO mypageDAO;
	
	@Autowired
	ServletContext servletContext;

	@Override
	public List<Sell> getSellList(Integer userno, int page, PageInfo pageInfo) throws Exception {
		int listCount = mypageDAO.sellListCount(userno); //전체 데이터 개수 가져오기 (전체 게시글 수)
		int maxPage = (int)Math.ceil((double)listCount/10);  
		int startPage = page/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		pageInfo.setPage(page); 
		pageInfo.setListCount(listCount);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		Integer row = (page - 1) * 10 + 1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("row", row);
		return mypageDAO.getSellList(map);
	}


	@Override
	public List<Sharing> getSharingList(Integer userno, int spage, PageInfo spageInfo) throws Exception {
		int listCount = mypageDAO.sharingListCount(userno); //전체 데이터 개수 가져오기 (전체 게시글 수)
		int maxPage = (int)Math.ceil((double)listCount/10);  
		int startPage = spage/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		spageInfo.setPage(spage); 
		spageInfo.setListCount(listCount);
		spageInfo.setMaxPage(maxPage);
		spageInfo.setStartPage(startPage);
		spageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		Integer row = (spage - 1) * 10 + 1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("row", row);
		return mypageDAO.getSharingList(map);
	}

	@Override
	public List<Users> getSellapplylist(Integer ino) throws Exception {
		return mypageDAO.getSellapplylist(ino);
	}
	@Override
	public String uintroduce(String introduce, Integer userno) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("introduce", introduce);
		map.put("userno", userno);		
		mypageDAO.uintroduce(map);
		return mypageDAO.uintromyself(userno);
	}

	@Override
	public String bintroduce(String bintroduce, Integer bno) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bintroduce", bintroduce);
		map.put("bno", bno);
		mypageDAO.bintroduce(map);
		return mypageDAO.bintromyself(bno);

	}


	@Override

	public List<Users> getSharingapplylist(Integer sno) throws Exception {
		return mypageDAO.getSharingapplylist(sno);
	}
	public Users getMypage(Integer userno) throws Exception {		
		return mypageDAO.getMypage(userno);

	}


	@Override
	public void selectSellApply(Integer userno, Integer ino) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("ino", ino);
		mypageDAO.selectSellApply(map);
		
	}


	@Override
	public void selectSharingApply(Integer userno, Integer sno) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("sno", sno);
		mypageDAO.selectSharingApply(map);
	}


	@Override
	public void cancelDeal(Integer ino) throws Exception {
		mypageDAO.cancelDeal(ino);
	}


	@Override
	public void cancelSharingDeal(Integer sno) throws Exception {
		mypageDAO.cancelSharingDeal(sno);
	}


	@Override
	public void completeDeal(Integer ino) throws Exception {
		mypageDAO.upDealCount(ino);
		Sell sell = mypageDAO.completedCount(ino);
		Long count1 = sell.getSellCompletedCount();
		Long count2 = sell.getBuyCompletedCount();
		if(count1 == 1 && count2 == 1) {
			mypageDAO.completeDeal(ino);
		}
	}
	
	@Override
	public void completeSeDeal(Integer ino) throws Exception {
		mypageDAO.upSeDealCount(ino);
		Sell sell = mypageDAO.completedCount(ino);
		Long count1 = sell.getSellCompletedCount();
		Long count2 = sell.getBuyCompletedCount();
		if(count1 == 1 && count2 == 1) {
			mypageDAO.completeDeal(ino);
		}
	}

	@Override
	public void completeSharingDeal(Integer sno) throws Exception {
		mypageDAO.upSharingDealCount(sno);
		Sharing sharing = mypageDAO.completedSharingCount(sno);
		Long count1 = sharing.getSellCompletedCount();
		Long count2 = sharing.getBuyCompletedCount();
		if(count1 == 1 && count2 == 1) {
			mypageDAO.completeSharingDeal(sno);
		}
	}
	
	@Override
	public void completeShDeal(Integer sno) throws Exception {
		mypageDAO.upShDealCount(sno);
		Sharing sharing = mypageDAO.completedSharingCount(sno);
		Long count1 = sharing.getSellCompletedCount();
		Long count2 = sharing.getBuyCompletedCount();
		if(count1 == 1 && count2 == 1) {
			mypageDAO.completeSharingDeal(sno);
		}
	}


	@Override
	public List<Sell> getBuySellList(Integer userno, int bspage, PageInfo bspageInfo) throws Exception {
		int listCount = mypageDAO.buySellListCount(userno); //전체 데이터 개수 가져오기 (전체 게시글 수)
		int maxPage = (int)Math.ceil((double)listCount/10);  
		int startPage = bspage/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		bspageInfo.setPage(bspage); 
		bspageInfo.setListCount(listCount);
		bspageInfo.setMaxPage(maxPage);
		bspageInfo.setStartPage(startPage);
		bspageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		Integer row = (bspage - 1) * 10 + 1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("row", row);
		return mypageDAO.getBuySellList(map);
	}


	@Override
	public List<Sharing> getBuySharingList(Integer userno, int ppage, PageInfo ppageInfo) throws Exception {
		int listCount = mypageDAO.buySharingListCount(userno); //전체 데이터 개수 가져오기 (전체 게시글 수)
		int maxPage = (int)Math.ceil((double)listCount/10);  
		int startPage = ppage/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		ppageInfo.setPage(ppage); 
		ppageInfo.setListCount(listCount);
		ppageInfo.setMaxPage(maxPage);
		ppageInfo.setStartPage(startPage);
		ppageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		Integer row = (ppage - 1) * 10 + 1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("row", row);
		return mypageDAO.getBuySharingList(map);
	}


	@Override
	public List<Sharing> getLikeSharingList(Integer userno) throws Exception {
		return mypageDAO.getLikeSharingList(userno);//무료나눔 좋아요 리스트 불러오기
	}


	@Override
	public void sendIReview(Map map) throws Exception {
		mypageDAO.sendIReview(map);
		mypageDAO.changeIReviewStatus(map);
	}
	
	@Override
	public void sendSReview(Map map) throws Exception {
		mypageDAO.sendSReview(map);
		mypageDAO.changeSReviewStatus(map);
	}
	public List<Sell> getLikeSellList(Integer userno) throws Exception {
		return mypageDAO.getLikeSellList(userno);//개인판매 좋아요 리스트 불러오기
	}


	@Override
	public List<Business> getLikeBusinessList(Integer userno) throws Exception {
		return mypageDAO.getLikeBusinessList(userno);//업체 좋아요 리스트 불러오기
	}
	@Transactional
	@Override
	public void deletesLike(Integer userno, Integer sno) throws Exception {
		mypageDAO.deletesLikecount(sno);//무료나눔 테이블에 있는 좋아요 카운트 -1
		mypageDAO.deletesLike(userno, sno);//좋아요 테이블에 있는 likescheck 0
	}


	@Override
	public void deleteiLike(Integer userno, Integer ino) throws Exception {
		mypageDAO.deleteiLikecount(ino);//개인판매 테이블에 있는 좋아요 카운트 -1
		mypageDAO.deleteiLike(userno, ino);	//좋아요 테이블에 있는 likescheck 0
	}


	@Override
	public void deletebLike(Integer userno, Integer bno) throws Exception {
		mypageDAO.deletebLike(userno, bno); //좋아요 테이블에 있는 likescheck 0
	}


	@Override
	public void sendUapplyReview(Map map) throws Exception {
		mypageDAO.uapplyReviewCompleted(map);
		mypageDAO.sendUapplyReview(map);
	}


	@Override
	public void uapplyReviewStatus(Integer ano) throws Exception {
		mypageDAO.uapplyReviewStatus(ano);
	}


	


	




}
