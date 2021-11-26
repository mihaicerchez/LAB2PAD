package com.example;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SmartUserService {


    private final UserMongoRepository userMongoRepository;

    public List<User> getAll() {return userMongoRepository.findAll();}

    public void saveUser(User user) {userMongoRepository.save(user); }

    public void eraseDB() {userMongoRepository.deleteAll(); }

    public User getByEmain(String email){return userMongoRepository.findFirstByEmail(email);}

    public User getLastUser() {
        return getAll().get(getAll().size()-1);
    }



}
