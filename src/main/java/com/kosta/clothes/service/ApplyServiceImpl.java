package com.kosta.clothes.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.Apply;
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



}
