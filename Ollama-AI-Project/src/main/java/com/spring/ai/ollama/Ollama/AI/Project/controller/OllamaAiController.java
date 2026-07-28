package com.spring.ai.ollama.Ollama.AI.Project.controller;

import com.spring.ai.ollama.Ollama.AI.Project.service.OllamaAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ollama")
public class OllamaAiController {

    public final OllamaAiService ollamaAiService;

    public OllamaAiController(OllamaAiService ollamaAiService) {
        this.ollamaAiService = ollamaAiService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> getChatResponse(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(ollamaAiService.getChatClient(query));
    }
}
