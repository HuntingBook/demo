package com.flightapi.service;

import com.flightapi.entity.User;
import com.flightapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Implementation of Spring Security's {@link UserDetailsService}.
 * This service is responsible for loading user-specific data.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Repository for user data access.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Loads the user by their email address (which is used as the username in this application).
     *
     * @param email The email address of the user to load.
     * @return UserDetails object containing the user's information.
     * @throws UsernameNotFoundException if no user is found with the given email address.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // For now, using an empty list of authorities.
        // This can be expanded later to include roles/permissions.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // Empty authorities list
        );
    }
}
