package com.example.demo4.controller;

import com.example.demo4.model.User;
import com.example.demo4.model.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    private Map<String, Long> usersSessions = new HashMap<>();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Long findUserId(String sessionId) {
        return usersSessions.get(sessionId);
    }

    public void endSession(String sessionId) {
        usersSessions.remove(sessionId);
    }

    public boolean checkUserByEmail(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        return !userOptional.isEmpty();
    }

    //if there is no such user returns null
    public Long login(String email, String password, String sessionId) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();
        if (!checkPassword(password, user.getPassword())) return null;

        usersSessions.put(sessionId, user.getId());
        return user.getId();
    }

    private boolean checkPassword(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void addUser(User user) {
        userRepo.save(user);
    }

    public void removeUser(User user) {
        userRepo.delete(user);
    }

    public User findUser (String email, String password) {
        //userRepo.findBy();
        //userRepo.fin
        return null;
    }

    public void addUser(String email, String password) {
        String encodedPassword = encoder.encode(password);
        User user = new User("No name", email, encodedPassword);
        addUser(user);
    }
}