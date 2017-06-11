package com.mokujin.controller;

import com.mokujin.domain.ChatMessage;
import com.mokujin.domain.ChatMessageModel;
import com.mokujin.domain.Profile;
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
public class ChatController {

    private String username;

    private String friendName;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String chat(@RequestParam("username") String friendName, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.username = authentication.getName();
        this.friendName = friendName;
        Profile profile = profileService.findByUsername(username);
        model.addAttribute("profile", profile);
        model.addAttribute("friend", profileService.findByUsername(friendName));
        model.addAttribute("friends", profile.getFriends());
        return "chat";
    }

    @PostMapping("/messages")
    @MessageMapping("/newMessage")
    @SendTo("/topic/newMessage")
    public ChatMessage save(ChatMessageModel chatMessageModel) {
        ChatMessageModel chatMessage = new ChatMessageModel
                (chatMessageModel.getText(), username, new Date(), friendName);
        chatMessageRepository.save(chatMessage);
        List<ChatMessageModel> chatMessageModelList =
                chatMessageRepository.getDialogue(username, friendName,
                        new PageRequest(0, 5, Sort.Direction.DESC, "message.createDate"))
                        .getContent();
        return new ChatMessage(chatMessageModelList.toString());
    }

    @GetMapping("/messages")
    @CrossOrigin()
    public HttpEntity<List<ChatMessageModel>> list() {
        List<ChatMessageModel> chatMessageModelList = chatMessageRepository.getDialogue(username,
                friendName, new PageRequest(0, 5, Sort.Direction.DESC, "message.createDate"))
                .getContent();
        return new ResponseEntity<>(chatMessageModelList, HttpStatus.OK);
    }
}
