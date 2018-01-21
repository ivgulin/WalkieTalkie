package com.mokujin.controller;

import com.mokujin.domain.ChatMessage;
import com.mokujin.domain.ChatMessageModel;
import com.mokujin.domain.Profile;
import com.mokujin.repository.ChatMessageRepository;
import com.mokujin.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ChatController {

    private String username;

    private String friendName;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String chat(@RequestParam("username") String friendName, Model model) {
        log.info("'chat() invoked with params - {}, {}'", friendName, model);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.username = authentication.getName();
        this.friendName = friendName;
        Profile profile = profileService.findByUsername(username);
        log.info("'profile={}'", profile);
        model.addAttribute("profile", profile);
        model.addAttribute("friend", profileService.findByUsername(friendName));
        model.addAttribute("friends", profile.getFriends());
        String redirect = "chat";
        log.info("'chat() returned model - {}, redirected to {}.jsp'", model, redirect);
        return redirect;
    }

    @PostMapping("/messages")
    @MessageMapping("/newMessage")
    @SendTo("/topic/newMessage")
    public ChatMessage save(ChatMessageModel chatMessageModel) {
        log.info("'chat() invoked with params - {}'", chatMessageModel);
        ChatMessageModel newChatMessageModel = new ChatMessageModel
                (chatMessageModel.getText(), username, new Date(), friendName);
        ChatMessageModel message = chatMessageRepository.save(newChatMessageModel);
        log.info("'chatMessageModel = {}'", message);
        List<ChatMessageModel> chatMessageModelList =
                chatMessageRepository.getDialogue(username, friendName,
                        new PageRequest(0, 5, Sort.Direction.DESC, "message.createDate"))
                        .getContent();
        ChatMessage chatMessage = new ChatMessage(chatMessageModelList.toString());
        log.info("'chat() returned chatMessage - {}'", chatMessage);
        return chatMessage;
    }

    @GetMapping("/messages")
    @CrossOrigin()
    public HttpEntity<List<ChatMessageModel>> list() {
        log.info("'list() invoked with no params '");
        List<ChatMessageModel> chatMessageModelList = chatMessageRepository.getDialogue(username,
                friendName, new PageRequest(0, 5, Sort.Direction.DESC, "message.createDate"))
                .getContent();
        ResponseEntity<List<ChatMessageModel>> listResponse = new ResponseEntity<>(chatMessageModelList, HttpStatus.OK);
        log.info("'list() returned list of chatMessages- {}'", listResponse);
        return listResponse;
    }
}
