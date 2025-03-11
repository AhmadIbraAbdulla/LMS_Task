package com.library.task.infrastructure.security;


import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.models.User;
import com.library.task.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(
                user.get().getId(),
                user.get().getEmail(),
                user.get().getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.get().getRole().getName()))
        );
    }

    public UserDetails loadUserById(Long userId) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }


        return new CustomUserDetails(
                user.get().getId(), // User ID
                user.get().getEmail(), // Email
                user.get().getPassword(), // Password
                Collections.singletonList(new SimpleGrantedAuthority(user.get().getRole().getName())) // Role
        );
    }
}
