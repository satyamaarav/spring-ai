package com.spring.ai.openai.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAiChatService {

   private final ChatClient chatClient;

    public OpenAiChatService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String getChatResponse(String query) {
        // Implementation for getting chat response from OpenAI
        return chatClient.prompt(query).call().content();
    }
}
