package com.flightapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Tokens (JWTs).
 * Provides methods for generating, validating, and extracting claims from JWTs.
 */
@Component
public class JwtUtil {

    /**
     * The secret key used for signing and verifying JWTs. Injected from application properties.
     * Must be at least 256 bits (32 bytes) long for HS256 algorithm.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * The expiration time for JWTs in milliseconds. Injected from application properties.
     */
    @Value("${jwt.expirationMs}")
    private long expirationMs;

    /**
     * Generates the signing key from the configured secret string.
     *
     * @return The {@link SecretKey} used for JWT signing.
     * @throws IllegalArgumentException if the configured JWT secret key is not strong enough (less than 256 bits).
     */
    private SecretKey getSigningKey() {
        // Ensure the secret key is strong enough for the HS256 algorithm
        // Keys.hmacShaKeyFor requires a byte array of at least 256 bits (32 bytes)
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key is not strong enough. It must be at least 256 bits (32 bytes).");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (subject) from the given JWT.
     *
     * @param token The JWT string.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT.
     *
     * @param token The JWT string.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT using a claims resolver function.
     *
     * @param token The JWT string.
     * @param claimsResolver A function that takes {@link Claims} and returns the desired claim value.
     * @param <T> The type of the claim to be extracted.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT.
     *
     * @param token The JWT string.
     * @return The {@link Claims} object containing all claims from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the given JWT is expired.
     *
     * @param token The JWT string.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT for the given {@link UserDetails}.
     * The username from UserDetails is used as the subject of the token.
     *
     * @param userDetails The user details for whom the token is to be generated.
     * @return The generated JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add any additional claims if needed, e.g., roles
        // claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Generates a JWT for the given username.
     *
     * @param username The username (subject) for whom the token is to be generated.
     * @return The generated JWT string.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Creates a JWT with the given claims and subject.
     *
     * @param claims A map of claims to include in the token.
     * @param subject The subject of the token (typically the username).
     * @return The generated JWT string.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the given JWT against the provided {@link UserDetails}.
     * Checks if the username in the token matches the UserDetails' username and if the token is not expired.
     *
     * @param token The JWT string to validate.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
