package com.spring.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MultiModelService {

    private final ChatClient openAIChatClient;

    private final ChatClient ollamaAiChatClient;

    public MultiModelService(@Qualifier("openAiChatClient") ChatClient openAiChatClient, @Qualifier("ollamaAiChatClient") ChatClient ollamaAiChatClient) {
        this.openAIChatClient = openAiChatClient;
        this.ollamaAiChatClient = ollamaAiChatClient;
    }

    /**
     *
     * @param openAiChatModel
     * @param ollamaAiChatModel
     *

    public MultiModelService(OpenAiChatModel openAiChatModel, OllamaChatModel ollamaAiChatModel) {
        this.openAIChatClient = ChatClient.builder(openAiChatModel).build();
        this.ollamaAiChatClient = ChatClient.builder(ollamaAiChatModel).build();
    }

    */

    public String getChatResponse(String model, String prompt) {
        if ("openai".equalsIgnoreCase(model)) {
            return openAIChatClient.prompt(prompt).call().content();
        } else if ("ollama".equalsIgnoreCase(model)) {
            return ollamaAiChatClient.prompt(prompt).call().content();
        } else {
            throw new IllegalArgumentException("Unsupported model: " + model);
        }
    }

}
