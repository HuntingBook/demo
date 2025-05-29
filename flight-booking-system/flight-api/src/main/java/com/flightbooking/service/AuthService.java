package com.flightbooking.service;

import com.flightbooking.dto.AuthResponseDTO;
import com.flightbooking.dto.LoginRequestDTO;
import com.flightbooking.dto.RegisterRequestDTO;
import com.flightbooking.dto.UserDTO;
import com.flightbooking.entity.User;
import com.flightbooking.exception.EmailAlreadyExistsException;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

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
