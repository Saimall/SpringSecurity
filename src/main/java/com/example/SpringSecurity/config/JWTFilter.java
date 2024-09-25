package com.example.SpringSecurity.config;

import com.example.SpringSecurity.service.JWTService;
import com.example.SpringSecurity.service.Myuserdetailsservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter  extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    ApplicationContext context;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username=null;
        String token=null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
             token = authHeader.substring(7);

             username = jwtService.extractUsername(token);
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) { // Checking the username is not null and token is not authenticated already

            //after verification of token and amke sure token is not authenticated as above we will get the user details from Databse as beloww
            UserDetails userDetails = context.getBean(Myuserdetailsservice.class).loadUserByUsername(username); //this will get the user deatils object with using user name from Database;

            //validated the token and check if matching or  not by below vaildateToken function
            if(jwtService.validateToken(token,userDetails)){
                //as next filter works with usernamepassword authentication filter but i want to pass token there so i will use

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                //as authToken knows about user but it dont have any idea on request object so we should also know about request object so we will set this object in authtoken

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //as we checked above that token is not authenticate mutiple times

                // we need to set it has authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        //Now we will say continue filter
        filterChain.doFilter(request,response);
    }
}

//So NOw filter work is done it will validate token and creates authentication object and it will pass to next.
