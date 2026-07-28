package com.spring.ai.openai.controllers;

import com.spring.ai.openai.services.OpenAiChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
public class OpenAiChatController {

    private final OpenAiChatService openAiChatService;
    public OpenAiChatController(OpenAiChatService openAiChatService) {
        this.openAiChatService = openAiChatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> getChatResponse(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(openAiChatService.getChatResponse(query));
    }
}
