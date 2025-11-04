package com.example.OpenAiLearning.config;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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
        InMemoryChatMemoryRepository memoryRepository = new InMemoryChatMemoryRepository();

        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(memoryRepository)
                .maxMessages(20)
                .build();

        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(memory)
                .build();

        return ChatClient.builder(openAI)
                .defaultAdvisors(memoryAdvisor)
                .build();
    }
}

