package com.karhabtiapp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtil {

 private static final Logger LOGGER = Logger.getLogger(JwtUtil.class.getName());

//  private static final String SECRET_KEY = "key1234";
@Value("${application.security.jwt.secret-key}")
public String SECRET_KEY ;
@Value("${application.security.jwt.expiration}")
private long jwtExpiration;
@Value("${application.security.jwt.refresh-token.expiration}")
private long refreshExpiration;

    public String extractUsername(String token) {
        if (token != null) {
            Claims claims = extractAllClaims(token);
            return (claims != null) ? claims.getSubject() : null;
        } else {
            LOGGER.warning("Token is null in extractUsername");
            throw new IllegalArgumentException("Token is null in extractUsername");
        }
    }

    public String generateToken(UserDetails userDetails) {
        LOGGER.info("Generating token for user: " + userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expirationTime
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return (expiration != null) && expiration.before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();


}



    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (username != null) {
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } else {
            LOGGER.warning("Username is null in validateToken");
            return false;
        }
    }
}
