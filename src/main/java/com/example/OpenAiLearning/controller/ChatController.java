package com.example.OpenAiLearning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class ChatController {

    public ChatClient chatClient;//from OpenAIConfig

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    //OR
//    private final ChatClient chatClient;
//    public ChatController(OpenAiChatModel chatModel) {
//        this.chatClient = ChatClient.create(chatModel);
//    }

    @Autowired
    private OpenAiEmbeddingModel openAiEmbeddingModel;


    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/chat")
    public String generateChat(@RequestParam("message") String message){
        return chatClient.prompt().user(message).call().content();
    }

    //RAG IMPLEMENTATION
    @PostMapping("/ask")
    public String generateWithRag(@RequestParam("query") String query){
        return chatClient.prompt(query).advisors(new QuestionAnswerAdvisor(vectorStore)).call().content();
    }

    @PostMapping("/reccomend-movie")
    public String generateMovie(@RequestParam("type")String type,@RequestParam("year")String year,@RequestParam("language")String language){
        String template= """
                I want to watch am movie of type {type} around the year {year} in the {lang} language
                I want response in the format of 
                1.Movie name
                2.Movie timing
                3.IDBM rating
                """;

        PromptTemplate promptTemplate=new PromptTemplate(template);
        Prompt prompt=promptTemplate.create(Map.of("type",type,"year",year,"lang",language));
        return chatClient.prompt(prompt).call().content();
    }




}
