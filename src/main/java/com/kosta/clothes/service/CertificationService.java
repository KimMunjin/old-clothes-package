package com.kosta.clothes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.dao.UsersDAO;
import com.kosta.clothes.security.EncryptHelper;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
@Service
public class CertificationService implements UsersService {
	@Autowired
	EncryptHelper encryptHelper;
	@Autowired
	UsersDAO usersDao;

	//번호 3개 이상 체크
	public boolean countPN(String phone) throws Exception{
		Integer uIdCount = usersDao.countUserIdbyPN(phone); //개인 아이디 수
		Integer bIdCount = usersDao.countBusinessIdbyPN(phone); //업체 아이디 수
		if((uIdCount+bIdCount) >= 3) {
			return true;//같은 번호로 가입한 아이디 3개 이상이면 true
		}else {
			return false;
		}
	}
	
	
	// 인증번호(전화번호, 인증번호)
    public void certifiedPhoneNumber(String phone, String cerNum) {
        String api_key = "NCSCEHWVJIO1GW6M"; //coolsms에서 발급받은 api_key
        String api_secret = "R7FQWGT4EPXFS1N4BLHXAGILZWZBFNEG";//coolsms에서 발급받은 api_secret key
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<>();
        params.put("to", phone);    // 수신전화번호 문자인증받을 번호
        params.put("from", "폰번호넣으세요");    // 발신전화번호 테스트 시에는 본인 번호 다 넣기 수신/발신  
        params.put("type", "SMS"); //문자형식
        params.put("text", "하우헌옷의 인증번호 : " + "["+cerNum+"]" + "입니다."); //문자 받을 때 문구 넣 을 곳 
        params.put("app_version", "test app 1.2"); // application name and version
        try {
            JSONObject obj = coolsms.send(params);  // 문자 보내기 JSONObject = 데이터 전달 위한 타입으로 정의
        } catch (CoolsmsException e) {  // 문자전송 실패 시 메세지
        }
    }
    
    //닉네임 중복 체크[개인]
    public boolean checkId(String checknick) throws Exception{
    	Users users = usersDao.selectName(checknick); //닉네임에 대한 정보를 찾아와 담는다ㅏ.
    	if(users == null) {//정보가 null라면 사용가능
    		return false;
    	}
    	return true;
    }
    
    //상호명 중복 체크[업체]
    @Override
	public boolean checkBname(String checkname) throws Exception {
    	Business business = usersDao.selectBname(checkname);//닉네임에 대한 정보를 찾아와 담는다ㅏ.
    	if(business == null) {//정보가 null라면 사용가능
    		return false;
    	}
		return true;
	}
    
    //아이디 중복 체크[개인]
	@Override
	public boolean checkuserid(String checkuserid) throws Exception {
		Users users = usersDao.selectuserid(checkuserid);//아이디에 대한 정보를 찾아와 담는다ㅏ.
    	if(users == null) {//정보가 null라면 사용가능
    		return false;
    	}
    	return true;
	}

	
	 //아이디 중복 체크[업체]
		@Override
		public boolean businessidCheck(String businessidCheck) throws Exception {
			Business business = usersDao.selectbusinessid(businessidCheck);//아이디에 대한 정보를 찾아와 담는다ㅏ.
	    	if(business == null) {//정보가 null이라면 사용가능
	    		return false;
	    	}
	    	return true;
		}
	
	//회원가입[개인]
	@Override
	public void insertUsers(Users users) throws Exception {
		String password = users.getPassword();
		String encryptedPw=encryptHelper.encrypt(password);
		users.setPassword(encryptedPw);
		usersDao.insertUsers(users);
	}

	//회원가입[업체]
	@Override
	public void insertBusiness(Business business) throws Exception {
		String password = business.getBpassword();
		String encryptedPw=encryptHelper.encrypt(password);
		business.setBpassword(encryptedPw);
		usersDao.insertBusiness(business);
	}

	//로그인[개인]
		@Override
		public Users login(String userid, String password) throws Exception {
			Users uauthUser= null;
			String hashpw = usersDao.checkupass(userid);
			boolean passcheck = encryptHelper.isMatch(password, hashpw);
			if(passcheck) {
				return usersDao.selectAccount(userid);
			}
//			Map<String,String> map = new HashMap<String, String>(); //아이디와 패스워드를 map으로 담았음
//			map.put("userid", userid);
//			map.put("password", password);
			return uauthUser;
		}
		//로그인[업체]
		@Override
		public Business blogin(String businessid, String bpassword) throws Exception {
			Business bauthUser = null;
			String hashpw = usersDao.checkbpass(businessid);
			boolean passcheck = encryptHelper.isMatch(bpassword, hashpw);
			if(passcheck) {
				return usersDao.selectbAccount(businessid);
			}
//			Map<String,String> map = new HashMap<String, String>(); //아이디와 패스워드를 map으로 담았음
//			map.put("businessid", businessid);
//			map.put("bpassword", bpassword);
			return bauthUser;
		}

