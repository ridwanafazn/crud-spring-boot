package com.api.inventorix.service;

import com.api.inventorix.model.Admin;
import com.api.inventorix.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin signup(String email, String username, String rawPassword) throws Exception {
        if (adminRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email already registered");
        }
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username already taken");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Admin admin = new Admin(email, username, encodedPassword);
        return adminRepository.save(admin);
    }

    public Optional<Admin> login(String email, String rawPassword) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (passwordEncoder.matches(rawPassword, admin.getPassword())) {
                return Optional.of(admin);
            }
        }
        return Optional.empty();
    }
}