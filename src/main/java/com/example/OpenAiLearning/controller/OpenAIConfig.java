package com.example.OpenAiLearning.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;

@Configuration
public class OpenAIConfig {

    private final OpenAiChatModel openAI;

    public OpenAIConfig(OpenAiChatModel openAI) {
        this.openAI = openAI;
    }

    @Bean
    public ChatClient chatClient() {
        return ChatClient.create(openAI);
    }
}

