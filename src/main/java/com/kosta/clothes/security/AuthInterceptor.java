package com.kosta.clothes.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kosta.clothes.bean.Business;
import com.kosta.clothes.bean.Users;

public class AuthInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		
		//1. handler 종류 확인
				if(handler instanceof HandlerMethod == false) {
					return true; //image,js파일이 여기에 들어온다. assets에 없는 애들
				}
				
				//2.casting
				HandlerMethod handlerMethod = (HandlerMethod)handler;
				
				
				//3. Method의 @Auth 받아오기
				Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
				
				//4. Method에 @Auth가 없으면 (인증이 필요 없는 메서드)
				if(auth == null) {
					//4.  Class(Type)에 @Auth를 받아오기
					auth = handlerMethod.getBeanType().getAnnotation(Auth.class);
					if (auth == null) {
						return true;
					}
				}
				
				auth = handlerMethod.getMethodAnnotation(Auth.class);
				//5. Method에 @Auth가 없으면
				if(auth==null) {
					return true;
				}
				//6. @Auth가 (class 또는 method에)붙어 있기 때문에 인증 여부 체크
				HttpSession session= request.getSession();
				//getSession(),getSession(true)는 session이 없으면 session을 만든다.
				//getSession(false)는 session이 없으면 null 반환
				
				if(session.getAttribute("authUser") == null) {//리턴이 안되어 있음
					response.sendRedirect("/login");
					return false;
				}
				
				//7. 세션이 존재하면 유효한 유저인지 확인
				Users uauthUser = new Users();
				Business bauthUser = new Business();
				if(session.getAttribute("authUser")!=null) {
					if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")){
						uauthUser = (Users)session.getAttribute("authUser");
					}else {
						bauthUser = (Business)session.getAttribute("authUser");
					}
					if(uauthUser == null&&bauthUser == null) {//인증 x
						response.sendRedirect("/login");
						return false;
					}
				}
				
				
				return true;//최종 목적은 컨트롤러에게 가기
	}
	
}
