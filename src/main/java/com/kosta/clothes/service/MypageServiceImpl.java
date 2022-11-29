package com.kosta.clothes.service;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.Sell;
import com.kosta.clothes.dao.MypageDAO;

@Service
public class MypageServiceImpl implements MypageService {
	
	@Autowired
	MypageDAO mypageDAO;
	
	@Autowired
	ServletContext servletContext;

	@Override
	public List<Sell> getSellList(Integer userno) throws Exception {
		return mypageDAO.getSellList(userno);
	}

}
