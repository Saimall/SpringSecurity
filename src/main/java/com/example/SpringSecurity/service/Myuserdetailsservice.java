package com.example.SpringSecurity.service;

import com.example.SpringSecurity.model.UserPrinciple;
import com.example.SpringSecurity.model.Users;
import com.example.SpringSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class Myuserdetailsservice implements UserDetailsService {


    @Autowired
    private UserRepository repository;

    //now we want below to connect with user repo
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

Users user = repository.findByUsername(username);

if(user==null){
    System.out.println("User not found");
    throw new UsernameNotFoundException("USer not found");
}
return new UserPrinciple(user);

    }
}
