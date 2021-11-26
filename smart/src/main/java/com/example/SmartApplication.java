package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class SmartApplication {

    @Value("${source}")
    String addr;

    @Autowired
    private UserMongoRepository userMongoRepository;
    private SyncService syncservice;
    private SmartUserService service;
    public static void main(String[] args) {
        SpringApplication.run(SmartApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterTheStart() throws URISyntaxException, IOException, InterruptedException {
        syncservice = new SyncService();
        service = new SmartUserService(userMongoRepository);
        //service.eraseDB();
        System.out.println("the application started...");

        service.eraseDB();
        User user = new User();
        user.setAge(25);
        user.setEmail("test1@gmail.com");
        user.setName("test1_name");
        System.out.println("saving user to the db");
        service.saveUser(user);
        System.out.println("getting users from the db");
        service.getAll().forEach(System.out::println);
        System.out.println("getting user by email");
        System.out.println(service.getByEmain("test1@gmail.com"));
        syncservice.subscribe(addr);
    }
}
