package com.spring.ai.controller;

import com.spring.ai.entity.Tut;
import com.spring.ai.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatClientController {

    private ChatService chatService;

    public ChatClientController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public ResponseEntity<String> chat(@RequestParam(value="q") String query) {
        return ResponseEntity.ok(chatService.chat(query));
    }

    @GetMapping("/tut")
    public ResponseEntity<Tut> getTutResponse(@RequestParam(value="q") String query) {
        return ResponseEntity.ok(chatService.getTut(query));
    }

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tut>> getTutResponses(@RequestParam(value="q") String query) {
        return ResponseEntity.ok(chatService.getTutResponses(query));
    }
}
