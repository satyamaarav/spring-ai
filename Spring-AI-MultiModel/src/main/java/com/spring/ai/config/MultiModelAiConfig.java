package com.spring.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiModelAiConfig {

    @Bean(name="openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    @Bean(name="ollamaAiChatClient")
    public ChatClient ollamaAiChatClient(OllamaChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
