package com.example.OpenAiLearning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("ai/image")
public class ImageGenController {

    private final OpenAiImageModel openAiImageModel;
    private final ChatClient chatClient;

    public ImageGenController(OpenAiImageModel openAiImageModel, ChatClient chatClient) {
        this.openAiImageModel = openAiImageModel;
        this.chatClient = chatClient;
    }

    @PostMapping("/genImage")
    public String genImage(@RequestParam("query") String query){
        ImagePrompt imagePrompt=new ImagePrompt(query, OpenAiImageOptions
                .builder()
                .quality("HD")
                .height(1024)
                .width(1024)
                .style("natural")
                .build());
        ImageResponse response=openAiImageModel.call(imagePrompt);

        return response.getResult().getOutput().getUrl();
    }


    @PostMapping("/describe-image")
    public String describeImage(@RequestParam String query, @RequestBody MultipartFile file){
        return chatClient.prompt().user(us->us.text(query)
                .media(MimeTypeUtils.IMAGE_JPEG,file.getResource()))
                .call()
                .content();
    }
}
