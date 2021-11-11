package com.example.SyncServer;


import org.springframework.beans.factory.annotation.Autowired;



public class Entity {
    private User user;
    private String source;

    public Entity(User user, String source) {
        this.user = user;
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
