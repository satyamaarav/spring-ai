package com.spring.ai.service.impl;

import com.spring.ai.entity.Tut;
import com.spring.ai.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {


    private final ChatClient chatClient;

    public ChatServiceImpl(@Qualifier("ollamaAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String chat(String query) {
        // Let try to use prompt with user and system
        String systemPrompt = "As an expert in cricket.";

        /*
        return chatClient.prompt().user(query).system(systemPrompt).call().content();
        */

        /*
        Prompt prompt = new Prompt(query);
        return chatClient.prompt(prompt).call().content();
        */

        /*
        var metadata = chatClient.prompt(query).call().chatResponse().getMetadata();
        System.out.println(metadata);
        */

        var content = chatClient.prompt(query).call().chatResponse().getResult().getOutput().getText();
        return content;

    }

    @Override
    public Tut getTut(String query) {
        return chatClient
                .prompt(query)
                .call()
                .entity(Tut.class);
    }

    @Override
    public List<Tut> getTutResponses(String query) {
        return chatClient
                .prompt(query)
                .call()
                .entity(new ParameterizedTypeReference<List<Tut>>() {});
    }

    @Override
    public String promptSpecific(String query) {
        // Implementation for specific prompt handling
        Prompt prompt = new Prompt(query, OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.7)
                .maxTokens(100)
                .build());

        return chatClient.prompt(prompt).call().content();
    }

}
