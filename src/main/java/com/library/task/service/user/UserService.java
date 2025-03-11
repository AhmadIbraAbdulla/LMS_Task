package com.library.task.service.user;

import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.models.Role;
import com.library.task.models.User;
import com.library.task.infrastructure.repositories.RoleRepository;
import com.library.task.infrastructure.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  implements IUserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public Optional<Role> findRoleByName(String roleName) {
        return Optional.ofNullable(roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)));
    }
}

