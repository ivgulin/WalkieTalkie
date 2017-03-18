package com.mokujin.controller;

import com.mokujin.domain.ChatMessage;
import com.mokujin.domain.ChatMessageModel;
import com.mokujin.repository.ChatMessageRepository;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatMessageController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ProfileService profileService;

    @RequestMapping
    public String chat(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("profile", profileService.findByUsername(authentication.getName()));
        return "chat";
    }


    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    @MessageMapping("/newMessage")
    @SendTo("/topic/newMessage")
    public ChatMessage save(ChatMessageModel chatMessageModel) {
        ChatMessageModel chatMessage = new ChatMessageModel(chatMessageModel.getText(), chatMessageModel.getAuthor(), new Date());
        ChatMessageModel message = chatMessageRepository.save(chatMessage);
        List<ChatMessageModel> chatMessageModelList = chatMessageRepository.findAll(new PageRequest(0, 5, Sort.Direction.DESC, "createDate")).getContent();
        return new ChatMessage(chatMessageModelList.toString());
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public HttpEntity list() {
        List<ChatMessageModel> chatMessageModelList = chatMessageRepository.findAll(new PageRequest(0, 5, Sort.Direction.DESC, "createDate")).getContent();
        return new ResponseEntity(chatMessageModelList, HttpStatus.OK);
    }
}
