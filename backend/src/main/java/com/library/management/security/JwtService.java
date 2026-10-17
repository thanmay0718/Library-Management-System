package com.library.management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

//generateToken()	Creates a signed JWT for the logged-in user.
//extractUsername()	Retrieves the subject, which in your project should be the user's email if that is the username used by UserDetails.
//extractClaim()	Retrieves a specific JWT claim.
//extractAllClaims()	Verifies the signature and parses the token.
//isTokenValid()	Checks that the token belongs to the user and has not expired.

@Service
public class JwtService {

    private final SecretKey singingKey;
    private static final long JWT_EXPIRATION = 24 * 60 * 60 * 1000L;

    public JwtService(@Value("${jwt.secret}") String secret){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.singingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(singingKey)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(singingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) &&
                extractAllClaims(token)
                        .getExpiration()
                        .after(new Date());
    }
}
