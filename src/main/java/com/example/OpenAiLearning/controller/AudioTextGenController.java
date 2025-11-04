package com.example.OpenAiLearning.controller;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class AudioTextGenController {


    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    public AudioTextGenController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
    }

    @PostMapping("/audio- to-text")
    public String generateAudioToText(@RequestBody MultipartFile file){

        OpenAiAudioTranscriptionOptions options=OpenAiAudioTranscriptionOptions.builder().build();
        AudioTranscriptionPrompt prompt=new AudioTranscriptionPrompt(file.getResource());//it helps to provide timestamp
        return openAiAudioTranscriptionModel.call(prompt).getResult().getOutput();
    }



}
