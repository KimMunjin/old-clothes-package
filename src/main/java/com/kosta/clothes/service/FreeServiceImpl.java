package com.kosta.clothes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.Free;
import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.dao.FreeDAO;

@Service
public class FreeServiceImpl implements FreeService{
	
	@Autowired
	FreeDAO freeDao;

	@Override
	public void registFree(Free free) throws Exception {
		Integer freeNum = freeDao.selectMaxFreeNum();		
		if(freeNum == null) { //null이면 1로!
			freeNum = 1;
		}else {
			freeNum = freeNum + 1; //글 번호 자동 생성 +1씩 증가해준다.
		}		
		free.setFno(freeNum);
		free.setFreadcount(Long.valueOf(0));
		freeDao.insertFree(free);
		
	}
	//글 전체 개수 및 목록 (페이징)
	@Override
	public List<Free> getFreeList(int page, PageInfo pageInfo) throws Exception {
		int listCount = freeDao.selectFreeCount(); //전체 데이터 개수 가져오기 (전체 게시글 수)
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
		int row = (page - 1) * 10 + 1; //ex) (5페이지 -1) * 10 + 1 => 41
		return freeDao.selectFreeList(row);
	}
	
	//글 클릭하면 num으로 정보값들 가져오기
	@Override
	public Free getFree(Integer free_num) throws Exception {
		return freeDao.selectFree(free_num);
	}
	
	//글 수정하기
	@Override
	public void modifyFree(Free free) throws Exception {
		
		freeDao.updateFree(free);
	}

	//글 삭제하기
	@Override
	public void freeDelete(Integer freeNum) throws Exception {
		freeDao.deleteFree(freeNum);
	}
	
	//조회수 증가

	@Override
	public Free Freehit(Integer freadcount) throws Exception {
		return freeDao.hitFree(freadcount);
		
	}
	@Override
	public List<Free> getFreeList(String kwd, int page, PageInfo pageInfo) throws Exception {
		int listCount = freeDao.searchedFreeCount(kwd); //전체 데이터 개수 가져오기 (전체 게시글 수)
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
		Integer row = (page - 1) * 10 + 1; //ex) (5페이지 -1) * 10 + 1 => 41
		return freeDao.searchedFreeList(kwd, row);
	}
	@Override
	public void freecmtDel(Integer freeNum) throws Exception {
		freeDao.deleteCmt(freeNum);
		
	}
}
