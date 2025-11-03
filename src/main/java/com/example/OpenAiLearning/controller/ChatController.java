package com.example.OpenAiLearning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("ai")
public class ChatController {

    private final ChatClient chatClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private OpenAiEmbeddingModel openAiEmbeddingModel;

    public ChatController(OpenAiChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("/chat")
    public String generateChat(@RequestParam("message") String message){
        return chatClient.prompt().user(message).call().content();
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

    @PostMapping("/embeddings")
    public float[] createEmbedding(@RequestParam("text")String text){
        return openAiEmbeddingModel.embed(text);
    }

    @PostMapping("/similarities")
    public double getSimilarities(@RequestParam("text1") String text1,@RequestParam("text2")String text2){
        float[] embeddings1=openAiEmbeddingModel.embed(text1);
        float[] embeddings2=openAiEmbeddingModel.embed(text2);

        double dotProduct=0;
        double norm1=0;
        double norm2=0;

        for (int i=0;i<embeddings1.length;i++){
            dotProduct+=embeddings1[i]*embeddings2[i];
            norm1+=Math.pow(embeddings1[i],2);
            norm2+=Math.pow(embeddings2[i],2);
        }

        return dotProduct/Math.sqrt(norm1)*Math.sqrt(norm2);

    }


}
