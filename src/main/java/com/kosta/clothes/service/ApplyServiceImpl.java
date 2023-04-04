package com.kosta.clothes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.Apply;
import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.bean.Wapply;
import com.kosta.clothes.dao.ApplyDAO;

@Service
public class ApplyServiceImpl implements ApplyService{

	@Autowired
	ApplyDAO applyDAO;
	
	@Override
	public String registSwapply(Wapply wapply) throws Exception {
		applyDAO.insertSwapply(wapply);
		return "true";
	}

	@Override
	public String registIwapply(Wapply wapply) throws Exception {
		applyDAO.insertIwapply(wapply);
		return "true";
	}

	@Override
	public Wapply selectSwapply(Map map) throws Exception {
		return applyDAO.getSapply(map);
	}

	@Override
	public Wapply selectIwapply(Map map) throws Exception {
		return applyDAO.getIapply(map);
	}


	@Override
	public Integer applycount(Integer bno) throws Exception {
		return  applyDAO.applycount(bno);
	}

	//사용자 -> 사업자 신청목록
	@Override
	public List<Apply> getBapply(Integer bno) throws Exception {
		return applyDAO.getBapply(bno);
	}
	//사용자가 신청한 목록
	@Override
	public List<Apply> getUapply(Integer userno) throws Exception {
		// TODO Auto-generated method stub
		return applyDAO.getUapply(userno);
	}

	@Override
	public Map astatuscount(Integer bno) throws Exception { 
		Integer a=applyDAO.astatuscount(bno);
		Integer b=applyDAO.bstatuscount(bno);
		Integer c=applyDAO.cstatuscount(bno);
		Integer d=applyDAO.dstatuscount(bno);
		Integer e=applyDAO.estatuscount(bno);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("후기등록완료", a);
		map.put("신청거절", b);
		map.put("수거완료", c);
		map.put("신청중", d);
		map.put("신청완료", e);
		
		return  map;
	}

	//수거승인시 	

		@Override
		public void updateApply(Integer ano, Integer userno, String astatus) throws Exception{
			applyDAO.updateApply(ano,userno,astatus);
		}

		@Override
		public void deleteApply(Integer ano, Integer userno,String astatus) throws Exception {
			applyDAO.deleteApply(ano,userno,astatus);
		}

		@Override
		public void deletewApply(Integer ano, Integer userno) throws Exception {
			applyDAO.deletewApply(ano,userno);
			
		}

		@Override
		public List<Apply> getApplyList(Integer userno, int apage, PageInfo apageInfo) throws Exception {
			int applyCount = applyDAO.applyListCount(userno);
			int maxPage = (int)Math.ceil((double)applyCount/6);  
			int startPage = apage/6 * 6 + 1;
			int endPage = startPage + 6 -1; 
			if(endPage > maxPage) { 
				endPage = maxPage; 
			}		
			//pageInfo에 데이터 전달
			apageInfo.setPage(apage); 
			apageInfo.setListCount(applyCount);
			apageInfo.setMaxPage(maxPage);
			apageInfo.setStartPage(startPage);
			apageInfo.setEndPage(endPage);
			
			//검색한 페이지의 시작 페이지 값을 구한 변수 
			Integer row = (apage - 1) * 5 + 1;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userno", userno);
			map.put("row", row);
			return applyDAO.getApplyList(map);
			
		}

		@Override
		public List<Apply> bgetApplyList(Integer bno, int bapage, PageInfo bapageInfo) throws Exception {
			int applyCount = applyDAO.bapplyListCount(bno);
			int maxPage = (int)Math.ceil((double)applyCount/6);  
			int startPage = bapage/6 * 6 + 1;
			int endPage = startPage + 6 -1; 
			if(endPage > maxPage) { 
				endPage = maxPage; 
			}		
			//pageInfo에 데이터 전달
			bapageInfo.setPage(bapage); 
			bapageInfo.setListCount(applyCount);
			bapageInfo.setMaxPage(maxPage);
			bapageInfo.setStartPage(startPage);
			bapageInfo.setEndPage(endPage);
			
			//검색한 페이지의 시작 페이지 값을 구한 변수 
			Integer row = (bapage - 1) * 5 + 1;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bno", bno);
			map.put("row", row);
			return applyDAO.bgetApplyList(map);
		}
}
