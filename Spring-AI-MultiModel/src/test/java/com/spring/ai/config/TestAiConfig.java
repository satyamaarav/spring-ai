package com.spring.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestAiConfig {

    @Bean
    @Primary
    public ChatClient.Builder chatClientBuilder() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient mockChatClient = mock(ChatClient.class);
        org.mockito.Mockito.when(builder.build()).thenReturn(mockChatClient);
        return builder;
    }

    @Bean(name = "ollamaAiChatClient")
    @Primary
    public ChatClient ollamaAiChatClient() {
        return mock(ChatClient.class);
    }

    @Bean(name = "openAiChatClient")
    @Primary
    public ChatClient openAiChatClient() {
        return mock(ChatClient.class);
    }

    @Bean
    @Primary
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
