package com.example.SyncServer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@AllArgsConstructor
public class SyncController {
    SyncService syncService;


    @PostMapping(value = "/sync", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String addNewUser(@RequestBody Entity entity) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("New entity has been added to syncro");
        System.out.println("Source: "+entity.getSource());
        System.out.println("ID: "+entity.getUser().getId());
        syncService.work(entity);
        return entity.getSource();
    }
}
