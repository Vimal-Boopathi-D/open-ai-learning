package com.example.OpenAiLearning.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("ai/embed")
public class EmbeddingsController {


    private final OpenAiEmbeddingModel openAiEmbeddingModel;
    private final VectorStore vectorStore;

    public EmbeddingsController(OpenAiEmbeddingModel openAiEmbeddingModel, VectorStore vectorStore) {
        this.openAiEmbeddingModel = openAiEmbeddingModel;
        this.vectorStore = vectorStore;
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


    @PostMapping("/products")
    public List<Document> getSimiliarProducts(@RequestParam("text")String text){
        //return vectorStore.similaritySearch(text); //for all similarities
        return vectorStore.similaritySearch(SearchRequest.builder().query(text).topK(2).build());
    }
}
