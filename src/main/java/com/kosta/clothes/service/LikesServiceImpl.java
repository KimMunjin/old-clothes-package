package com.kosta.clothes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.clothes.bean.Likes;
import com.kosta.clothes.dao.LikesDAO;
import com.kosta.clothes.dao.SharingDAO;

@Service
public class LikesServiceImpl implements LikesService{

	@Autowired
	LikesDAO likesDAO;
	
	@Autowired
	SharingDAO sharingDAO;
	
	@Override
	public void registSlikes(Likes likes) throws Exception {
		likes.setLikescheck((long) 1);
		likesDAO.insertSlikes(likes);
	}

//	@Override
//	public Likes selectSLikes(Likes likes) throws Exception {
//		return likesDAO.selectSLikes(likes);
//	}

	@Override
	public void updateSlikes(Likes likes) throws Exception {
		if(likes.getLikescheck()==0) {
			likesDAO.upSlikescheck(likes);
		}else {
			likesDAO.downSlikescheck(likes);
		}
	}

	@Override
	public Long getSlikescheck(Likes likes) throws Exception {
		return likesDAO.getSlikescheck(likes);
	}
	

	@Override
	public void registIlikes(Likes likes) throws Exception {
		likes.setLikescheck((long) 1);
		likesDAO.insertIlikes(likes);
	}

	@Override
	public Long getIlikescheck(Likes likes) throws Exception {
		return likesDAO.getIlikescheck(likes);
	}

	@Override
	public void updateIlikes(Likes likes) throws Exception {
		if(likes.getLikescheck()==0) {
			likesDAO.upIlikescheck(likes);
		}else {
			likesDAO.downIlikescheck(likes);
		}		
	}
	
	//bno 값을 가져오려고
	@Override
	public List<Likes> getbno(Integer userno) throws Exception {
		return likesDAO.getbno(userno);
	}
//판매업체
	@Override
	public void registBlikes(Likes likes) throws Exception {
		likesDAO.insertBlikes(likes);
	}

	@Override
	public void upBusinessLikes(Likes likes) throws Exception {
		likesDAO.upBlikes(likes);
	}

	@Override
	public Long getBlikescheck(Likes likes) throws Exception {
		return likesDAO.getBlikescheck(likes);
	}

	@Override
	public void updateBlikes(Likes likes) throws Exception {
		if(likes.getLikescheck()==0) {
			likesDAO.upBlikes(likes);
		}else {
			likesDAO.downBlikes(likes);
		}
		
	}

	@Override
	public Long getLikescheck(Likes likes) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
