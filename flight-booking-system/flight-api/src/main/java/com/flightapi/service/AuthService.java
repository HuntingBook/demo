package com.flightapi.service;

import com.flightapi.dto.AuthResponseDTO;
import com.flightapi.dto.LoginRequestDTO;
import com.flightapi.dto.RegisterRequestDTO;
import com.flightapi.dto.UserDTO;
import com.flightapi.entity.User;
import com.flightapi.exception.EmailAlreadyExistsException;
import com.flightapi.repository.UserRepository;
import com.flightapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling authentication-related operations such as user registration and login.
 */
@Service
public class AuthService {

    /**
     * Repository for user data access.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Encoder for user passwords.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Manages authentication processes.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Utility for JWT generation and validation.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user in the system.
     *
     * @param registerRequestDTO DTO containing user registration details.
     * @return AuthResponseDTO containing the JWT and user details upon successful registration.
     * @throws EmailAlreadyExistsException if the email provided already exists in the system.
     */
    @Transactional
    public AuthResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + registerRequestDTO.getEmail());
        }

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setCountry(registerRequestDTO.getCountry());
        user.setPhone(registerRequestDTO.getPhone());

        User savedUser = userRepository.save(user);

        // Directly use the email from the saved user for token generation
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponseDTO(token, UserDTO.fromUser(savedUser));
    }

    /**
     * Logs in an existing user.
     *
     * @param loginRequestDTO DTO containing user login credentials.
     * @return AuthResponseDTO containing the JWT and user details upon successful login.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    @Transactional(readOnly = true)
    public AuthResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication")); // Should not happen

        String token = jwtUtil.generateToken(userDetails.getUsername());
        return new AuthResponseDTO(token, UserDTO.fromUser(user));
    }
}
