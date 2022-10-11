package com.cardgame.config.Auth.security.Securitytwo.Security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {

    private Map<String, Object> claims = new HashMap<String, Object>();

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;
    public String generateToken(Authentication authentication){
        log.info("logging authentication before generating token {}",authentication);
        UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();
//        claims.put("id", userPrincipal.getId());
//        claims.put("UID", userPrincipal.getUid());
//        claims.put("username", userPrincipal.getUsername());
//        claims.put("mobile", userPrincipal.getMobilenumber());

        return Jwts.builder()
//                .setId(UUID.randomUUID().toString())
                .setSubject(Long.toString(userPrincipal.getUid()))
//                .setSubject(Long.toString(userPrincipal.getId()))
//                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    public Long getuserIdfromJwt(String token){
        Claims claims= Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
          log.info("logging the claims {}",claims);
        return Long.parseLong(claims.getSubject());
    }
    public Long jwtExpirationInMs(){
        return jwtExpirationInMs;
    }
    public boolean validateToken( String authToken){
        try {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
        return true;
        }catch (SignatureException| MalformedJwtException| ExpiredJwtException|UnsupportedJwtException|IllegalArgumentException exception){
            log.error("an exception has occurred while validating the json web token {}", exception.getMessage());
        }
        return false;
    }

    public String generatetokenwithusername(Long userId) {

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256,jwtSecret)
                .compact();
    }
}
