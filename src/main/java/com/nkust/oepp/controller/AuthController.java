package com.nkust.oepp.controller;

import com.nkust.oepp.dto.LoginRequest;
import com.nkust.oepp.dto.LoginResponse;
import com.nkust.oepp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        // 從 Header 中提取 Token
        final String requestTokenHeader = request.getHeader(header);
        String token = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(prefix + " ")) {
            token = requestTokenHeader.substring(prefix.length() + 1);
        }

        authService.logout(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "登出成功");
        return ResponseEntity.ok(response);
    }
}

