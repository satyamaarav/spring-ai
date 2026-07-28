package com.spring.ai.ollama.Ollama.AI.Project.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaAiService {

    private final ChatClient chatClient;
    public OllamaAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getChatClient(String query) {
        return chatClient.prompt(query).call().content();
    }
}
