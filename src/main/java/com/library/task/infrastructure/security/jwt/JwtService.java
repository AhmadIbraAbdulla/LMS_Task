package com.library.task.infrastructure.security.jwt;


import com.library.task.infrastructure.security.CustomUserDetailsService;
import com.library.task.shared.dtos.auth.LogInRequestDto;
import com.library.task.infrastructure.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements IJwtService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String createJwtToken(LogInRequestDto authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        Long userId = ((CustomUserDetails) userDetails).getId();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return jwtUtil.generateToken(userId, role);
    }
}

