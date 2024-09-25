package com.example.SpringSecurity.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTFilter jwtFilter;


    @Bean
    public SecurityFilterChain getsecurityfilterchain(HttpSecurity http) throws Exception {
        //In this basically http is the filter chain object and we are customizering the filter here without default configuration

        http.csrf(customizer -> customizer.disable());
        http.authorizeHttpRequests(request -> request.requestMatchers("/register","/login").permitAll().anyRequest().authenticated());
// By above we can not acccess the home page because it requires authenticated for that we will use below form login with default customization like below
        http.formLogin().disable();
        //By above line after running the code when visits home page it will ask default login form and need to use the username and password that setted in application properties file

        //when we want to check authentication by rest APIS we need to write the below code

        http.httpBasic(Customizer.withDefaults()); //this line will help us to test with REst API clients like POSTMAN

        //Now instead of generating csrf token we can make our http statless which means we are generating sessionID new each and everytime when user making request. In that case we dont have to worry about sessionID. That can be acheived by nelow line
//        every time the user makes a request (like POST, PUT, etc.), you would validate the session ID included with that request. Here’s a brief overview of how this works:
//
//        1) User Makes a Request: The user sends an HTTP request (e.g., POST or PUT) along with a newly generated session ID.
//
//         2) Server Receives the Request: The server receives the request and extracts the session ID from the headers or parameters.
//
//          3) Session ID Validation: The server checks the validity of the session ID:
//
//        4) Check if it exists: Ensure the session ID is recognized and valid.
//        5) Check for expiration: Ensure the session ID hasn’t expired.
//        6) Process the Request: If the session ID is valid, proceed to process the request. If it’s invalid, respond with an error (e.g., 401 Unauthorized).
//
//        7)New Session ID Generation (if needed): Depending on your design, you might generate a new session ID for the next request as well.


        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build(); //this return filter chain
    }


//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                return null;
//            }
//        }

    //Below method convert unauthenticated object into authenticated ones

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));

        provider.setUserDetailsService(userDetailsService);

        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }




}
