package com.library.task.infrastructure.seeders;


import com.library.task.infrastructure.repositories.RoleRepository;
import com.library.task.infrastructure.repositories.UserRepository;
import com.library.task.models.Role;
import com.library.task.models.User;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedDatabase() {
        seedRoles();
        seedAdminUser();
    }
    public void seedRoles() {
        if (roleRepository.count() == 0) {
            // Create a list of roles to insert
            List<Role> roles = Arrays.asList(
                    new Role(null, "ROLE_ADMIN"),
                    new Role(null, "ROLE_PATRON"),
                    new Role(null, "ROLE_LIBRARIAN")
            );

            // Save all roles in a single database operation
            roleRepository.saveAll(roles);
        }
    }

    private void seedAdminUser() {
        if (userRepository.findByEmail("admin@library.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN does not exist"));

            User admin = new User();
            admin.setEmail("admin@library.com");
            admin.setPassword(passwordEncoder.encode("admin123Admin"));
            admin.setRole(adminRole);

            userRepository.save(admin);
        } else {
            System.out.println("⚠️ Admin user already exists. Skipping seeding.");
        }
    }
}