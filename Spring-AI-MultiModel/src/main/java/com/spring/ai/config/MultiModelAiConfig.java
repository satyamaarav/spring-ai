package com.spring.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiModelAiConfig {

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    @Bean(name = "ollamaAiChatClient")
    public ChatClient ollamaAiChatClient(OllamaChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    /*
     * Bean specifically for OpenAI ChatClient with default options set to use GPT-4o-mini model.
     * This allows for easy injection of a pre-configured ChatClient for OpenAI in other parts of the application.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClient) {
        return chatClient
                .defaultOptions((ChatOptions.Builder) OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.7)
                        .maxTokens(1000)
                        .build())
                .build();
    }
}
