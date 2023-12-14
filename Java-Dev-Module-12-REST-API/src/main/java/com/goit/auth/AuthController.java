package com.goit.auth;

import com.goit.auth.dto.login.LoginRequest;
import com.goit.auth.dto.registration.RegistrationRequest;
import com.goit.generic_response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public Response register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }
    @PostMapping("/login")
    public Response register(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}