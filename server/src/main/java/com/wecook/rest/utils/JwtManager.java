package com.wecook.rest.utils;

import com.wecook.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class JwtManager {
    private static final String SECRET_KEY = "4SE6IRZWpYkLRdvs7NsYWcsz330P4tHH1bHIM0vyVig";
    private static final long EXPIRATION_TIME = 86400000; // 1 giorno

    private static final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    private static JwtManager instance;
    private final ConcurrentHashMap<String, Long> tokens = new ConcurrentHashMap<>();

    private JwtManager() {
    }

    public static JwtManager getInstance() {
        if (instance == null) {
            synchronized (JwtManager.class) {
                if (instance == null) {
                    instance = new JwtManager();
                }
            }
        }
        return instance;
    }

    public String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        tokens.put(token, user.getId()); // Mappa il token all'ID utente
        return token;
    }

    public String validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return claimsJws.getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public void invalidateToken(String token) {
        tokens.remove(token);
    }

    public Long getUserId(String token) {
        return tokens.get(token);
    }
}
