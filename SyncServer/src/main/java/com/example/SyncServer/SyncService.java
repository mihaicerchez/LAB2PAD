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

    private ArrayList<String> hosts = new ArrayList<String>();

    public void addUser(Entity entity) {
        entitys.add(entity);
    }

    public void addHost(String host) {
        if (!hosts.contains(host)){
            hosts.add(host);
        }
        else
        {
            System.out.println("Host is already exists");
        }
    }

    public void work(Entity entity) throws URISyntaxException, IOException, InterruptedException {
        if (!entitys.contains(entity)) {
            addUser(entity);
            toSync(entity);
        } else {
            System.out.println("FAILED");
        }
    }

    ObjectMapper objectMapper = new ObjectMapper();

    public void toSync(Entity entity) throws URISyntaxException, IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(entity.getUser());
        for (String i : hosts) {
            System.out.println("Sending to host"+ i);
            if (!i.equals(entity.getSource())) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://" + i + "/sync"))
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .headers("Content-Type", "application/json")
                        .build();
                CompletableFuture<HttpResponse<String>> response = client
                        .sendAsync(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response);
                System.out.println("Send to "+i);
            }
        }
    }
}
