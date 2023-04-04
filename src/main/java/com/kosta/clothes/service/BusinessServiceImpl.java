package com.kosta.clothes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.Apply;
import com.kosta.clothes.bean.Business;
import com.kosta.clothes.dao.ApplyDAO;
import com.kosta.clothes.dao.BusinessDAO;

@Service
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	BusinessDAO businessDao;
	
	@Autowired
	ApplyDAO applyDao;
	//시 구 검색
	@Override
	public List<Business> allBusinessInfo(String sido, String sigungu) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("sido", sido);
		map.put("sigungu", sigungu);
		return businessDao.selectBusiness(map);
	}
	
	//좋아요 관련 리스트
	@Override
	public List<Business> allBusinessInfo1(String sido, String sigungu, Integer userno) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		String userno1=Integer.toString(userno); 
		map.put("sido", sido);
		map.put("sigungu", sigungu);
		map.put("userno", userno1);
		return businessDao.selectBusiness1(map);
	}
	//카테고리 정렬 리스트
	/*
	 * @Override public List<Business> cateException(String sido, String sigungu,
	 * String category) throws Exception { Map<String,String> map = new
	 * HashMap<String,String>(); map.put("sido", sido); map.put("sigungu", sigungu);
	 * System.out.println("category: " + category + "이다");
	 * if(category.equals("review")) { System.out.println("review들어옴"); return
	 * businessDao.selectCatereview(map); }else if(category.equals("high")) {
	 * System.out.println("high들어옴"); return businessDao.selectCatehigh(map); }else
	 * { System.out.println("low들어옴"); return businessDao.selectCatelow(map); } }
	 */

	//카테고리 정렬 리스트
	@Override
	public List<Business> cateusers(String sido, String sigungu, String category)
			throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("sido", sido);
		map.put("sigungu", sigungu);
		if(category.equals("review")) {
			return businessDao.selectUserCatereview(map);
		}else if(category.equals("high")) {
			return businessDao.selectUserCatehigh(map);
		}else {
			return businessDao.selectUserCatelow(map);
		}
	}

	@Override
	public void registapply(Apply apply) throws Exception {
		applyDao.insertapply(apply);
	}

	@Override
	public Long likecheck(Integer bno, Integer userno) throws Exception {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("bno", bno);
		map.put("userno", userno);
		return businessDao.likecheck(map);
	}	
	


}
