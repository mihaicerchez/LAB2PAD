package com.example.controller;

import com.example.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@AllArgsConstructor
public class SmartController {

    private final SmartUserService service;
    private final SyncService syncService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private Environment env;

    @GetMapping("/users")
    @ResponseBody
    public List<User> users(@RequestParam(name="name", required=false, defaultValue="again") String name, Model model) {
        return service.getAll();
    }

    @GetMapping("/postput")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "adduser";
    }

    @RequestMapping(
            value = "/users",
            method = {RequestMethod.POST, RequestMethod.PUT})
    public String addNewUser(@ModelAttribute("user") User user) throws IOException, URISyntaxException, InterruptedException {
        service.saveUser(user);
        User newuser = service.getLastUser();
        Entity entity = new Entity(newuser, env.getProperty("source"));
        String body = objectMapper.writeValueAsString(entity);
        System.out.println("\n\n\n"+body);
        syncService.syncSendEntity(entity);
        //Thread.sleep(10000);
        return "redirect:/users";

    }
    @PostMapping(value = "/sync", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String syncro(@RequestBody User user) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Was Synchronized");
        System.out.println("UserID: " + user.getId());
        service.saveUser(user);
        return user.getEmail();
    }




}
