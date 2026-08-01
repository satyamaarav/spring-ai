package com.spring.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;

public class DynamicTemplates {

    private final ChatClient chatClient;

    public DynamicTemplates(@Qualifier("ollamaAiChatClient")ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Create dynamic prompt template using param
     * @param query
     * @return
     */
    public String dynamicChatClient(String query) {
        String queryStr = "Act as an expert in coding and programming. Always write code in Java. Answer the following question:{query}";
        Prompt prompt = new Prompt(query);

        var response = chatClient
                .prompt()
                .user(u -> u.text(queryStr).param("query", queryStr))
                .call()
                .content();

        return response;
    }
}
