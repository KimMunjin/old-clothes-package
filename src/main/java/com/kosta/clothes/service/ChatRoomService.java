package com.kosta.clothes.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.clothes.bean.ChatList;
import com.kosta.clothes.bean.ChatRoom;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.dao.ChatRoomDAO;


@Service
@Transactional
public class ChatRoomService implements ChatRoomDAO {
	
	@Autowired
	ChatRoomDAO chatRoomDAO;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	HttpSession session;

	@Override
	public void addChatRoom(ChatRoom chatRoom) throws IOException {
		
		Timestamp createdDate = Timestamp.valueOf(LocalDateTime.now());
        
        chatRoom.setCreatedDate(createdDate);
        chatRoomDAO.addChatRoom(chatRoom);
		
	}
	
	//no connection with DB
	public List<ChatRoom> readChatHistory(ChatRoom chatRoom) throws IOException {
		String fileUploadPath = "/home/ubuntu/app/oldclothes/chat/";
//		String fileUploadPath = servletContext.getRealPath("/chat/");
		String pathName = fileUploadPath + chatRoom.getFileName();
		
		//DB에 저장된 chat.txt 파일을 읽어옴 
		BufferedReader br = new BufferedReader(new FileReader(pathName));
		//View에 ChatRoom 객체로 전달
		ChatRoom chatRoomLines = new ChatRoom();
		List<ChatRoom> chatHistory = new ArrayList<ChatRoom>();

		String chatLine;
		int idx = 1;
		
		while ((chatLine = br.readLine()) != null) {
			
			//1개 메시지는 3줄(보낸사람,메시지내용,보낸시간)로 구성돼있음
			int answer = idx % 3;
			if (answer == 1) {
				//보낸사람
				chatRoomLines.setSenderName(chatLine);
				idx++;
			} else if (answer == 2) {
				//메시지내용
				chatRoomLines.setContent(chatLine);
				idx++;
			} else {
				//보낸시간
				chatRoomLines.setSendTime(chatLine);
				//메시지 담긴 ChatRoom 객체 List에 저장
				chatHistory.add(chatRoomLines);
				//객체 초기화, 줄(row)인덱스 초기화
				chatRoomLines = new ChatRoom();
				idx = 1;
			}			
		}
		
		return chatHistory;
	}
	
	@Override
	public void updateFileName(Integer chatno, String fileName) {
		chatRoomDAO.updateFileName(chatno, fileName);
	}
	
	public void createFile(Integer ino, Integer chatno) throws IOException {
		String fileUploadPath = "/home/ubuntu/app/oldclothes/chat/";
//		String fileUploadPath = servletContext.getRealPath("/chat/");

		String fileName = ino + "_" + chatno + ".txt";
		String pathName = fileUploadPath + fileName;
		//File 클래스에 pathName 할당
		File txtFile = new File(pathName);
		//로컬경로에 파일 생성
		txtFile.createNewFile();
		chatRoomDAO.updateFileName(chatno, fileName);
	}

	@Override
	public List<ChatList> findByEmail(Integer chatuserno) {
		return chatRoomDAO.findByEmail(chatuserno);
	}

	//이미 방이 만들어져있는지
	@Override
	public int countByChatId(Integer ino, Integer buyerno) {
		return chatRoomDAO.countByChatId(ino, buyerno);
	}


	//no connection with DB
	public void appendMessage(ChatRoom chatRoom) throws IOException {
		String fileUploadPath = "/home/ubuntu/app/oldclothes/chat/";
//		String fileUploadPath = servletContext.getRealPath("/chat/");
		Integer ino = chatRoom.getIno();
		Integer buyerno = chatRoom.getBuyerno();
		
		ChatRoom chatRoomAppend = chatRoomDAO.findByChatId(ino, buyerno);
				
		String pathName = fileUploadPath + chatRoomAppend.getFileName();
		
		FileOutputStream fos = new FileOutputStream(pathName, true);
		String content = chatRoom.getContent();
		String senderName = chatRoom.getSenderName();
		String senderId = chatRoom.getSenderId();
		String sendTime = chatRoom.getSendTime();
		
		String writeContent = senderName + "\n" + content + "\n" + "[" +  sendTime + "]" + "\n";
		
		byte[] b = writeContent.getBytes();
		
		fos.write(b);
		fos.close();
		
		if (senderName.equals(chatRoom.getSellerName())) {
			updateChatReadBuy(chatRoom.getChatno(), 0);
		} else {
			updateChatReadSell(chatRoom.getChatno(), 0);
		}
		
	}

	@Override
	public ChatRoom findByChatId(Integer ino, Integer buyerno) {
		return chatRoomDAO.findByChatId(ino, buyerno);
	}
	
	@Override
	public Integer getId(Integer ino, Integer buyerno) {
		return chatRoomDAO.getId(ino, buyerno);
	}

	@Override
	public void updateChatReadBuy(Integer chatno, int chatReadBuy) {
		chatRoomDAO.updateChatReadBuy(chatno, chatReadBuy);
		
	}

	@Override
	public void updateChatReadSell(Integer chatno, int chatReadSell) {
		chatRoomDAO.updateChatReadSell(chatno, chatReadSell);
		
	}
	
	@Override
	public int getUnreadMessages(Integer chatuserno) {
		return chatRoomDAO.getUnreadMessages(chatuserno);
	}

	@Override
	public List<Integer> getUnreadChatRoom(Integer chatuserno) {
		List<Integer> unread = chatRoomDAO.getUnreadChatRoom(chatuserno); 
		return unread;
	}

	@Override
	public void deleteChat(Integer sellerno, Integer ino) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(session.getAttribute("authUser")!=null) {
			if(session.getAttribute("authUser").getClass().getName().equals("com.kosta.clothes.bean.Users")) {
				Users users = (Users) session.getAttribute("authUser");
				sellerno = users.getUserno();
			}
		}
		map.put("sellerno", sellerno);
		map.put("ino", ino);
		chatRoomDAO.deleteChat(sellerno, ino);
	}


	

	
	

}
