package com.example.SyncServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SyncService {
    HttpClient client = HttpClient.newHttpClient();

    private ArrayList<Entity> entitys = new ArrayList<Entity>();

    public void addUser(Entity entity){
        entitys.add(entity);
    }

    public void work(Entity entity) throws URISyntaxException, IOException, InterruptedException {
        if (!entitys.contains(entity))
        {
            addUser(entity);
            toSync(entity);
        }
        else
        {
            System.out.println("FAILED");
        }
    }
    ObjectMapper objectMapper = new ObjectMapper();

    public CompletableFuture<HttpResponse<String>> toSync(Entity entity) throws URISyntaxException, IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(entity.getUser());
        if (entity.getSource().equals("localhost:9001"))
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9002/sync"))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .headers("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response = client
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            System.out.println("Send to 9002");
            return response;
        }
        else {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9001/sync"))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .headers("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response = client
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Send to 9001");
            System.out.println(response);
            return response;
        }
    }


}
