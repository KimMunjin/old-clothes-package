package com.kosta.clothes.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.service.UsersService;

public class AuthLoginInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private UsersService usersService; //interceptor는 spring container안에 존재하므로 autowired를 할 수 있다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		Users uauthUser=null;
		Business bauthUser = null;
		String url = "/";
    	String reurl =(String)request.getSession().getAttribute("redirectURI");
    	
    	String id = request.getParameter("id");
    	String password = request.getParameter("password");
    	
    	if("null".equals(id)||"".equals(id)||"null".equals(password)||"".equals(password)) {
    		response.sendRedirect("/loginfail");
			return false;
    	}
    	
    	boolean ucheck = usersService.checkuserid(id);
    	boolean bcheck = usersService.businessidCheck(id);
    	if(ucheck) {
    		String userid = id;
    		uauthUser = usersService.login(userid, password);
    	}else if(bcheck){
    		String businessid = id;
			String bpassword = password;
    		bauthUser = usersService.blogin(businessid,bpassword);
    	}else {
    		response.sendRedirect("/loginfail");
			return false;
    	}
		
    	HttpSession session = request.getSession();
    	if(uauthUser == null && bauthUser==null) { //개인 업체 둘 다 아닐 경우
			response.sendRedirect("/loginfail");
			return false;
		}else if(uauthUser != null){
			session.setAttribute("authUser", uauthUser);
		}else {
		session.setAttribute("authUser", bauthUser);
		}
    	
    	if(reurl==null) {
    		response.sendRedirect(url);
    	}else if(reurl.contains("joinform")) {
    		response.sendRedirect(url);
    	}else if(reurl.contains("login")) {
    		response.sendRedirect(url);
    	}else if(reurl.contains("searchid")) {
    		response.sendRedirect(url);
    	}else if(reurl.contains("checkidnphone")) {
    		response.sendRedirect(url);
    	}else if(reurl.contains("mypage")) {
    		response.sendRedirect(url);
    	}else if(reurl.contains("passcheck")) {
    		response.sendRedirect(url);
    	}else {
    		response.sendRedirect(reurl);
    	}
    	return false;
	}

}
