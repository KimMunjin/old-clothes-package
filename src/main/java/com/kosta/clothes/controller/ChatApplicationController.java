package com.kosta.clothes.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kosta.clothes.bean.ChatList;
import com.kosta.clothes.bean.ChatRoom;
import com.kosta.clothes.bean.TimeUtils;
import com.kosta.clothes.bean.Users;
import com.kosta.clothes.service.ChatRoomService;


@Controller
public class ChatApplicationController {

	@Autowired
	private SimpMessagingTemplate simpMessage;
	
	@Autowired
	private ChatRoomService chatRoomService;

	//채팅으로 거래하기(productInfo 화면)
	@RequestMapping(value="/chatMessage", method=RequestMethod.GET)
	public String getWebSocketWithSockJs(Model model, HttpSession session, 
			@ModelAttribute("chatRoom") ChatRoom chatRoom) throws IOException {
		
		//productInfo화면에서 Chat화면에 전달해줄 parameter
		
		Users login = (Users) session.getAttribute("authUser");
		Integer buyerno = login.getUserno();
		String buyerName = login.getNickname();
		chatRoom.setBuyerno(buyerno);
		chatRoom.setBuyerName(buyerName);
		
		
		//이미 chatRoom이 만들어져있는지 확인
		if (chatRoomService.countByChatId(chatRoom.getIno(), chatRoom.getBuyerno()) > 0) {
			//get ChatRoomInfo
			ChatRoom chatRoomTemp = chatRoomService.findByChatId(chatRoom.getIno(), chatRoom.getBuyerno());
			//load existing chat history
			List<ChatRoom> chatHistory = chatRoomService.readChatHistory(chatRoomTemp);
			//transfer chatHistory Model to View
			model.addAttribute("chatHistory", chatHistory);
			
		} else {
			//chatRoom 생성			
			chatRoomService.addChatRoom(chatRoom);			
			//text file 생성
			chatRoomService.createFile(chatRoom.getIno(), chatRoomService.getId(chatRoom.getIno(), chatRoom.getBuyerno()));								
		}

			//chatRoom Add 시 생성될 chatId
			chatRoom.setChatno(chatRoomService.getId(chatRoom.getIno(), chatRoom.getBuyerno()));
			
			//chatRoom 객체 Model에 저장해 view로 전달
			model.addAttribute("chatRoomInfo", chatRoom);
		
		return "chatroom/chatBroadcastProduct";
	}
	
	@MessageMapping("/broadcast")
	public void send(ChatRoom chatRoom) throws IOException {
		chatRoom.setSendTime(TimeUtils.getCurrentTimeStamp());
		//append message to txtFile
		chatRoomService.appendMessage(chatRoom);
		
		Integer chatno = chatRoom.getChatno();
		String url = "/user/" + chatno + "/queue/messages";
		simpMessage.convertAndSend(url, new ChatRoom(chatRoom.getContent(), chatRoom.getSenderName(), chatRoom.getSendTime(), chatRoom.getSenderId())); 
	}
	
	@ResponseBody
	@RequestMapping("/chatread/chatroom/ajax")
	public void ajaxChatRoomRead(@RequestBody String json) throws IOException {
		JSONObject jsn = new JSONObject(json);
		String idStr = (String) jsn.get("chatno");
		int chatno = Integer.parseInt(idStr);
		String flag = (String) jsn.get("flag");
		if (flag.equals("sell")) {
			chatRoomService.updateChatReadSell(chatno, 1);
		} else {
			chatRoomService.updateChatReadBuy(chatno, 1);
		}
		
		//		if (!readerId.equals(buyerId)) {
//			chatRoomService.updateChatReadSell(id, 1);
//		} else {
//			chatRoomService.updateChatReadBuy(id, 1);
//		}
	}
	
	@ResponseBody
	@RequestMapping("/chatread/product/ajax")
	public void ajaxChatProductRead(@RequestBody String json) throws IOException {
		JSONObject jsn = new JSONObject(json);
		String idStr = (String) jsn.get("chatno");
		int chatno = Integer.parseInt(idStr);
		chatRoomService.updateChatReadBuy(chatno, 1);
		
	}
	
	@RequestMapping(value="/chatList", method=RequestMethod.GET)
	public String getChatList(Model model, HttpSession session) {
		 return "chatroom/chatList";
	}
	
