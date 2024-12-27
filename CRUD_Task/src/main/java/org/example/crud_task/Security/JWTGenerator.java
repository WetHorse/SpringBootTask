package org.example.crud_task.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTGenerator {



    public String generateToken(String username) {
        long expirationTime = 1000 * 60 * 60; // 1 hour
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SECRET.getBytes())
                .compact();
    }



    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.JWT_SECRET.getBytes())
                .build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstant.JWT_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token);

            return true;
        }catch (Exception ex){
            throw new AuthenticationCredentialsNotFoundException("JWT error: ", ex);
        }
    }
}

