package com.example.OpenAiLearning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class ChatController {

    public final ChatClient chatClient; // from OpenAiConfig
    private final OpenAiEmbeddingModel openAiEmbeddingModel;
    private final VectorStore vectorStore;
    public ChatController(ChatClient chatClient, OpenAiEmbeddingModel openAiEmbeddingModel, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.openAiEmbeddingModel = openAiEmbeddingModel;
        this.vectorStore = vectorStore;
    }

    //OR
//    private final ChatClient chatClient;
//    public ChatController(OpenAiChatModel chatModel) {
//        this.chatClient = ChatClient.create(chatModel);
//    }

    @GetMapping("/chat")
    public String generateChat(@RequestParam("message") String message){
        return chatClient.prompt().user(message).call().content();
    }

    //RAG IMPLEMENTATION
    @PostMapping("/ask")
    public String generateWithRag(@RequestParam("query") String query){
        return chatClient.prompt(query).advisors(new QuestionAnswerAdvisor(vectorStore)).call().content();
    }

    @PostMapping("/reccomend/movie")
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


    @PostMapping("/reccomend/movie/list")
    public List<String> generateMovieInList(@RequestParam("type")String name,@RequestParam("year")String year,@RequestParam("language")String language){
        String template= """
                name five top movies of {name}
                """;
        ListOutputConverter outputConverter=new ListOutputConverter(new DefaultConversionService());
        String format=outputConverter.getFormat();
        PromptTemplate promptTemplate=new PromptTemplate(template);
        Prompt prompt=promptTemplate.create(Map.of("name",name,"format",format));
        return outputConverter.convert(chatClient.prompt().call().content());
    }

}