	@RequestMapping(value="/chatRoom/{ino}/{buyerno}", method=RequestMethod.GET)
	public String getChatRoom(@PathVariable Map<String, Integer> requestVar,
			Model model) throws IOException {
		Integer buyerno = Integer.parseInt(String.valueOf(requestVar.get("buyerno")));
		Integer ino = Integer.parseInt(String.valueOf(requestVar.get("ino")));
			
		//read chatHistory
		ChatRoom chatRoomRead = chatRoomService.findByChatId(ino, buyerno);
		List<ChatRoom> chatHistory = chatRoomService.readChatHistory(chatRoomRead);
		model.addAttribute("chatHistory", chatHistory);
		
		Integer chatno = chatRoomService.getId(ino, buyerno);
		String ititle = chatRoomRead.getItitle();
		Integer sellerno = chatRoomRead.getSellerno();
		String buyername = chatRoomRead.getBuyerName();
		String sellername = chatRoomRead.getSellerName();
		model.addAttribute("chatno", chatno);
		model.addAttribute("ino", ino);
		model.addAttribute("buyerno", buyerno);
		model.addAttribute("sellerno", sellerno);
		model.addAttribute("ititle", ititle);
		model.addAttribute("buyername", buyername);
		model.addAttribute("sellername", sellername);
		return "chatroom/chatBroadcastChatRoom";
	}
	
	
	@RequestMapping(value="/chatUnreadAlert/ajax", method=RequestMethod.POST)
	@ResponseBody
	public int chatUnread(@RequestBody String json) {
		
		JSONObject jsn = new JSONObject(json);
		Integer chatuserno = Integer.parseInt((String) jsn.get("chatuserno"));
    	int messages = chatRoomService.getUnreadMessages(chatuserno);

		return messages;
	}

	@RequestMapping(value="/chatUnreadMessageInfo/ajax", method=RequestMethod.POST)
	@ResponseBody
	public String chatListUnread(@RequestBody String json) {
		//ajax가 전송한 String을 key, value로 분류하기 위해 JSON Object convert
		JSONObject jsn = new JSONObject(json);
		//JSON.get([mapped name])으로 value 추출하기
		Integer chatuserno = Integer.parseInt((String)jsn.get("chatuserno"));
		//email에 해당되는 모든 chatRoom select 받기
		List<ChatList> chatRoomList = chatRoomService.findByEmail(chatuserno);
		//chatRoom 정보는 JSON Array에 저장됨
		JSONArray ja = new JSONArray();
		//email에 해당되는 읽지 않은 chatRoom select 받기
		List<Integer> unreadChatId = chatRoomService.getUnreadChatRoom(chatuserno);

		 for (ChatList chatList : chatRoomList) {
			//chatRoom 정보를 JSON Object에 put 해줌, chatRoom이 반복문에서 넘어갈 때마다 객체 초기화 
			 JSONObject jo = new JSONObject();
			 jo.put("ino", chatList.getIno());
			 jo.put("buyerno", chatList.getBuyerno());
		 	//리스트에 출력할 상대방 닉네임 확인
		 if (chatList.getBuyerno()==chatuserno) {
			 jo.put("senderName", chatList.getSellerName());
		 } else if(chatList.getSellerno()==chatuserno){
			 jo.put("senderName", chatList.getBuyerName());
		 }
		 
		 	 jo.put("ititle", chatList.getItitle());
		 //읽지 않은 chatRoom이 0개일때
		 if (unreadChatId.size() == 0) {
			 jo.put("messageUnread", "");
		 	} else {
		 		//읽지 않은 chatRoomId들과 현재 chatRoomId 대조 후 처리 
				 for (int ele : unreadChatId) {
					 	if (chatList.getChatno() == ele) {
					 		jo.put("messageUnread", "<img id='alert' src='/static/image/newmessage.png'>");
					 		break;
					 	} else {
					 		jo.put("messageUnread", "");
					 	}
				 }
			}
		 	ja.put(jo);
		}
		 //Javascript에 parsing 할 수 있도록 JSON Object에 Array를 담아줌
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", ja);
		 //String으로 변환해주면 끝, 프런트<->백엔드 전달은 String으로 이루어지며 형식은 JSON을 선택했음 
		 String result = jsnResult.toString();
		 //View로 result를 return해줌
		 return result;
	}
	
	
	@RequestMapping(value="/chatList/ajax", method=RequestMethod.POST)
	@ResponseBody
	public String chatList(@RequestBody String json) {
		
		JSONObject jsn = new JSONObject(json);
		Integer chatuserno = Integer.parseInt((String) jsn.get("chatuserno"));
		List<ChatList> chatRoomList = chatRoomService.findByEmail(chatuserno);		 
		JSONArray ja = new JSONArray();

		 for (ChatList chatList : chatRoomList) {
			
			 JSONObject jo = new JSONObject();
			 ja.put(jo);
		}
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", ja);
		 String result = jsnResult.toString();
		
		 return result;
	}
	
	
}
