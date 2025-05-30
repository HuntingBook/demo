package com.flightapi.config;

import com.flightapi.service.UserDetailsServiceImpl;
import com.flightapi.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that handles JWT-based authentication for incoming requests.
 * This filter extends {@link OncePerRequestFilter} to ensure it's executed once per request.
 * It extracts the JWT from the Authorization header, validates it, and if valid,
 * sets the authentication in the Spring Security context.
 */
@Component // Ensure this filter is picked up as a Spring bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /**
     * Utility class for JWT token generation, validation, and extraction of claims.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Service for loading user-specific data. Used to fetch {@link UserDetails} for authentication.
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * Processes an incoming HTTP request to perform JWT authentication.
     * It extracts the JWT from the 'Authorization' header, validates it using {@link JwtUtil},
     * and if the token is valid and the user is not already authenticated, it loads the user details
     * using {@link UserDetailsServiceImpl} and sets up the Spring Security context.
     * Catches various JWT-related exceptions and logs them.
     *
     * @param request The incoming {@link HttpServletRequest}.
     * @param response The outgoing {@link HttpServletResponse}.
     * @param filterChain The {@link FilterChain} to pass the request along.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                String username = jwtUtil.extractUsername(jwt);

                // Check if user is already authenticated
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("Set Authentication in SecurityContextHolder for user: {}", username);
                    } else {
                        logger.warn("JWT token validation failed for user: {}", username);
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("JWT token is malformed: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("JWT signature validation failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // This can happen if the token is empty or if the secret key is invalid during parsing
            logger.warn("JWT claims string is empty or argument is invalid: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT from the 'Authorization' header of the request.
     * The token is expected to be prefixed with "Bearer ".
     *
     * @param request The {@link HttpServletRequest} from which to extract the token.
     * @return The JWT string if found and correctly formatted, otherwise null.
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
