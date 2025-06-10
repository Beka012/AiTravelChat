package com.example.AuthService.service;

import com.example.AuthService.dto.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    @Value("754f07732dea76b3349f0388414d8906060f4ef8a6730aa9cb975d5ae1ea4891d34946cdd8aaf7f221776f96b1a974d82c7971249c4083e46030c517b7a0d758eca7bda020ec5b65a00af8004a2b9cc42043912c41ed313025a729937d2d96b921d6f2827d077f0e400116357409a01df942fcb37f11e57141f25c6fe615793ac7fe757626a63a5866e7a3b77ef4f69a9a098c5621ab0b196d85414b1391664737b1e9d307fcca3f304bc7eab1ebb7c1df54914c3b551984cf06fcc2f668999673e17d8dc8ceec6f29b5013d8b17f838fb4325832eefbab5b6701422f61ed2b793c1cdb20e4b11d9e8447d06a6992a3fb9a293b913970b7719708db258611eab")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationMs;


    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);

        return new TokenPair(accessToken, refreshToken);
    }

    // Generate access token
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtExpirationMs, new HashMap<>());
    }

    // Generate refresh token
    public String generateRefreshToken(Authentication authentication) {
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        return generateToken(authentication, refreshExpirationMs, claims);
    }

    private String generateToken(Authentication authentication, long expirationInMs, Map<String, String> claims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date(); // Time of token creation
        Date expiryDate = new Date(now.getTime() + expirationInMs); // Time of token expiration

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userPrincipal.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    // Validate token
    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return username != null
                && username.equals(userDetails.getUsername());
    }

    public boolean isValidToken(String token) {
        return extractAllClaims(token) != null;
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);

        if(claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    // Validate if the token is refresh token
    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        if(claims == null) {
            return false;
        }
        return "refresh".equals(claims.get("tokenType"));
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return claims;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
