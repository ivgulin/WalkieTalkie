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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatMessageController {

    private String username;

    private String friendName;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ProfileService profileService;

    @RequestMapping
    public String chat( Model model){
        model.addAttribute("profile", profileService.findByUsername(username));
        model.addAttribute("friend", profileService.findByUsername(friendName));
        return "chat";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String chat(@RequestParam("username")String friendName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.username = authentication.getName();
        this.friendName = friendName;
        return "redirect:/chat";
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    @MessageMapping("/newMessage")
    @SendTo("/topic/newMessage")
    public ChatMessage save(ChatMessageModel chatMessageModel) {
        ChatMessageModel chatMessage = new ChatMessageModel
                (chatMessageModel.getText(), username, new Date(), friendName);
        chatMessageRepository.save(chatMessage);
        List<ChatMessageModel> chatMessageModelList = chatMessageRepository.getDialogue(chatMessage.getAuthor(),chatMessage.getReceiver());
        return new ChatMessage(chatMessageModelList.toString());
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public HttpEntity list() {
        List<ChatMessageModel> chatMessageModelList = chatMessageRepository.getDialogue(username,friendName);
        return new ResponseEntity(chatMessageModelList, HttpStatus.OK);
    }
}
