package org.example.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.SECRET}")
    private String SECRET;

    public  String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role",user.getRole())
                .claim("email",user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(SignatureAlgorithm.HS256,SECRET)
                .compact();
    }
}
