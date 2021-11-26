package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class SyncService {
    HttpClient client = HttpClient.newHttpClient();


    ObjectMapper objectMapper = new ObjectMapper();

    public CompletableFuture<HttpResponse<String>> syncSendEntity(Entity entity) throws URISyntaxException, IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(entity);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:9010/sync"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        CompletableFuture<HttpResponse<String>> response = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Entity has been sent to the SyncServer");
        System.out.println(response);
        return response;
    }

    public void subscribe(String addr) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:9010/subscribe"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(addr))
                .build();

        CompletableFuture<HttpResponse<String>> response = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("This node has been subscribed to sync server");
        System.out.println(response);
    }




}
