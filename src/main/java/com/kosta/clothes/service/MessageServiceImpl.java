package com.kosta.clothes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.MessageVO;
import com.kosta.clothes.bean.PageInfo;
import com.kosta.clothes.dao.MessageDAO;

@Service
public class MessageServiceImpl implements MessageService{

	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	ServletContext servletContext;
	
	@Override
	public String submitMessage(MessageVO message, String sect) throws Exception {

		if(sect.equals("users")) {
			messageDAO.insertMessage(message);
		} else {
			messageDAO.insertSBMessage(message);
		}
		return "true";
	}

	@Override
	public List<MessageVO> uRecvMessage(Map map) throws Exception { //개인이 받은 쪽지함
		//페이징
		int listCount = messageDAO.selectRmessageCount((Integer)map.get("recvUserno")); //전체 데이터 개수 가져오기 (전체 게시글 수)
		listCount += messageDAO.selectBmessageCount((Integer)map.get("recvUserno"));
		int maxPage = (int)Math.ceil((double)listCount/10);
		PageInfo rpageInfo = new PageInfo();
		rpageInfo = (PageInfo) map.get("pageInfo");
		Integer page = (Integer) map.get("page");
		int startPage = page/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		rpageInfo.setPage(page); 
		rpageInfo.setListCount(listCount);
		rpageInfo.setMaxPage(maxPage);
		rpageInfo.setStartPage(startPage);
		rpageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		int row = (page - 1) * 10 + 1; //ex) (5페이지 -1) * 10 + 1 => 41
		map.put("row", row);
		return messageDAO.uRecvMessage(map);
	}

	@Override
	public List<MessageVO> bRecvMessage(Map map) throws Exception { //사업자가 받은 쪽지함
		int listCount = messageDAO.selectBRmessageCount((Integer)map.get("recvUserno")); //전체 데이터 개수 가져오기 (전체 게시글 수)
		listCount += messageDAO.selectBRBmessageCount((Integer)map.get("recvUserno"));
		int maxPage = (int)Math.ceil((double)listCount/10);
		PageInfo rpageInfo = new PageInfo();
		rpageInfo = (PageInfo) map.get("pageInfo");
		Integer page = (Integer) map.get("page");
		int startPage = page/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		rpageInfo.setPage(page); 
		rpageInfo.setListCount(listCount);
		rpageInfo.setMaxPage(maxPage);
		rpageInfo.setStartPage(startPage);
		rpageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		int row = (page - 1) * 10 + 1; //ex) (5페이지 -1) * 10 + 1 => 41
		map.put("row", row);
		return messageDAO.bRecvMessage(map);
	}

	@Override
	public List<MessageVO> uSendMessage(Integer sendUserno, Integer page, PageInfo spageInfo) throws Exception { //개인이 보낸 쪽지함
		int listCount = messageDAO.selectSmessageCount(sendUserno); //전체 데이터 개수 가져오기 (전체 게시글 수)
		listCount += messageDAO.selectSBmessageCount(sendUserno);
		int maxPage = (int)Math.ceil((double)listCount/10);  
		int startPage = page/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		spageInfo.setPage(page); 
		spageInfo.setListCount(listCount);
		spageInfo.setMaxPage(maxPage);
		spageInfo.setStartPage(startPage);
		spageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		int row = (page - 1) * 10 + 1; //ex) (5페이지 -1) * 10 + 1 => 41
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		map.put("sendUserno", sendUserno);
		map.put("spageInfo", spageInfo);
		return messageDAO.uSendMessage(map);
	}

	@Override
	public List<MessageVO> bSendMessage(Integer sendUserno, Integer spage, PageInfo pageInfo) throws Exception {//사업자가 보낸 쪽지함
		int listCount = messageDAO.selectBSmessageCount(sendUserno); //전체 데이터 개수 가져오기 (전체 게시글 수)
		listCount += messageDAO.selectBSBmessageCount(sendUserno);
		int maxPage = (int)Math.ceil((double)listCount/10);  
		int startPage = spage/10 * 10 + 1; 
		int endPage = startPage + 10 -1; 
		if(endPage > maxPage) { 
			endPage = maxPage; 
		}
		
		//pageInfo에 데이터 전달
		pageInfo.setPage(spage); 
		pageInfo.setListCount(listCount);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		//검색한 페이지의 시작 페이지 값을 구한 변수 
		int row = (spage - 1) * 10 + 1; //ex) (5페이지 -1) * 10 + 1 => 41
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		map.put("sendUserno", sendUserno);
		map.put("spageInfo", pageInfo);
		return messageDAO.bSendMessage(map);
	}

	@Override
	public void deleteMessage(Integer mno) throws Exception {
		messageDAO.deleteMessage(mno);
	}

	@Override
	public MessageVO uRecvViewMessage(Integer mno) throws Exception {
		return messageDAO.uRecvViewMessage(mno);
	}

	@Override
	public MessageVO bRecvViewMessage(Integer mno) throws Exception {
		return messageDAO.bRecvViewMessage(mno);
	}

	@Override
	public MessageVO uSendViewMessage(Integer mno) throws Exception {
		return messageDAO.uSendViewMessage(mno);
	}

	@Override
	public MessageVO bSendViewMessage(Integer mno) throws Exception {
		return messageDAO.bSendViewMessage(mno);
	}

	@Override
	public Business mypageBusiness(Integer bno) throws Exception {
		return messageDAO.mypageBusiness(bno);
	}




}