		@Override
		public List<String> findUserId(String phone) throws Exception {
			return usersDao.findUserId(phone); //개인 아이디 리스트 조회
		}

		@Override
		public List<String> findBusinessId(String bphone) throws Exception {
			return usersDao.findBusinessId(bphone); // 업체 아이디 리스트 조회
		}

		@Override
		public String checkUserIdnPhone(String userid, String phone) throws Exception {
			Map<String,String> map = new HashMap<String, String>();
			map.put("userid", userid);
			map.put("phone", phone);
			return usersDao.checkUserIdnPhone(map); //아이디와 전화번호로 개인 체크
		}

		@Override
		public String checkBusinessIdnPhone(String businessid, String bphone) throws Exception {
			Map<String,String> map = new HashMap<String, String>();
			map.put("businessid", businessid);
			map.put("bphone", bphone);
			return usersDao.checkBusinessIdnPhone(map); //아이디와 전화번호로 업체 체크
		}
		//비밀번호 변경
		@Override
		public void changePass(String userid, String password) throws Exception {
			String encryptedPw=encryptHelper.encrypt(password);
			Map<String,String> map = new HashMap<String, String>(); //아이디와 패스워드를 map으로 담았음
			map.put("userid", userid);
			map.put("password", encryptedPw);
			usersDao.updatepassword(map); //개인비밀번호 변경
		}
		//비밀번호 변경
		@Override
		public void changebPass(String businessid, String bpassword) throws Exception {
			String encryptedPw=encryptHelper.encrypt(bpassword);
			Map<String,String> map = new HashMap<String, String>(); //아이디와 패스워드를 map으로 담았음
			map.put("businessid", businessid);
			map.put("bpassword", encryptedPw);
			usersDao.updatebpassword(map); //업체비밀번호 변경
		}

		//개인비밀번호 확인용
		@Override
		public boolean checkupass(String id, String pass) throws Exception {
			String hashpw = usersDao.checkupass(id);
			boolean passcheck = encryptHelper.isMatch(pass, hashpw);
			return passcheck;
		}

		//개인회원정보수정
		@Override
		public void modifyuser(Users user) throws Exception {
			String password = user.getPassword();
			String encryptedPw=encryptHelper.encrypt(password);
			user.setPassword(encryptedPw);
			usersDao.updateuser(user);
			
		}	

		//업체비밀번호 확인용
		@Override
		public boolean checkbpass(String id, String pass) throws Exception {
			String hashpw = usersDao.checkbpass(id);
			boolean passcheck = encryptHelper.isMatch(pass, hashpw);
			return passcheck;
		}

		//업체회원정보수정
		@Override
		public void modifybusiness(Business business) throws Exception {
			String password = business.getBpassword();
			String encryptedPw=encryptHelper.encrypt(password);
			business.setBpassword(encryptedPw);
			usersDao.updatebusiness(business);
			
		}

		
		//개인회원탈퇴
		@Transactional
		@Override
		public void deleteuser(Integer userno) throws Exception {
			usersDao.deletecomment(userno);
			usersDao.deletechatroom(userno);
			usersDao.deletelikes(userno);
			usersDao.deletereview(userno);
			usersDao.deletemessage(userno);
			usersDao.deletewapply(userno);
			usersDao.deleteapply(userno);
			usersDao.deletefree(userno);
			usersDao.deleteindivi(userno);	
			usersDao.deletesharing(userno);
			usersDao.deleteuser(userno);
		}


		//업체회원탈퇴
		@Transactional
		@Override
		public void deletebusiness(Integer bno) throws Exception {
			usersDao.deleteblikes(bno);
			usersDao.deletebcomments(bno);
			usersDao.deletebreview(bno);
			usersDao.deletebapply(bno);
			usersDao.deletebfree(bno);
			usersDao.deletebusiness(bno);
			
		}

		//넘버로 개인 모든 정보 가져오기
		@Override
		public Users selectuAll(Integer userno) throws Exception {
			return usersDao.selectuAll(userno);
		}

		//넘버로 업체 모든 정보 가져오기
		@Override
		public Business selectbAll(Integer bno) throws Exception {
			return usersDao.selectbAll(bno);
		}


}
