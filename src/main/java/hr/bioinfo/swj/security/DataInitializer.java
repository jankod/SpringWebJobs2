package hr.bioinfo.swj.security;

import hr.bioinfo.swj.model.Role;
import hr.bioinfo.swj.model.User;
import hr.bioinfo.swj.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Lazy(false) // Ensure this bean is created at startup
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        log.info("Initializing default users");
        
        // Create super_admin user if not exists
        if (!userRepository.existsByUsername("super_admin")) {
            User superAdmin = new User();
            superAdmin.setUsername("super_admin");
            superAdmin.setPassword(passwordEncoder.encode("super_admin"));
            superAdmin.setEmail("super_admin@example.com");
            superAdmin.setFirstName("Super");
            superAdmin.setLastName("Admin");
            superAdmin.setEnabled(true);
            superAdmin.addRole(Role.SUPER_ADMIN);
            
            userRepository.save(superAdmin);
            log.info("Created super_admin user");
        }
        
        // Create admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEnabled(true);
            admin.addRole(Role.ADMIN);
            
            userRepository.save(admin);
            log.info("Created admin user");
        }
        
        // Create regular user if not exists
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("user@example.com");
            user.setFirstName("Regular");
            user.setLastName("User");
            user.setEnabled(true);
            user.addRole(Role.USER);
            
            userRepository.save(user);
            log.info("Created regular user");
        }
    }
}