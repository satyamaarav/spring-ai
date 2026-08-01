package com.spring.ai.ollama.Ollama.AI.Project.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaAiService {

    /**
     * What is ChatClient?
     * ChatClient is a high-level Fluent API introduced in Spring AI.
     * Instead of manually creating Prompt and ChatResponse objects,
     * it lets you interact with the AI using a fluent builder pattern.
     *
     * Application
     *      │
     *      ▼
     * ChatClient
     *      │
     *      ▼
     * ChatModel
     *      │
     *      ▼
     * OpenAI/Ollama
     *
     * Notice that ChatClient internally calls ChatModel.
     *
     * Why ChatClient?
     * Spring team observed that developers repeatedly wrote:
     *
     * Create Prompt
     * ↓
     * Call ChatModel
     * ↓
     * Extract Response
     * ↓
     * Return String
     * To reduce boilerplate, they created ChatClient.
     */
    private final ChatClient chatClient;
    public OllamaAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getChatClient(String query) {
        return chatClient.prompt(query).call().content();
    }
}
