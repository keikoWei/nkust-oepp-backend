package com.nkust.oepp.config;

import com.nkust.oepp.entity.Role;
import com.nkust.oepp.entity.User;
import com.nkust.oepp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 初始化所有角色的測試帳號
        createUserIfNotExists("superadmin", "superadmin", Role.SUPER_ADMIN);
        createUserIfNotExists("headquarters", "headquarters", Role.HEADQUARTERS);
        createUserIfNotExists("education", "education", Role.EDUCATION_CENTER);
        createUserIfNotExists("product", "product", Role.PRODUCT_CENTER);
        createUserIfNotExists("exhibition", "exhibition", Role.EXHIBITION_CENTER);
        createUserIfNotExists("management", "management", Role.MANAGEMENT_CENTER);
        
        System.out.println("========================================");
        System.out.println("測試帳號已建立（帳號/密碼相同）:");
        System.out.println("1. superadmin - 大帳號（所有權限）");
        System.out.println("2. headquarters - 處本部");
        System.out.println("3. education - 教育推廣中心");
        System.out.println("4. product - 產品推廣中心");
        System.out.println("5. exhibition - 會展管理中心");
        System.out.println("6. management - 經營管理中心");
        System.out.println("========================================");
    }

    private void createUserIfNotExists(String username, String password, Role role) {
        if (!userRepository.existsByUsername(username)) {
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            userRepository.save(user);
        }
    }
}

