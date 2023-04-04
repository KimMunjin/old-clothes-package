package com.kosta.clothes.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {
	
	@Autowired
	ServletContext servletContext;
	
	@GetMapping("/")
	String main(HttpServletRequest request) {
		Set<String> pathSet = request.getSession().getServletContext().getResourcePaths("/");
		return "/home2";
	}
	//이미지 경로(서버에서 가져오기) filename 경로를 받아온다.
	//Main에 이미지 보여주기위한 컨트롤러
	@GetMapping("/image/{filename}")
	public void viewImage(@PathVariable String filename, HttpServletResponse response) {
		String path = servletContext.getRealPath("/image/");//경로 얻어오기
		FileInputStream fis = null;//finally에서 닫아주기 위해 여기에 정의
		try {
			fis = new FileInputStream(path+filename);//file에 담아준다 경로+파일명
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(fis, out);//fis에 out값을 복사한다.
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch(Exception e) {}
			}
		}
	}
}
