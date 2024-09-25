package com.example.SpringSecurity.service;


import com.example.SpringSecurity.model.Users;
import com.example.SpringSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private  JWTService jwtService;


  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public Users register( Users user){

        user.setPassword(encoder.encode(user.getPassword()));

        Users saveduser = userRepository.save(user);

        return saveduser;

    }

    public String verify(Users user) {

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));


        if(authentication.isAuthenticated()){
            return jwtService.generatToken(user.getUsername());
        }
        return "failure";




    }
}
