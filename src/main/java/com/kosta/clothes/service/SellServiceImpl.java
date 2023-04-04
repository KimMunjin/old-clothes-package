package com.kosta.clothes.service;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.clothes.bean.FileVO;
import com.kosta.clothes.bean.Sell;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.dao.ApplyDAO;
import com.kosta.clothes.dao.CommentsDAO;
import com.kosta.clothes.dao.FileDAO;
import com.kosta.clothes.dao.LikesDAO;
import com.kosta.clothes.dao.ReviewDAO;
import com.kosta.clothes.dao.SellDAO;

@Service
public class SellServiceImpl implements SellService{
	@Autowired
	SellDAO sellDAO;
	
	@Autowired
	FileDAO fileDAO;
	
	@Autowired
	LikesDAO likesDAO;
	
	@Autowired
	ReviewDAO reviewDAO;	
	
	@Autowired
	CommentsDAO commentsDAO;
	
	@Autowired
	ApplyDAO applyDAO;
	
	@Autowired
	ServletContext servletContext;

	@Override
	public void registSell(Sell sell, MultipartFile[] files) throws Exception {
		Integer sellid = sellDAO.getNextIndividualNo();
		Sell sellvo = new Sell();
		sellvo.setIno(sellid);
		sellvo.setItitle(sell.getItitle());
		sellvo.setIcontent(sell.getIcontent());
		sellvo.setIstatus("등록완료");
		sellvo.setUserno(sell.getUserno());
		String fileids = "";
		FileVO fileVo = new FileVO();
		if(files!=null) {
			String path = "/home/ubuntu/app/oldclothes/upload/";
//			String path = servletContext.getRealPath("/upload/");
			for(MultipartFile file : files) {
				if(!file.isEmpty()) {
					fileVo.setDirectory_name(path);
					fileVo.setTname(file.getOriginalFilename());
					fileVo.setTsize(file.getSize());
					fileVo.setIno(sellid);
					fileVo.setContent_type(file.getContentType());
					fileDAO.insertFileInfo(fileVo);
					
					FileOutputStream fos = new FileOutputStream(path+fileVo.getTno());
					FileCopyUtils.copy(file.getBytes(), fos);
					
					fileids += fileVo.getTno()+",";
				}
				
			}
		}
		sellvo.setIfileids(fileids);
		sellvo.setIdealType(sell.getIdealType());
		sellvo.setPrice(sell.getPrice());
		sellvo.setIaddress(sell.getIaddress());
		
		sellDAO.insertIndividual(sellvo);
	}

	@Override
	public List<Sell> getSellList(String kwd) throws Exception {
		List<Sell> sellList = sellDAO.getSellSearchedList(kwd);
		for(int i = 0; i < sellList.size(); i++) {
			String addr = sellList.get(i).getIaddress();
			String[] addChange = addr.split(" ");
			if(addChange[2].matches("^.*.구$")) {
				String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
				sellList.get(i).setIaddress(join1);

			} else {
				String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
				sellList.get(i).setIaddress(join2);
			}
		}
		return sellList;
	}

	@Override
	public List<Sell> getSellList() throws Exception {
		List<Sell> sellList = sellDAO.getSellList();
		for(int i = 0; i < sellList.size(); i++) {
			String addr = sellList.get(i).getIaddress();
			String[] addChange = addr.split(" ");
			if(addChange[2].matches("^.*.구$")) {
				String join1 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).add(addChange[3]).toString();
				sellList.get(i).setIaddress(join1);

			} else {
				String join2 = new StringJoiner(" ").add(addChange[0]).add(addChange[1]).add(addChange[2]).toString();
				sellList.get(i).setIaddress(join2);
			}
		}
		return sellList;
	}

	@Override
	public Sell viewSell(Integer ino) throws Exception {
		return sellDAO.selectIndividual(ino);
	}

	@Override
	public Users getInickname(Integer ino) throws Exception {
		return sellDAO.getInickname(ino);
	}

	@Override
	public List<Sell> infiniteScrollDown(Integer inoToStart) throws Exception {
		return sellDAO.infiniteScrollDown(inoToStart);
	}

	@Override
	public List<Sell> infiniteScrollDown(Integer inoToStart, String kwd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ino", inoToStart);
		map.put("kwd", kwd);
		return sellDAO.searchedInfiniteScrollDown(map);
	}

	@Override
	public void modifySell(Sell sell) throws Exception {
		sellDAO.updateIndividual(sell);
	}

	@Override
	public void modifyIfileids(Sell sell, FileVO fileVo, MultipartFile[] files) throws Exception {
		fileDAO.deleteIfileInfo(sell.getIno());
		String fileids = "";
		FileVO nfileVo = new FileVO();
		if(files!=null) {
			String path = "/home/ubuntu/app/oldclothes/upload/";
//			String path = servletContext.getRealPath("/upload/");
			for(MultipartFile file : files) {
				if(!file.isEmpty()) {
					nfileVo.setDirectory_name(path);
					nfileVo.setTname(file.getOriginalFilename());
					nfileVo.setTsize(file.getSize());
					nfileVo.setIno(sell.getIno());
					nfileVo.setContent_type(file.getContentType());
					fileDAO.insertFileInfo(nfileVo);
					
					FileOutputStream fos = new FileOutputStream(path+nfileVo.getTno());
					FileCopyUtils.copy(file.getBytes(), fos);
					
					fileids += nfileVo.getTno()+",";
					sell.setIfileids(fileids);
				}
				
			}
		}
		sellDAO.updateIfileids(sell);
	}

	@Override
	public void deleteSell(Integer ino) throws Exception {
		likesDAO.deleteIlikes(ino);
		reviewDAO.deleteIReview(ino);
		commentsDAO.commentIDelete(ino);
		applyDAO.deleteIwapply(ino);
		sellDAO.deleteIndividual(ino);
		fileDAO.deleteIfileInfo(ino);
	}

	@Override
	public void upSellLikes(Sell sell) throws Exception {
		sellDAO.upIndividualLikes(sell);
	}

	@Override
	public void downSellLikes(Sell sell) throws Exception {
		sellDAO.downIndividualLikes(sell);
	}

	@Override
	public void upApplycount(Sell sell) throws Exception {
		sellDAO.upApplycount(sell);
	}

	@Override
	public Integer sellcount(Integer userno) throws Exception {
		return sellDAO.sellcount(userno);
		
	}

	@Override
	public Integer statuscount(Integer userno) throws Exception {
		return sellDAO.statuscount(userno);
	}

	@Override
	public void alterStatus(Integer userno, Integer ino) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userno", userno);
		map.put("ino", ino);
		sellDAO.alterStatus(map);
	}
	
}
