package com.nkust.oepp.service;

import com.nkust.oepp.dto.LoginRequest;
import com.nkust.oepp.dto.LoginResponse;
import com.nkust.oepp.entity.User;
import com.nkust.oepp.repository.UserRepository;
import com.nkust.oepp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String jwt = jwtUtil.generateToken(user.getUsername());

            return LoginResponse.builder()
                    .token(jwt)
                    .type("Bearer")
                    .username(user.getUsername())
                    .role(user.getRole().getCode())
                    .roleName(user.getRoleName())
                    .id(user.getId())
                    .build();

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }

    /**
     * 登出，將 Token 加入黑名單
     */
    public void logout(String token) {
        if (token != null && !token.isEmpty()) {
            tokenBlacklistService.blacklistToken(token);
        }
    }
}

