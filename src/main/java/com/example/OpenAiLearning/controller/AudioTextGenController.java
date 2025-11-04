package com.example.OpenAiLearning.controller;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.tts.TextToSpeechOptions;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class AudioTextGenController {


    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AudioTextGenController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    @PostMapping("/audio- to-text")
    public String generateAudioToText(@RequestBody MultipartFile file){

        OpenAiAudioTranscriptionOptions options=OpenAiAudioTranscriptionOptions.builder()
                .language("es")
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT)
                .build();

        AudioTranscriptionPrompt prompt=new AudioTranscriptionPrompt(file.getResource(),options);//it helps to provide timestamp

        return openAiAudioTranscriptionModel.call(prompt).getResult().getOutput();
    }


    @PostMapping("/text-to-audio")
    public byte[] generateTextToAudio(@RequestParam String query){

        OpenAiAudioSpeechOptions options=OpenAiAudioSpeechOptions.builder()
                .speed(1.5f)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.SHIMMER)//or .voice("nova)
                .build();

        SpeechPrompt prompt=new SpeechPrompt(query,options);

        return openAiAudioSpeechModel.call(prompt).getResult().getOutput();
    }



}
