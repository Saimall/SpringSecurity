package com.example.SpringSecurity.controller;


import com.example.SpringSecurity.model.Users;
import com.example.SpringSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user){

    Users user1 =  userService.register(user);
    return user1;

    }

    @PostMapping("/login")
    public String login(@RequestBody Users user){

         return userService.verify(user);

    }


}
