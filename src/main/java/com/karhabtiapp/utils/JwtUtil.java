package com.karhabtiapp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtil {

    private static final Logger LOGGER = Logger.getLogger(JwtUtil.class.getName());

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long ACCESS_TOKEN_EXPIRATION_MS = 1000 * 60 * 60;

    //  (7 days)
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7;

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
        return generateToken(new HashMap<>(), userDetails, ACCESS_TOKEN_EXPIRATION_MS);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, REFRESH_TOKEN_EXPIRATION_MS);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        if (claims != null) {
            return claimsResolver.apply(claims);
        } else {
            LOGGER.log(Level.WARNING, "Claims is null in extractClaim for token: {0}", token);
            throw new IllegalArgumentException("Claims is null in extractClaim");
        }
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationTime) {
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY).compact();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return (expiration != null) && expiration.before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            if (token != null) {
                LOGGER.info("Token: " + token);
                return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            } else {
                LOGGER.warning("Token is null in extractAllClaims");
                throw new IllegalArgumentException("Token is null in extractAllClaims");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error extracting claims from token: {0}, Error: {1}", new Object[]{token, e.getMessage()});
            return null;
        }
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
