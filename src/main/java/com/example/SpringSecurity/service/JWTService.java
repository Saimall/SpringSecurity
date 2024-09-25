package com.example.SpringSecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {


    //for timebeing creating a const key but it is not optimized

    private String secretkey="";


    //generation of secretkey . This below logic can be also included in GetKey method as well
    public JWTService(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }


    public String generatToken(String username) {

        Map<String,Object> claims=new HashMap<>(); // this are nothing but storeing as key, value pairs in token as we copy token and past in jwt.io we got
        //payload and header in key value paris which is map. so for that reason we used clamins

        return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 60*60*30)).and().signWith(getKey()).compact();

    }

    private SecretKey getKey() {

        byte[] keyBytes= Decoders.BASE64.decode(secretkey); //this will convert string into bytes

        return Keys.hmacShaKeyFor(keyBytes); // we used keys class and used hmac and passed the bytes to convert into string
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject); //as subject contains username so we are specifying what we need from claims .
    }

    private <T> T extractClaim(String token, Function<Claims,T>claimsResolver) {
        final  Claims claims = extractAllClaim(token);
        return  claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        //we need to use the key to extarct all claims
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }



    public boolean validateToken(String token, UserDetails userDetails) {

        //for validating we need to check whether the token is experied or not
        final String userName=extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); //if expiration time is before new Date() then it will gives false other wise it will return true;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

}
